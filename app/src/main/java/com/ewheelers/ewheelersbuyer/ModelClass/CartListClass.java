package com.ewheelers.ewheelersbuyer.ModelClass;

import java.io.Serializable;

public class CartListClass {
    private String imageurl;
    private String productName;
    private String productOption;
    private String productPrice;
    private String product_qty;
    private String brandname;
    private String minimuborderqty;
    private String shopname;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getMinimuborderqty() {
        return minimuborderqty;
    }

    public void setMinimuborderqty(String minimuborderqty) {
        this.minimuborderqty = minimuborderqty;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductOption() {
        return productOption;
    }

    public void setProductOption(String productOption) {
        this.productOption = productOption;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }
}
