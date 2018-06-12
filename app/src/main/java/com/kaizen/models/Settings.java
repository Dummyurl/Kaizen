package com.kaizen.models;

public class Settings {
    private String created_date;
    private String id;
    private String status;
    private String name;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "created_date='" + created_date + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
