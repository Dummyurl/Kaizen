package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ShopItemListResponse {
    private String responce;

    private List<ShopItem> shopitemlist;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<ShopItem> getShopitemlist() {

        if (shopitemlist == null) {
            shopitemlist = new ArrayList<>();
        }

        return shopitemlist;
    }

    public void setShopitemlist(List<ShopItem> shopitemlist) {
        this.shopitemlist = shopitemlist;
    }
}
