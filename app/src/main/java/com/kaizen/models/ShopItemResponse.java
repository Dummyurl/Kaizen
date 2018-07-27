package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ShopItemResponse {
    private String responce;
    private List<ShopItem> shopitemslist;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<ShopItem> getShopitemslist() {

        if(shopitemslist==null)
        {
            shopitemslist=new ArrayList<>();
        }

        return shopitemslist;
    }

    public void setShopitemslist(List<ShopItem> shopitemslist) {
        this.shopitemslist = shopitemslist;
    }
}
