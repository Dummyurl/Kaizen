package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponse {
    private List<Category> category;

    public List<Category> getCategory() {
        if (category == null) {
            category = new ArrayList<>();
        }

        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "category=" + category +
                '}';
    }
}
