package com.ewheelers.eWheelersBuyers.ModelClass;

public class AllebikesModelClass {
    public static final int ALLBIKES=0;
    public static final int ALLCATEGORIES=1;
    public static final int ALLBRANDS=2;
    public static final int ALLSHOPS=3;
    public static final int ALLDEALERS=4;


    int typeLayout;
    String networkImage;
    String productName;
    String price;
    String productid;

    String testdriveenable;
    String issell;
    String isrent;
    String instock;

    String shopphone;
    String shoppincode;
    String autocomplete;


    public String getAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }

    public AllebikesModelClass(String networkImage, String productName, String price) {
        this.networkImage = networkImage;
        this.productName = productName;
        this.price = price;
    }

    public AllebikesModelClass() {
    }

    public String getShopphone() {
        return shopphone;
    }

    public void setShopphone(String shopphone) {
        this.shopphone = shopphone;
    }

    public String getShoppincode() {
        return shoppincode;
    }

    public void setShoppincode(String shoppincode) {
        this.shoppincode = shoppincode;
    }

    public String getInstock() {
        return instock;
    }

    public void setInstock(String instock) {
        this.instock = instock;
    }

    public int getTypeLayout() {
        return typeLayout;
    }

    public void setTypeLayout(int typeLayout) {
        this.typeLayout = typeLayout;
    }

    public String getTestdriveenable() {
        return testdriveenable;
    }


    public void setTestdriveenable(String testdriveenable) {
        this.testdriveenable = testdriveenable;
    }

    public String getIssell() {
        return issell;
    }

    public void setIssell(String issell) {
        this.issell = issell;
    }

    public String getIsrent() {
        return isrent;
    }

    public void setIsrent(String isrent) {
        this.isrent = isrent;
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
