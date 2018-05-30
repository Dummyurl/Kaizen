package com.kaizen.models;

import java.util.List;

public class BannerResponse {
    private List<Banner> reports;
    private String responce;

    public List<Banner> getReports() {
        return reports;
    }

    public void setReports(List<Banner> reports) {
        this.reports = reports;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }
}
