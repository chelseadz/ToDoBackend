package com.encora.ToDoBack.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;

public class ToDo {
    private String id;

    @NotEmpty
    private String text;
    private LocalDateTime dueDate;
    private boolean done;
    private LocalDateTime doneDate;
    private Priority priority;
    private LocalDateTime creationDate = LocalDateTime.now();

    // Constructor
    public ToDo(String text, Priority priority, LocalDateTime dueDate) {
        if (text == null || text.length() > 120 || text.length() <=0) {
            throw new IllegalArgumentException("Text must be non-null and have a maximum length of 120 characters.");
        }
        this.text = text;
        this.priority = priority;
        this.id = UUID.randomUUID().toString();
        this.dueDate = dueDate;
    }

    public ToDo(String text, Priority priority) {
        if (text == null || text.length() >= 120 || text.length() <=0) {
            throw new IllegalArgumentException("Text must be non-null and have a maximum length of 120 characters.");
        }
        this.text = text;
        this.priority = priority;
        this.id = UUID.randomUUID().toString();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null || text.length() > 120) {
            throw new IllegalArgumentException("Text must be non-null and have a maximum length of 120 characters.");
        }
        this.text = text;
    }

    public void setCreationDate(LocalDateTime date) {
        this.creationDate = date;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime forceDueDate() {
        if (this.dueDate != null){return this.dueDate;}
        else{ 
            LocalDateTime dummyDate = LocalDateTime.of(2900,01,01,01,01,01);
            return dummyDate;
        }
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Enum for Priority
    public enum Priority {
        HIGH, MEDIUM, LOW
    }
}

