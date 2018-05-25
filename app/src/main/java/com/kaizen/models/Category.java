package com.kaizen.models;

public class Category {
    private String id;
    private String mainCategoryTitle;
    private String status;
    private String category_image;
    private String createDate;

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

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", mainCategoryTitle='" + mainCategoryTitle + '\'' +
                ", status='" + status + '\'' +
                ", category_image='" + category_image + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
