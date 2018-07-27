package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ShopSubCategoryResponse {
    private String responce;
    private List<ShopSubCategory> shopsubcategorylist;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<ShopSubCategory> getShopsubcategorylist() {

        if (shopsubcategorylist == null) {
            shopsubcategorylist = new ArrayList<>();
        }

        return shopsubcategorylist;
    }

    public void setShopsubcategorylist(List<ShopSubCategory> shopsubcategorylist) {
        this.shopsubcategorylist = shopsubcategorylist;
    }

    @Override
    public String toString() {
        return "ShopSubcategoryResponse{" +
                "responce='" + responce + '\'' +
                ", shopsubcategorylist=" + shopsubcategorylist +
                '}';
    }
}
