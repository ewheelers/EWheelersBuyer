package com.ewheelers.eWheelersBuyers.ModelClass;

public class PromoCodesModel {
    String promoCode;
    String description;
    String startdate;
    String enddate;
    String imagelur;
    String title;
    String minimumval;
    String maxval;

    public String getMinimumval() {
        return minimumval;
    }

    public void setMinimumval(String minimumval) {
        this.minimumval = minimumval;
    }

    public String getMaxval() {
        return maxval;
    }

    public void setMaxval(String maxval) {
        this.maxval = maxval;
    }

    public String getImagelur() {
        return imagelur;
    }

    public void setImagelur(String imagelur) {
        this.imagelur = imagelur;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
