package com.crazyostudio.studentcircle.model;

// Repository.java
public class Repository {

    private String avatar_url;
    private String default_branch;
    private String id;
    private String name;
    private String description;

    public Repository(String avatar_url, String default_branch, String id, String name, String description) {
        this.avatar_url = avatar_url;
        this.default_branch = default_branch;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getDefault_branch() {
        return default_branch;
    }

    public void setDefault_branch(String default_branch) {
        this.default_branch = default_branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
