package com.ewheelers.ewheelersbuyer.ModelClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comparemodelclass implements Serializable {
    public static final int head=0;
    public static final int sublist=1;
    int typeofLay;
    String heading;
    String values;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public int getTypeofLay() {
        return typeofLay;
    }

    public void setTypeofLay(int typeofLay) {
        this.typeofLay = typeofLay;
    }
}
