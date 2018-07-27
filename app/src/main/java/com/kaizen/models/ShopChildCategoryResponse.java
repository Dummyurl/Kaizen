package com.kaizen.models;

import java.util.List;

public class ShopChildCategoryResponse {
    private List<ShopChildCategory> shopchildcategorylist;

    private String responce;

    public List<ShopChildCategory> getShopchildcategorylist ()
    {
        return shopchildcategorylist;
    }

    public void setShopchildcategorylist (List<ShopChildCategory> shopchildcategorylist)
    {
        this.shopchildcategorylist = shopchildcategorylist;
    }

    public String getResponce ()
    {
        return responce;
    }

    public void setResponce (String responce)
    {
        this.responce = responce;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [shopchildcategorylist = "+shopchildcategorylist+", responce = "+responce+"]";
    }
}
