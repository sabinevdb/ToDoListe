package com.example.todolist;

import java.util.Date;

public class AufgabeData {

    public AufgabeData(long id, String title, String description, Date timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String newName) {
        this.title = newName;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String newName) {
        this.description = newName;
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long newName) {
        this.id = newName;
    }

    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date newName) {
        this.timestamp = newName;
    }
}
