package com.ewheelers.eWheelersBuyers.ModelClass;

import java.util.ArrayList;
import java.util.List;

public class FilterListClass {
    public static final int CATHEAD=0;
    public static final int CATSUB=1;
    String catId;
    String catName;
    String brandid;
    String brandname;

    int type;


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
