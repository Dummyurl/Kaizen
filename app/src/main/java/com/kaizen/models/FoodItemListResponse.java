package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class FoodItemListResponse {
    private String responce;

    private List<FoodItem> fooditemlist;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<FoodItem> getFooditemlist() {

        if (fooditemlist == null) {
            fooditemlist = new ArrayList<>();
        }

        return fooditemlist;
    }

    public void setFooditemlist(List<FoodItem> fooditemlist) {
        this.fooditemlist = fooditemlist;
    }
}
