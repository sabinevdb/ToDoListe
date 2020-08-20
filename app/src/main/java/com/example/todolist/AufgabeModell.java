package com.example.todolist;

import java.util.Date;

public class AufgabeModell {


    public AufgabeModell(long id, String title, String description, Date timestamp, String filename) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.filename = filename;
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

    private String filename;

    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }
}
