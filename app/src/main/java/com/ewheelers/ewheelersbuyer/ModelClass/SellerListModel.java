package com.ewheelers.ewheelersbuyer.ModelClass;

public class SellerListModel {
    public static final int SELLLIST=0;
    public static final int SHOPSLIST=1;

    int typeoflayout;
    String sellersname;
    String sellersaddress;
    String sellersphoneno;
    String sellerslatitude;
    String Sellerslongitude;
    String SellerPrice;
    String SellerCod;
    String selproductid;

    public int getTypeoflayout() {
        return typeoflayout;
    }

    public void setTypeoflayout(int typeoflayout) {
        this.typeoflayout = typeoflayout;
    }

    public String getSellerPrice() {
        return SellerPrice;
    }

    public void setSellerPrice(String sellerPrice) {
        SellerPrice = sellerPrice;
    }

    public String getSellerCod() {
        return SellerCod;
    }

    public void setSellerCod(String sellerCod) {
        SellerCod = sellerCod;
    }

    public String getSelproductid() {
        return selproductid;
    }

    public void setSelproductid(String selproductid) {
        this.selproductid = selproductid;
    }

    public String getSellersname() {
        return sellersname;
    }

    public void setSellersname(String sellersname) {
        this.sellersname = sellersname;
    }

    public String getSellersaddress() {
        return sellersaddress;
    }

    public void setSellersaddress(String sellersaddress) {
        this.sellersaddress = sellersaddress;
    }

    public String getSellersphoneno() {
        return sellersphoneno;
    }

    public void setSellersphoneno(String sellersphoneno) {
        this.sellersphoneno = sellersphoneno;
    }

    public String getSellerslatitude() {
        return sellerslatitude;
    }

    public void setSellerslatitude(String sellerslatitude) {
        this.sellerslatitude = sellerslatitude;
    }

    public String getSellerslongitude() {
        return Sellerslongitude;
    }

    public void setSellerslongitude(String sellerslongitude) {
        Sellerslongitude = sellerslongitude;
    }
}
