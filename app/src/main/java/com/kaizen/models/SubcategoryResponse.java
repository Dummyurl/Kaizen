package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryResponse {
    private List<Subcategory> subcategory;

    public List<Subcategory> getSubcategory() {
        if (subcategory == null) {
            subcategory = new ArrayList<>();
        }

        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String toString() {
        return "SubcategoryResponse{" +
                "subcategory=" + subcategory +
                '}';
    }
}
