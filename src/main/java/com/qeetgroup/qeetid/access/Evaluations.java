package com.qeetgroup.qeetid.access;

import java.util.List;

/**
 * Several AuthZEN decisions, in the request's order.
 *
 * @param evaluations the evaluations
 */
public record Evaluations(List<Evaluation> evaluations) {}
