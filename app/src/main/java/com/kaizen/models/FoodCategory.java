package com.kaizen.models;

public class FoodCategory {
    private String created_date;
    private String id;
    private String mainCategoryTitle;
    private String status;
    private String categoryurl;

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainCategoryTitle() {
        return mainCategoryTitle;
    }

    public void setMainCategoryTitle(String mainCategoryTitle) {
        this.mainCategoryTitle = mainCategoryTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryurl() {
        return categoryurl;
    }

    public void setCategoryurl(String categoryurl) {
        this.categoryurl = categoryurl;
    }

    @Override
    public String toString() {
        return "FoodCategory{" +
                "created_date='" + created_date + '\'' +
                ", id='" + id + '\'' +
                ", mainCategoryTitle='" + mainCategoryTitle + '\'' +
                ", status='" + status + '\'' +
                ", categoryurl='" + categoryurl + '\'' +
                '}';
    }
}
