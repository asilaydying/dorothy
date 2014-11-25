package com.example.asilaydying.dorothy;

/**
 * Created by supergep on 2014.11.19..
 */
public class KosarItem {
    private String productId;
    private String RecordId;
    private String ProductName;
    private String ProductCnt;
    private String ProductAmountSum;
    private String PictureLink;
    private String IsAdditionalFood;
    private String MasterFoodRecordId;
    private String AdditionalCategory;
    private String FileSize;
    private String ProductDetail;

    public String getProductDetail() {
        return ProductDetail;
    }

    public void setProductDetail(String productDetail) {
        ProductDetail = productDetail;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getAdditionalCategory() {
        return AdditionalCategory;
    }

    public void setAdditionalCategory(String additionalCategory) {
        AdditionalCategory = additionalCategory;
    }

    private String NeedAdditional;
    private String Index;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getNeedAdditional() {
        return NeedAdditional;
    }

    public void setNeedAdditional(String needAdditional) {
        NeedAdditional = needAdditional;
    }

    public String getMasterFoodRecordId() {
        return MasterFoodRecordId;
    }

    public void setMasterFoodRecordId(String masterFoodRecordId) {
        MasterFoodRecordId = masterFoodRecordId;
    }

    public String getIsAdditionalFood() {
        return IsAdditionalFood;
    }

    public void setIsAdditionalFood(String isAdditionalFood) {
        IsAdditionalFood = isAdditionalFood;
    }

    public String getPictureLink() {
        return PictureLink;
    }

    public void setPictureLink(String pictureLink) {
        PictureLink = pictureLink;
    }

    public String getProductAmountSum() {
        return ProductAmountSum;
    }

    public void setProductAmountSum(String productAmountSum) {
        ProductAmountSum = productAmountSum;
    }

    public String getProductCnt() {
        return ProductCnt;
    }

    public void setProductCnt(String productCnt) {
        ProductCnt = productCnt;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    private String CategoryId;
}
