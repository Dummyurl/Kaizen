package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ListChildCategoryResponse {
    private List<ListChildCategory> reports;

    public List<ListChildCategory> getReports() {
        if (reports == null) {
            reports = new ArrayList<>();
        }

        return reports;
    }

    public void setReports(List<ListChildCategory> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "ChildCategoryResponse{" +
                "reports=" + reports +
                '}';
    }
}
