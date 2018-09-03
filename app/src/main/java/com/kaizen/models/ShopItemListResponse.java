package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ShopItemListResponse {
    private String responce;

    private List<ShopItem> data;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<ShopItem> getShopitemlist() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setShopitemlist(List<ShopItem> shopitemlist) {
        this.data = shopitemlist;
    }
}
