package com.ewheelers.eWheelersBuyers.ModelClass;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeCollectionProducts implements Serializable {
    public static final int PRODUCTS=0;
    public static final int BRANDS=1;
    public static final int PREVIEW=2;
    public static final int CATEGORY=3;
    public static final int SHOPS=4;

    private JSONArray jsonArray;

    private int type;
    private String collectionid;
       //products
    private String cartItems;
    private String is_in_any_wishlist;
    private String product_id;
    private String selprod_id;
    private String product_name;
    private String selprod_title;
    private String product_image_updated_on;
    private String special_price_found;
    private String splprice_display_list_price;
    private String splprice_display_dis_val;
    private String splprice_display_dis_type;
    private String theprice;
    private String selprod_price;
    private String selprod_stock;
    private String selprod_condition;
    private String prodcat_id;
    private String prodcat_name;
    private String selprod_sold_count;
    private String in_stock;
    private String selprod_test_drive_enable;
    private String is_sell;
    private String is_rent;
    private String rent_price;
    private String rental_type;
    private String sprodata_rental_stock;
    private String prod_rating;
    private String product_image_url;

    private String brandid;
    private String brandname;
    private String brandimageurl;
    private String colbrandid;
    private String colbranddescription;

    private String title_name;

    private String slideImageurl;

    private String collectionName;

    private String shopid;
    private String shopname;
    private String shopuserid;
    private String countryname;
    private String statename;
    private String rating;
    private String shoplogo;
    private String shopbanner;

    private String prodcategory_id;
    private String prodcategory_name;
    private String prodcategory_description;
    private String prodcategory_imageurl;


    public String getCollectionid() {
        return collectionid;
    }

    public void setCollectionid(String collectionid) {
        this.collectionid = collectionid;
    }

    public String getColbrandid() {
        return colbrandid;
    }

    public void setColbrandid(String colbrandid) {
        this.colbrandid = colbrandid;
    }

    public String getColbranddescription() {
        return colbranddescription;
    }

    public void setColbranddescription(String colbranddescription) {
        this.colbranddescription = colbranddescription;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getProdcategory_id() {
        return prodcategory_id;
    }

    public void setProdcategory_id(String prodcategory_id) {
        this.prodcategory_id = prodcategory_id;
    }

    public String getProdcategory_name() {
        return prodcategory_name;
    }

    public void setProdcategory_name(String prodcategory_name) {
        this.prodcategory_name = prodcategory_name;
    }

    public String getProdcategory_description() {
        return prodcategory_description;
    }

    public void setProdcategory_description(String prodcategory_description) {
        this.prodcategory_description = prodcategory_description;
    }

    public String getProdcategory_imageurl() {
        return prodcategory_imageurl;
    }

    public void setProdcategory_imageurl(String prodcategory_imageurl) {
        this.prodcategory_imageurl = prodcategory_imageurl;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopuserid() {
        return shopuserid;
    }

    public void setShopuserid(String shopuserid) {
        this.shopuserid = shopuserid;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getShoplogo() {
        return shoplogo;
    }

    public void setShoplogo(String shoplogo) {
        this.shoplogo = shoplogo;
    }

    public String getShopbanner() {
        return shopbanner;
    }

    public void setShopbanner(String shopbanner) {
        this.shopbanner = shopbanner;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public HomeCollectionProducts() {
    }

    public String getCartItems() {
        return cartItems;
    }

    public void setCartItems(String cartItems) {
        this.cartItems = cartItems;
    }

    public String getSlideImageurl() {
        return slideImageurl;
    }

    public void setSlideImageurl(String slideImageurl) {
        this.slideImageurl = slideImageurl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getBrandimageurl() {
        return brandimageurl;
    }

    public void setBrandimageurl(String brandimageurl) {
        this.brandimageurl = brandimageurl;
    }

    public String getIs_in_any_wishlist() {
        return is_in_any_wishlist;
    }

    public void setIs_in_any_wishlist(String is_in_any_wishlist) {
        this.is_in_any_wishlist = is_in_any_wishlist;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSelprod_id() {
        return selprod_id;
    }

    public void setSelprod_id(String selprod_id) {
        this.selprod_id = selprod_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSelprod_title() {
        return selprod_title;
    }

    public void setSelprod_title(String selprod_title) {
        this.selprod_title = selprod_title;
    }

    public String getProduct_image_updated_on() {
        return product_image_updated_on;
    }

    public void setProduct_image_updated_on(String product_image_updated_on) {
        this.product_image_updated_on = product_image_updated_on;
    }

    public String getSpecial_price_found() {
        return special_price_found;
    }

    public void setSpecial_price_found(String special_price_found) {
        this.special_price_found = special_price_found;
    }

    public String getSplprice_display_list_price() {
        return splprice_display_list_price;
    }

    public void setSplprice_display_list_price(String splprice_display_list_price) {
        this.splprice_display_list_price = splprice_display_list_price;
    }

    public String getSplprice_display_dis_val() {
        return splprice_display_dis_val;
    }

    public void setSplprice_display_dis_val(String splprice_display_dis_val) {
        this.splprice_display_dis_val = splprice_display_dis_val;
    }

    public String getSplprice_display_dis_type() {
        return splprice_display_dis_type;
    }

    public void setSplprice_display_dis_type(String splprice_display_dis_type) {
        this.splprice_display_dis_type = splprice_display_dis_type;
    }

    public String getTheprice() {
        return theprice;
    }

    public void setTheprice(String theprice) {
        this.theprice = theprice;
    }

    public String getSelprod_price() {
        return selprod_price;
    }

    public void setSelprod_price(String selprod_price) {
        this.selprod_price = selprod_price;
    }

    public String getSelprod_stock() {
        return selprod_stock;
    }

    public void setSelprod_stock(String selprod_stock) {
        this.selprod_stock = selprod_stock;
    }

    public String getSelprod_condition() {
        return selprod_condition;
    }

    public void setSelprod_condition(String selprod_condition) {
        this.selprod_condition = selprod_condition;
    }

    public String getProdcat_id() {
        return prodcat_id;
    }

    public void setProdcat_id(String prodcat_id) {
        this.prodcat_id = prodcat_id;
    }

    public String getProdcat_name() {
        return prodcat_name;
    }

    public void setProdcat_name(String prodcat_name) {
        this.prodcat_name = prodcat_name;
    }

    public String getSelprod_sold_count() {
        return selprod_sold_count;
    }

    public void setSelprod_sold_count(String selprod_sold_count) {
        this.selprod_sold_count = selprod_sold_count;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }

    public String getSelprod_test_drive_enable() {
        return selprod_test_drive_enable;
    }

    public void setSelprod_test_drive_enable(String selprod_test_drive_enable) {
        this.selprod_test_drive_enable = selprod_test_drive_enable;
    }

    public String getIs_sell() {
        return is_sell;
    }

    public void setIs_sell(String is_sell) {
        this.is_sell = is_sell;
    }

    public String getIs_rent() {
        return is_rent;
    }

    public void setIs_rent(String is_rent) {
        this.is_rent = is_rent;
    }

    public String getRent_price() {
        return rent_price;
    }

    public void setRent_price(String rent_price) {
        this.rent_price = rent_price;
    }

    public String getRental_type() {
        return rental_type;
    }

    public void setRental_type(String rental_type) {
        this.rental_type = rental_type;
    }

    public String getSprodata_rental_stock() {
        return sprodata_rental_stock;
    }

    public void setSprodata_rental_stock(String sprodata_rental_stock) {
        this.sprodata_rental_stock = sprodata_rental_stock;
    }

    public String getProd_rating() {
        return prod_rating;
    }

    public void setProd_rating(String prod_rating) {
        this.prod_rating = prod_rating;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }
}
