package com.crazyostudio.studentcircle.model;

public class LinksModels {
    public String title;
    public String links;

    public LinksModels() {
    }

    public LinksModels(String title, String links) {
        this.title = title;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }
}
