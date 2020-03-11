package com.ewheelers.ewheelersbuyer.ModelClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartListClass {
    private String imageurl;
    private String productName;
    private String productOption;
    private String productPrice;
    private String product_qty;
    private String brandname;
    private String minimuborderqty;
    private String shopname;
    private String keyvalue;
    private String optionvalue;

    private ArrayList<String> options;

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getKeyvalue() {
        return keyvalue;
    }

    public void setKeyvalue(String keyvalue) {
        this.keyvalue = keyvalue;
    }

    public String getOptionvalue() {
        return optionvalue;
    }

    public void setOptionvalue(String optionvalue) {
        this.optionvalue = optionvalue;
    }

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
