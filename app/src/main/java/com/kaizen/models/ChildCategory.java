package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ChildCategory {
    private String subCategoryId;
    private String id;
    private String categoryTitle;
    private String mainCategoryId;
    private String status;
    private String categoryAlias;
    private String createDate;
    private String mainImage;
    private List<Childcat> childcat;

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryAlias() {
        return categoryAlias;
    }

    public void setCategoryAlias(String categoryAlias) {
        this.categoryAlias = categoryAlias;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<Childcat> getChildcat() {
        if (childcat == null) {
            childcat = new ArrayList<>();
        }

        return childcat;
    }

    public void setChildcat(List<Childcat> childcat) {
        this.childcat = childcat;
    }

    @Override
    public String toString() {
        return "ChildCategory{" +
                "subCategoryId='" + subCategoryId + '\'' +
                ", id='" + id + '\'' +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", mainCategoryId='" + mainCategoryId + '\'' +
                ", status='" + status + '\'' +
                ", categoryAlias='" + categoryAlias + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
