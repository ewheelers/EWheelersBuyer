package com.ewheelers.eWheelersBuyers.ModelClass;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

public class HomeModelClass implements Serializable {
    public static final int PRODUCTLAYOUT=0;
    public static final int CATEGORYLAYOUT=1;
    public static final int BRANDLAYOUT=2;
    public static final int SHOPSLAYOUT=3;

    int typeoflayout;
    String headcatTitle;
    String collectiontype;
    List<HomeCollectionProducts> homeCollectionProducts;
    List<JSONArray> jsonArray;
    JSONArray jsonArraylist;

    public JSONArray getJsonArraylist() {
        return jsonArraylist;
    }

    public void setJsonArraylist(JSONArray jsonArraylist) {
        this.jsonArraylist = jsonArraylist;
    }

    public List<JSONArray> getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(List<JSONArray> jsonArray) {
        this.jsonArray = jsonArray;
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
