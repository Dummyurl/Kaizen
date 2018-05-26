package com.kaizen.models;

public class Subcategory {
    private String id;
    private String status;
    private String subCatAliasName;
    private String subCategoryTitle;
    private String maincat_id;
    private String createdDate;

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
        return "Subcategory{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", subCatAliasName='" + subCatAliasName + '\'' +
                ", subCategoryTitle='" + subCategoryTitle + '\'' +
                ", maincat_id='" + maincat_id + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
