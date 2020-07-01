package com.ewheelers.eWheelersBuyers.ModelClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterListClass {
    public static final int CATHEAD=0;
    public static final int CATSUB=1;
    public static final int OPTIONS=2;
    public static final int SPINNER = 3;
    public static final int FILTERS = 4;

    String catId;
    String catName;
    String brandid;
    String brandname;

    int type;

    String subname;
    String subcatid;

    String optionid;
    String optioniscolor;
    String optionname;

    JSONObject jsonObject;

    List<SubFilterModelClass> subFilterModelClasses;

    List<SubFilterModelClass> subFilterOptionsModelClasses;

    public FilterListClass() {
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public FilterListClass(String s) {
        this.catName = s;
    }

    public List<SubFilterModelClass> getSubFilterOptionsModelClasses() {
        return subFilterOptionsModelClasses;
    }

    public void setSubFilterOptionsModelClasses(List<SubFilterModelClass> subFilterOptionsModelClasses) {
        this.subFilterOptionsModelClasses = subFilterOptionsModelClasses;
    }

    public List<SubFilterModelClass> getSubFilterModelClasses() {
        return subFilterModelClasses;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }

    public String getOptioniscolor() {
        return optioniscolor;
    }

    public void setOptioniscolor(String optioniscolor) {
        this.optioniscolor = optioniscolor;
    }

    public String getOptionname() {
        return optionname;
    }

    public void setOptionname(String optionname) {
        this.optionname = optionname;
    }

    public void setSubFilterModelClasses(List<SubFilterModelClass> subFilterModelClasses) {
        this.subFilterModelClasses = subFilterModelClasses;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(String subcatid) {
        this.subcatid = subcatid;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

}
