package com.kaizen.models;

public class ListChildCategory {
    private String created_date;
    private String status;
    private String brand_id;
    private String brandName;
    private String mainImage;
    private String id;
    private String aliasName;
    private String description;
    private String CatId;
    private String mainCatId;
    private String CreatedDate;
    private String subCatId;
    private String bannerImg;
    private boolean enquiry;
    private boolean enableClick;

    public boolean isEnquiry() {
        return enquiry;
    }

    public boolean isEnableClick() {
        return enableClick;
    }

    public void setEnableClick(boolean enableClick) {
        this.enableClick = enableClick;
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

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(boolean enquiry) {
        this.enquiry = enquiry;
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

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
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

    @Override
    public String toString() {
        return "ListChildCategory{" +
                "created_date='" + created_date + '\'' +
                ", status='" + status + '\'' +
                ", brand_id='" + brand_id + '\'' +
                ", brandName='" + brandName + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", id='" + id + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", enquiry='" + enquiry + '\'' +
                ", description='" + description + '\'' +
                ", CatId='" + CatId + '\'' +
                ", mainCatId='" + mainCatId + '\'' +
                ", CreatedDate='" + CreatedDate + '\'' +
                ", subCatId='" + subCatId + '\'' +
                ", bannerImg='" + bannerImg + '\'' +
                '}';
    }
}
