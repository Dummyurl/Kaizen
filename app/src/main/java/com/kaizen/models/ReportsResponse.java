package com.kaizen.models;

import java.util.List;

public class ReportsResponse {
    private List<Reports> reports;

    private String responce;

    public List<Reports> getReports() {
        return reports;
    }

    public void setReports(List<Reports> reports) {
        this.reports = reports;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }
}
