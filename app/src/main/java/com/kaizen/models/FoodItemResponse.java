package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class FoodItemResponse {
    private String responce;
    private List<FoodItem> fooditemslist;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<FoodItem> getFooditemslist() {

        if(fooditemslist==null)
        {
            fooditemslist=new ArrayList<>();
        }

        return fooditemslist;
    }

    public void setFooditemslist(List<FoodItem> fooditemslist) {
        this.fooditemslist = fooditemslist;
    }
}
