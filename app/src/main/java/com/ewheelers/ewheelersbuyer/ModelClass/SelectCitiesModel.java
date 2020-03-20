package com.ewheelers.ewheelersbuyer.ModelClass;

public class SelectCitiesModel {
    String cityname;
    int cityicon;

    public SelectCitiesModel(String cityname, int cityicon) {
        this.cityname = cityname;
        this.cityicon = cityicon;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public int getCityicon() {
        return cityicon;
    }

    public void setCityicon(int cityicon) {
        this.cityicon = cityicon;
    }
}
