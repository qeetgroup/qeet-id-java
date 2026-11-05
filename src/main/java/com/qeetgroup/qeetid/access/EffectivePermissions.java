package com.qeetgroup.qeetid.access;

import java.util.List;

/**
 * Every permission a user holds in an organisation, and the grants behind them.
 *
 * @param permissions the permissions
 * @param grants the grants
 */
public record EffectivePermissions(List<String> permissions, List<GrantStep> grants) {}
