package com.ewheelers.ewheelersbuyer.ModelClass;

public class AllebikesModelClass {
    String networkImage;
    String productName;
    String price;
    String productid;

    public AllebikesModelClass(String networkImage, String productName, String price) {
        this.networkImage = networkImage;
        this.productName = productName;
        this.price = price;
    }

    public AllebikesModelClass() {
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getNetworkImage() {
        return networkImage;
    }

    public void setNetworkImage(String networkImage) {
        this.networkImage = networkImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
