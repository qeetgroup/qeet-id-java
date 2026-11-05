package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Paths;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.Objects;

/**
 * Manages relationship tuples (ReBAC) and answers relationship checks.
 *
 * <p>Get it from {@code qeetId.relationships()}. Safe for concurrent use.
 */
public final class Relationships {
  private final Transport http;

  /**
   * Used by the client; get this from {@code qeetId.relationships()}.
   *
   * @param http the client's transport
   */
  public Relationships(Transport http) {
    this.http = http;
  }

  /**
   * Writes a tuple.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the relation tuple
   */
  public RelationTuple create(String tenantId, RelationTupleCreateParams params) {
    return create(tenantId, params, RequestOptions.none());
  }

  /**
   * Writes a tuple. With per-call settings: see {@link #create(String, RelationTupleCreateParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the relation tuple
   */
  public RelationTuple create(
      String tenantId, RelationTupleCreateParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/relation-tuples", tenantId)).json(params),
        options,
        RelationTuple.class);
  }

  /**
   * Lists the tuples on an object (who has what on it).
   *
   * @param tenantId the organisation's id
   * @param object the object, {@code type:id}
   * @return the tuples
   */
  public ListResult<RelationTuple> listOnObject(String tenantId, String object) {
    return listOnObject(tenantId, object, RequestOptions.none());
  }

  /**
   * Lists the tuples on an object, with per-call settings. See {@link #listOnObject(String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param object the object, {@code type:id}
   * @param options per-call settings
   * @return the tuples
   */
  public ListResult<RelationTuple> listOnObject(
      String tenantId, String object, RequestOptions options) {
    ApiRequest request =
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/relation-tuples", tenantId))
            .query("object", Objects.requireNonNull(object, "object"));
    return http.list(request, RelationTuple.class, options);
  }

  /**
   * Lists the tuples of a subject (what it reaches).
   *
   * @param tenantId the organisation's id
   * @param subject the subject, {@code type:id} or a userset {@code type:id#relation}
   * @return the tuples
   */
  public ListResult<RelationTuple> listOfSubject(String tenantId, String subject) {
    return listOfSubject(tenantId, subject, RequestOptions.none());
  }

  /**
   * Lists the tuples of a subject, with per-call settings. See {@link #listOfSubject(String,
   * String)}.
   *
   * @param tenantId the organisation's id
   * @param subject the subject
   * @param options per-call settings
   * @return the tuples
   */
  public ListResult<RelationTuple> listOfSubject(
      String tenantId, String subject, RequestOptions options) {
    ApiRequest request =
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/relation-tuples", tenantId))
            .query("subject", Objects.requireNonNull(subject, "subject"));
    return http.list(request, RelationTuple.class, options);
  }

  /**
   * Deletes a relationship.
   *
   * @param tenantId the organisation's id
   * @param tupleId the tuple's id
   */
  public void delete(String tenantId, String tupleId) {
    delete(tenantId, tupleId, RequestOptions.none());
  }

  /**
   * Deletes a relationship. With per-call settings: see {@link #delete(String, String)}.
   *
   * @param tenantId the organisation's id
   * @param tupleId the tuple's id
   * @param options per-call settings
   */
  public void delete(String tenantId, String tupleId, RequestOptions options) {
    http.executeVoid(
        ApiRequest.of(
            "DELETE",
            Paths.of("/v1/tenants/{tenantId}/relation-tuples/{tupleId}", tenantId, tupleId)),
        options);
  }

  /**
   * Whether the user holds the relation on the object, following usersets.
   *
   * @param tenantId the organisation's id
   * @param params the object, relation and user
   * @return whether the relation holds
   */
  public boolean check(String tenantId, RelationCheckParams params) {
    return check(tenantId, params, RequestOptions.none());
  }

  /**
   * Whether the user holds the relation, with per-call settings. See {@link #check(String,
   * RelationCheckParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the object, relation and user
   * @param options per-call settings
   * @return whether the relation holds
   */
  public boolean check(String tenantId, RelationCheckParams params, RequestOptions options) {
    return decide(tenantId, params, false, options).allowed();
  }

  /**
   * {@code check} with its reasons: the deciding model and the path of tuples.
   *
   * @param tenantId the organisation's id
   * @param params the object, relation and user
   * @return the decision
   */
  public RelationDecision explain(String tenantId, RelationCheckParams params) {
    return explain(tenantId, params, RequestOptions.none());
  }

  /**
   * {@code check} with its reasons, with per-call settings. See {@link #explain(String,
   * RelationCheckParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the object, relation and user
   * @param options per-call settings
   * @return the decision
   */
  public RelationDecision explain(
      String tenantId, RelationCheckParams params, RequestOptions options) {
    return decide(tenantId, params, true, options);
  }

  private RelationDecision decide(
      String tenantId, RelationCheckParams params, boolean explain, RequestOptions options) {
    ApiRequest request =
        ApiRequest.post(Paths.of("/v1/tenants/{tenantId}/relation-tuples/check", tenantId))
            .query("explain", explain ? "true" : null)
            .json(params)
            .retrySafe();
    return http.execute(request, options, RelationDecision.class);
  }

  /**
   * The graph of identities reachable from an object and relation.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the relation graph
   */
  public RelationGraph graph(String tenantId, RelationGraphParams params) {
    return graph(tenantId, params, RequestOptions.none());
  }

  /**
   * The graph of identities reachable from an object and relation. With per-call settings: see
   * {@link #graph(String, RelationGraphParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the relation graph
   */
  public RelationGraph graph(String tenantId, RelationGraphParams params, RequestOptions options) {
    return http.execute(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/relation-tuples/graph", tenantId))
            .query(params),
        options,
        RelationGraph.class);
  }

  /**
   * The object types the tuples name, with counts.
   *
   * @param tenantId the organisation's id
   * @return the list
   */
  public ListResult<RelationObjectType> listTypes(String tenantId) {
    return listTypes(tenantId, RequestOptions.none());
  }

  /**
   * The object types the tuples name, with counts. With per-call settings: see {@link
   * #listTypes(String)}.
   *
   * @param tenantId the organisation's id
   * @param options per-call settings
   * @return the list
   */
  public ListResult<RelationObjectType> listTypes(String tenantId, RequestOptions options) {
    return http.list(
        ApiRequest.get(Paths.of("/v1/tenants/{tenantId}/relation-tuples/types", tenantId)),
        RelationObjectType.class,
        options);
  }

  /**
   * The objects of one type the tuples name, a page at a time.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @return the first page; iterate it for every item, page by page
   */
  public Page<RelationObject> listObjects(String tenantId, RelationObjectListParams params) {
    return listObjects(tenantId, params, RequestOptions.none());
  }

  /**
   * The objects of one type the tuples name, a page at a time. With per-call settings: see {@link
   * #listObjects(String, RelationObjectListParams)}.
   *
   * @param tenantId the organisation's id
   * @param params the parameters
   * @param options per-call settings
   * @return the first page; iterate it for every item, page by page
   */
  public Page<RelationObject> listObjects(
      String tenantId, RelationObjectListParams params, RequestOptions options) {
    return http.page(
        Paths.of("/v1/tenants/{tenantId}/relation-tuples/objects", tenantId),
        params,
        RelationObject.class,
        options);
  }
}
