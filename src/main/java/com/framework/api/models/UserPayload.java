package com.framework.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * UserPayload — request/response model for User API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPayload {

    private String name;
    private String job;
    private String email;
    private String password;

    // ── Constructors ─────────────────────────────────────────────

    public UserPayload() {}

    public UserPayload(String name, String job) {
        this.name = name;
        this.job  = job;
    }

    public UserPayload(String email, String password, boolean isLogin) {
        this.email    = email;
        this.password = password;
    }

    // ── Getters / Setters ────────────────────────────────────────

    public String getName()     { return name; }
    public void setName(String name) { this.name = name; }

    public String getJob()      { return job; }
    public void setJob(String job)   { this.job = job; }

    public String getEmail()    { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "UserPayload{name='" + name + "', job='" + job + "', email='" + email + "'}";
    }
}
