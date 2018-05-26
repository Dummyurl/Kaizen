package com.kaizen.models;

public class ListChildCategory {
    private String status;
    private String productImage;
    private String brandName;
    private String productID;
    private String id;
    private String aliasName;
    private String enquiry;
    private String description;
    private String CatId;
    private String mainCatId;
    private String CreatedDate;
    private String subCatId;
    private String bannerImg;
    private String coverImage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(String enquiry) {
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "ListChildCategory{" +
                "status='" + status + '\'' +
                ", productImage='" + productImage + '\'' +
                ", brandName='" + brandName + '\'' +
                ", productID='" + productID + '\'' +
                ", id='" + id + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", enquiry='" + enquiry + '\'' +
                ", description='" + description + '\'' +
                ", CatId='" + CatId + '\'' +
                ", mainCatId='" + mainCatId + '\'' +
                ", CreatedDate='" + CreatedDate + '\'' +
                ", subCatId='" + subCatId + '\'' +
                ", bannerImg='" + bannerImg + '\'' +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }
}
