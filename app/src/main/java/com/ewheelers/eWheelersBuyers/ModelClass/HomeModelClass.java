package com.ewheelers.eWheelersBuyers.ModelClass;

import android.view.View;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

public class HomeModelClass implements Serializable {
    public static final int PRODUCTLAYOUT=1;
    public static final int BANNERLAYOUT=0;

    int typeoflayout;
    String collectionId;
    String primaryrecord;
    String headcatTitle;
    String collectiontype;
    String totproducts;
    List<HomeCollectionProducts> homeCollectionProducts;
    List<HomeCollectionProducts> homeCollectionProductsBrands;
    List<HomeCollectionProducts> homeCollectionProductsShops;
    List<HomeCollectionProducts> homeCollectionProductsCategories;
    List<HomeCollectionProducts> homeCollectionProductsSlidersList;

    String bannerurl;
    String bannerimageurl;

    List<HomeModelClass> homeModelClassesBanners;
    List<HomeModelClass> homeModelClassesBannersBottom;
    List<HomeModelClass> homeModelClassesBannersTop;

    public List<HomeModelClass> getHomeModelClassesBannersTop() {
        return homeModelClassesBannersTop;
    }

    public void setHomeModelClassesBannersTop(List<HomeModelClass> homeModelClassesBannersTop) {
        this.homeModelClassesBannersTop = homeModelClassesBannersTop;
    }

    public List<HomeModelClass> getHomeModelClassesBannersBottom() {
        return homeModelClassesBannersBottom;
    }

    public void setHomeModelClassesBannersBottom(List<HomeModelClass> homeModelClassesBannersBottom) {
        this.homeModelClassesBannersBottom = homeModelClassesBannersBottom;
    }

    public List<HomeModelClass> getHomeModelClassesBanners() {
        return homeModelClassesBanners;
    }

    public void setHomeModelClassesBanners(List<HomeModelClass> homeModelClassesBanners) {
        this.homeModelClassesBanners = homeModelClassesBanners;
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl;
    }

    public String getBannerimageurl() {
        return bannerimageurl;
    }

    public void setBannerimageurl(String bannerimageurl) {
        this.bannerimageurl = bannerimageurl;
    }

    public String getTotproducts() {
        return totproducts;
    }

    public void setTotproducts(String totproducts) {
        this.totproducts = totproducts;
    }

    public List<HomeCollectionProducts> getHomeCollectionProductsSlidersList() {
        return homeCollectionProductsSlidersList;
    }

    public void setHomeCollectionProductsSlidersList(List<HomeCollectionProducts> homeCollectionProductsSlidersList) {
        this.homeCollectionProductsSlidersList = homeCollectionProductsSlidersList;
    }

    public List<HomeCollectionProducts> getHomeCollectionProductsCategories() {
        return homeCollectionProductsCategories;
    }

    public void setHomeCollectionProductsCategories(List<HomeCollectionProducts> homeCollectionProductsCategories) {
        this.homeCollectionProductsCategories = homeCollectionProductsCategories;
    }

    public List<HomeCollectionProducts> getHomeCollectionProductsShops() {
        return homeCollectionProductsShops;
    }

    public void setHomeCollectionProductsShops(List<HomeCollectionProducts> homeCollectionProductsShops) {
        this.homeCollectionProductsShops = homeCollectionProductsShops;
    }

    public List<HomeCollectionProducts> getHomeCollectionProductsBrands() {
        return homeCollectionProductsBrands;
    }

    public void setHomeCollectionProductsBrands(List<HomeCollectionProducts> homeCollectionProductsBrands) {
        this.homeCollectionProductsBrands = homeCollectionProductsBrands;
    }



    public String getPrimaryrecord() {
        return primaryrecord;
    }

    public void setPrimaryrecord(String primaryrecord) {
        this.primaryrecord = primaryrecord;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public int getTypeoflayout() {
        return typeoflayout;
    }

    public void setTypeoflayout(int typeoflayout) {
        this.typeoflayout = typeoflayout;
    }

    public String getHeadcatTitle() {
        return headcatTitle;
    }

    public void setHeadcatTitle(String headcatTitle) {
        this.headcatTitle = headcatTitle;
    }

    public String getCollectiontype() {
        return collectiontype;
    }

    public void setCollectiontype(String collectiontype) {
        this.collectiontype = collectiontype;
    }

    public List<HomeCollectionProducts> getHomeCollectionProducts() {
        return homeCollectionProducts;
    }

    public void setHomeCollectionProducts(List<HomeCollectionProducts> homeCollectionProducts) {
        this.homeCollectionProducts = homeCollectionProducts;
    }
}
