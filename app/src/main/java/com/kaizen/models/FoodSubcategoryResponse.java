package com.kaizen.models;

import java.util.List;

public class FoodSubcategoryResponse {
    private String responce;
    private List<FoodSubcategory> foodsubcategorylist;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<FoodSubcategory> getFoodsubcategorylist() {
        return foodsubcategorylist;
    }

    public void setFoodsubcategorylist(List<FoodSubcategory> foodsubcategorylist) {
        this.foodsubcategorylist = foodsubcategorylist;
    }

    @Override
    public String toString() {
        return "FoodSubcategoryResponse{" +
                "responce='" + responce + '\'' +
                ", foodsubcategorylist=" + foodsubcategorylist +
                '}';
    }
}
