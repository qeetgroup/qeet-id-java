package com.qeetgroup.qeetid;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jspecify.annotations.Nullable;

/**
 * One page of a cursor-paginated list. Iterate it, or {@link #stream()} it, to walk every item from
 * this page to the end of the list, fetching later pages as needed; or step through pages with
 * {@link #hasNextPage()} and {@link #nextPage()}.
 *
 * <pre>{@code
 * for (User user : qeetId.users().list()) { ... }          // every user, page by page
 * Page<User> page = qeetId.users().list(params);           // one page
 * page.data();                                             // its items
 * }</pre>
 *
 * <p>A page is immutable. Fetching a later page sends a request and may throw what any call throws.
 *
 * @param <T> the item type
 */
public final class Page<T> implements Iterable<T> {
  private final List<T> data;
  private final @Nullable String nextCursor;
  private final Function<String, Page<T>> fetch;

  private Page(List<T> data, @Nullable String nextCursor, Function<String, Page<T>> fetch) {
    this.data = List.copyOf(data);
    this.nextCursor = nextCursor;
    this.fetch = fetch;
  }

  /**
   * A page, for tests and fakes: its items, the next page's cursor, and how to fetch a page.
   *
   * @param data the items
   * @param nextCursor the next page's cursor, or null on the last page
   * @param fetch fetches the page a cursor names
   * @param <T> the item type
   * @return the page
   */
  public static <T> Page<T> of(
      List<T> data, @Nullable String nextCursor, Function<String, Page<T>> fetch) {
    return new Page<>(data, nextCursor, Objects.requireNonNull(fetch, "fetch"));
  }

  /**
   * The items on this page.
   *
   * @return the items
   */
  public List<T> data() {
    return data;
  }

  /**
   * The cursor of the next page; pass it as {@code cursor} to start a later listing there.
   *
   * @return the cursor, or null on the last page
   */
  public @Nullable String nextCursor() {
    return nextCursor;
  }

  /**
   * Whether another page follows this one.
   *
   * @return whether there is a next page
   */
  public boolean hasNextPage() {
    return nextCursor != null;
  }

  /**
   * Fetches the next page.
   *
   * @return the next page
   * @throws NoSuchElementException on the last page; check {@link #hasNextPage()} first
   */
  public Page<T> nextPage() {
    String cursor = nextCursor;
    if (cursor == null) {
      throw new NoSuchElementException("This is the last page: there is no next page.");
    }
    return fetch.apply(cursor);
  }

  /**
   * This page and every page after it, fetched as the iteration reaches them.
   *
   * @return the pages
   */
  public Iterable<Page<T>> pages() {
    return PageIterator::new;
  }

  /**
   * Every item from this page to the end of the list, fetching pages as needed.
   *
   * @return the items
   */
  @Override
  public Iterator<T> iterator() {
    Iterator<Page<T>> pages = new PageIterator();
    return new Iterator<>() {
      private Iterator<T> items = List.<T>of().iterator();

      @Override
      public boolean hasNext() {
        while (!items.hasNext() && pages.hasNext()) {
          items = pages.next().data().iterator();
        }
        return items.hasNext();
      }

      @Override
      public T next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return items.next();
      }
    };
  }

  /**
   * Every item from this page to the end of the list, as a lazy stream.
   *
   * @return the items
   */
  public Stream<T> stream() {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED | Spliterator.NONNULL),
        false);
  }

  @Override
  public String toString() {
    return "Page[items=" + data.size() + ", hasNextPage=" + hasNextPage() + "]";
  }

  /**
   * Walks pages, fetching each only when asked for it, and stops if the server repeats a cursor,
   * which would otherwise loop forever.
   */
  private final class PageIterator implements Iterator<Page<T>> {
    private final Set<String> seen = new HashSet<>();
    private @Nullable Page<T> pending = Page.this;
    private @Nullable Page<T> last;

    @Override
    public boolean hasNext() {
      if (pending != null) {
        return true;
      }
      Page<T> previous = last;
      String cursor = previous == null ? null : previous.nextCursor;
      if (previous == null || cursor == null || !seen.add(cursor)) {
        last = null;
        return false;
      }
      pending = previous.nextPage();
      return true;
    }

    @Override
    public Page<T> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Page<T> current = java.util.Objects.requireNonNull(pending);
      pending = null;
      last = current;
      return current;
    }
  }
}
