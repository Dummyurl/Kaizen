package com.kaizen.models;

import java.util.List;

public class FoodCategoryResponse {
    private List<FoodCategory> foodmaincategory;
    private String responce;

    public List<FoodCategory> getFoodmaincategory() {
        return foodmaincategory;
    }

    public void setFoodmaincategory(List<FoodCategory> foodmaincategory) {
        this.foodmaincategory = foodmaincategory;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }
}
