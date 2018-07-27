package com.kaizen.models;

import java.util.List;

public class ShopCategoryResponse {
    private List<ShopCategory> shopmaincategory;
    private String responce;

    public List<ShopCategory> getShopmaincategory() {
        return shopmaincategory;
    }

    public void setShopmaincategory(List<ShopCategory> shopmaincategory) {
        this.shopmaincategory = shopmaincategory;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }
}
