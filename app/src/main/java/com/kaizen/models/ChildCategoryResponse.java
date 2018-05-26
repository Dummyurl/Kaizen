package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ChildCategoryResponse {
    private List<ChildCategory> childcategory;

    public List<ChildCategory> getChildcategory() {
        if (childcategory == null) {
            childcategory = new ArrayList<>();
        }

        return childcategory;
    }

    public void setChildcategory(List<ChildCategory> childcategory) {
        this.childcategory = childcategory;
    }

    @Override
    public String toString() {
        return "ChildCategoryResponse{" +
                "childcategory=" + childcategory +
                '}';
    }
}
