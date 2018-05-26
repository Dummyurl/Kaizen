package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ListChildCategoryResponse {
    List<ListChildCategory> listchildcategory;

    public List<ListChildCategory> getListchildcategory() {

        if (listchildcategory == null) {
            listchildcategory = new ArrayList<>();
        }

        return listchildcategory;
    }

    public void setListchildcategory(List<ListChildCategory> listchildcategory) {
        this.listchildcategory = listchildcategory;
    }

    @Override
    public String toString() {
        return "ListChildCategoryResponse{" +
                "listchildcategory=" + listchildcategory +
                '}';
    }
}
