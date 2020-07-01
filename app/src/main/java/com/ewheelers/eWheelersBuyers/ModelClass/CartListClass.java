package com.ewheelers.eWheelersBuyers.ModelClass;

import java.util.ArrayList;

public class CartListClass {
    public static final int ADDONLAYOUT=0;
    public static final int MAINPRODUCT=1;

    private String productid;
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
    public static final int addon=0;
    public static final int rent=1;

    int type;

    private ArrayList<String> options;

    public String rentalprice;
    public String rentalsecurity;
    public String total;
    public String rentStartdate;
    public String rentEnddate;

    private String sellerAddress;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getRentalprice() {
        return rentalprice;
    }

    public void setRentalprice(String rentalprice) {
        this.rentalprice = rentalprice;
    }

    public String getRentalsecurity() {
        return rentalsecurity;
    }

    public void setRentalsecurity(String rentalsecurity) {
        this.rentalsecurity = rentalsecurity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRentStartdate() {
        return rentStartdate;
    }

    public void setRentStartdate(String rentStartdate) {
        this.rentStartdate = rentStartdate;
    }

    public String getRentEnddate() {
        return rentEnddate;
    }

    public void setRentEnddate(String rentEnddate) {
        this.rentEnddate = rentEnddate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
