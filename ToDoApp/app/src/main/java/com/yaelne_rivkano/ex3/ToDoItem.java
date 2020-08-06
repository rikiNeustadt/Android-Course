package com.yaelne_rivkano.ex3;

public class ToDoItem {
    private String title;
    private String description;
    private Long dateTime;

    public ToDoItem(String title, String description, Long dateTime) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getDateTime() {
        return dateTime;
    }
}
