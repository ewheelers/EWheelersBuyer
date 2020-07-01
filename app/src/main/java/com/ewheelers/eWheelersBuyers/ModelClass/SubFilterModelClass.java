package com.ewheelers.eWheelersBuyers.ModelClass;

public class SubFilterModelClass extends FilterListClass {
    String subname;
    String subcatid;

    String optionvalue_name;
    String optionvalue_id;
    String optionvalue_color_code;

    String brandid;
    String brandname;

    public static final int CATGORY=0;
    public static final int COLORS=1;

    int typeOf;

    @Override
    public String getBrandid() {
        return brandid;
    }

    @Override
    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    @Override
    public String getBrandname() {
        return brandname;
    }

    @Override
    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public int getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(int typeOf) {
        this.typeOf = typeOf;
    }

    public String getOptionvalue_name() {
        return optionvalue_name;
    }

    public void setOptionvalue_name(String optionvalue_name) {
        this.optionvalue_name = optionvalue_name;
    }

    public String getOptionvalue_id() {
        return optionvalue_id;
    }

    public void setOptionvalue_id(String optionvalue_id) {
        this.optionvalue_id = optionvalue_id;
    }

    public String getOptionvalue_color_code() {
        return optionvalue_color_code;
    }

    public void setOptionvalue_color_code(String optionvalue_color_code) {
        this.optionvalue_color_code = optionvalue_color_code;
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
}
