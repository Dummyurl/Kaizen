package com.kaizen.models;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class ShopItem extends SugarRecord {

    public ShopItem() {
    }

    private List<CustomFields> custom_fields;

    private String created_date;

    private String status;

    private String shop_discount_price;

    private String brandName;

    private String aliasName;

    private String shop_price;

    private String description;

    private String CatId;

    private String mainCatId;

    private String subCatId;

    private String bannerImg;

    private int quantity;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public List<CustomFields> getCustom_fields() {

        if (custom_fields == null) {
            custom_fields = new ArrayList<>();
        }

        return custom_fields;
    }

    public void setCustom_fields(List<CustomFields> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShop_discount_price() {
        return shop_discount_price;
    }

    public void setShop_discount_price(String shop_discount_price) {
        this.shop_discount_price = shop_discount_price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatId() {
        return CatId;
    }

    public void setCatId(String catId) {
        CatId = catId;
    }

    public String getMainCatId() {
        return mainCatId;
    }

    public void setMainCatId(String mainCatId) {
        this.mainCatId = mainCatId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }
}
