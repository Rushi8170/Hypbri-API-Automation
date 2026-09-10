package com.framework.api.endpoints;

/**
 * APIEndpoints — centralised list of all API endpoint paths.
 * Base URL is configured in config-{env}.properties → api.base.url
 */
public final class APIEndpoints {

    private APIEndpoints() {}

    // ── Users (reqres.in) ────────────────────────────────────────
    public static final String USERS          = "/users";
    public static final String USER_BY_ID     = "/users/{id}";
    public static final String USERS_LIST     = "/users?page={page}";

    // ── Auth ─────────────────────────────────────────────────────
    public static final String REGISTER       = "/register";
    public static final String LOGIN          = "/login";

    // ── Resources ────────────────────────────────────────────────
    public static final String RESOURCES      = "/unknown";
    public static final String RESOURCE_BY_ID = "/unknown/{id}";
}
