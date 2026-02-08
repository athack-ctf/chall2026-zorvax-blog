package com.athackctf.chall2025.jestersblog;

import jakarta.persistence.*;

@Entity
@Table(name = "BLOGPOSTS")
public class BlogPosts {

    @Id
    private int id;
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
