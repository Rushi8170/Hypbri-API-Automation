package com.framework.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserResponse — maps the response from GET /users/{id} on reqres.in
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private int id;
    private String name;
    private String job;
    private String email;
    private String token;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    // ── Getters / Setters ────────────────────────────────────────

    public int getId()            { return id; }
    public void setId(int id)     { this.id = id; }

    public String getName()       { return name; }
    public void setName(String n) { this.name = n; }

    public String getJob()        { return job; }
    public void setJob(String j)  { this.job = j; }

    public String getEmail()      { return email; }
    public void setEmail(String e){ this.email = e; }

    public String getToken()      { return token; }
    public void setToken(String t){ this.token = t; }

    public String getCreatedAt()  { return createdAt; }
    public String getUpdatedAt()  { return updatedAt; }

    @Override
    public String toString() {
        return "UserResponse{id=" + id + ", name='" + name + "', email='" + email + "', token='" + token + "'}";
    }
}
