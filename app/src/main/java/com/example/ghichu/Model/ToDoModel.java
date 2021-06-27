package com.example.ghichu.Model;

public class ToDoModel  {
    private int id, status;
    private String task;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "ToDoModel{" +
                "status=" + status +
                ", task='" + task + '\'' +
                '}';
    }

    public void setTask(String task) {
        this.task = task;
    }

}
