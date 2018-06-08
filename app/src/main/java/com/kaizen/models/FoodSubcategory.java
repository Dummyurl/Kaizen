package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class FoodSubcategory {
    private String id;
    private String status;
    private String subCatAliasName;
    private String subCategoryTitle;
    private String maincat_id;
    private String createdDate;
    private String bannerImg;
    private List<Childcat> childcat;

    public List<Childcat> getChildcat() {

        if (childcat == null) {
            childcat = new ArrayList<>();
        }

        return childcat;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public void setChildcat(List<Childcat> childcat) {
        this.childcat = childcat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubCatAliasName() {
        return subCatAliasName;
    }

    public void setSubCatAliasName(String subCatAliasName) {
        this.subCatAliasName = subCatAliasName;
    }

    public String getSubCategoryTitle() {
        return subCategoryTitle;
    }

    public void setSubCategoryTitle(String subCategoryTitle) {
        this.subCategoryTitle = subCategoryTitle;
    }

    public String getMaincat_id() {
        return maincat_id;
    }

    public void setMaincat_id(String maincat_id) {
        this.maincat_id = maincat_id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "FoodSubcategory{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", subCatAliasName='" + subCatAliasName + '\'' +
                ", subCategoryTitle='" + subCategoryTitle + '\'' +
                ", maincat_id='" + maincat_id + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
