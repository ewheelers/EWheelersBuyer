package com.ewheelers.ewheelersbuyer.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetails implements Parcelable {
    public static final int OPTIONS=0;
    public static final int PREVIEWIMAGES=1;
    public static final int OFFERS=2;
    public static final int BUYWITH=3;
    public static final int SIMILARPRODUCTS=4;
    public static final int BOTTOMEDBUTTONS = 5;
    public static final int DEALERSLISt = 6;

    private int typeoflayout;
    private String selproductid;

    private String buttonText;
    private int buttonBackground;

    private String productimg_url;

    private String productDescription;
    private String productSpecifications;

    //for options & spinner data
    private String optionName;
    private String optionid;
    private String optioniscolor;
   // private ArrayList<String> optionslist;

    private ArrayList<OptionValues> optionValuesArrayList;


    //offers list
    private String offertitle;
    private String offercaption;
    private int imageicon;

    //Buy together
    private String buywithimageurl;
    private String buywithproductname;
    private String buywithproductprice;
    private String buywithorderqty;
    private String butwithselectedProductId;
    private String buywithQuantity;

    //similar images
    private String similarproductid;
    private String similarimageurl;
    private String similarproductname;
    private String similarproductprice;

    //dealers
    private String shopname;
    private String shopcity;
    private String shopstate;
    private String shopcountry;


    public ProductDetails(int typeoflayout, String offertitle, String offercaption, int imageicon) {
        this.offertitle = offertitle;
        this.offercaption = offercaption;
        this.imageicon = imageicon;
        this.typeoflayout = typeoflayout;
    }

    public ProductDetails(int typeoflayout,String offertitle, int imageicon) {
        this.buttonText = offertitle;
        this.buttonBackground = imageicon;

        this.offertitle = offertitle;
        this.imageicon = imageicon;
        this.typeoflayout = typeoflayout;
    }

    public ProductDetails(int typeoflayout,String offertitle, int imageicon,String selproductid) {
        this.buttonText = offertitle;
        this.buttonBackground = imageicon;

        this.offertitle = offertitle;
        this.imageicon = imageicon;
        this.typeoflayout = typeoflayout;
        this.selproductid = selproductid;
    }

    public ProductDetails() {

    }

    public String getSelproductid() {
        return selproductid;
    }

    public void setSelproductid(String selproductid) {
        this.selproductid = selproductid;
    }

    private ProductDetails(Parcel in) {
        //selproductid = in.readString();
        typeoflayout = in.readInt();
        productimg_url = in.readString();
        productDescription = in.readString();
        productSpecifications = in.readString();
        optionName = in.readString();
        optionValuesArrayList = in.createTypedArrayList(OptionValues.CREATOR);
        offertitle = in.readString();
        offercaption = in.readString();
        imageicon = in.readInt();
        buywithimageurl = in.readString();
        buywithproductname = in.readString();
        buywithproductprice = in.readString();
        buywithorderqty = in.readString();
        similarproductid = in.readString();
        similarimageurl = in.readString();
        similarproductname = in.readString();
        similarproductprice = in.readString();
        //optionValuesArrayList = in.readArrayList(OptionValues.class.getClassLoader());

    }


    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };


    public ArrayList<OptionValues> getOptionValuesArrayList() {
        return optionValuesArrayList;
    }

    public void setOptionValuesArrayList(ArrayList<OptionValues> optionValuesArrayList) {
        this.optionValuesArrayList = optionValuesArrayList;
    }

    public String getButwithselectedProductId() {
        return butwithselectedProductId;
    }

    public void setButwithselectedProductId(String butwithselectedProductId) {
        this.butwithselectedProductId = butwithselectedProductId;
    }

    public String getBuywithQuantity() {
        return buywithQuantity;
    }

    public void setBuywithQuantity(String buywithQuantity) {
        this.buywithQuantity = buywithQuantity;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopcity() {
        return shopcity;
    }

    public void setShopcity(String shopcity) {
        this.shopcity = shopcity;
    }

    public String getShopstate() {
        return shopstate;
    }

    public void setShopstate(String shopstate) {
        this.shopstate = shopstate;
    }

    public String getShopcountry() {
        return shopcountry;
    }

    public void setShopcountry(String shopcountry) {
        this.shopcountry = shopcountry;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getButtonBackground() {
        return buttonBackground;
    }

    public void setButtonBackground(int buttonBackground) {
        this.buttonBackground = buttonBackground;
    }

    public String getSimilarproductid() {
        return similarproductid;
    }

    public void setSimilarproductid(String similarproductid) {
        this.similarproductid = similarproductid;
    }

    public String getSimilarimageurl() {
        return similarimageurl;
    }

    public void setSimilarimageurl(String similarimageurl) {
        this.similarimageurl = similarimageurl;
    }

    public String getSimilarproductname() {
        return similarproductname;
    }

    public void setSimilarproductname(String similarproductname) {
        this.similarproductname = similarproductname;
    }

    public String getSimilarproductprice() {
        return similarproductprice;
    }

    public void setSimilarproductprice(String similarproductprice) {
        this.similarproductprice = similarproductprice;
    }

    public String getBuywithimageurl() {
        return buywithimageurl;
    }

    public void setBuywithimageurl(String buywithimageurl) {
        this.buywithimageurl = buywithimageurl;
    }

    public String getBuywithproductname() {
        return buywithproductname;
    }

    public void setBuywithproductname(String buywithproductname) {
        this.buywithproductname = buywithproductname;
    }

    public String getBuywithproductprice() {
        return buywithproductprice;
    }

    public void setBuywithproductprice(String buywithproductprice) {
        this.buywithproductprice = buywithproductprice;
    }

    public String getBuywithorderqty() {
        return buywithorderqty;
    }

    public void setBuywithorderqty(String buywithorderqty) {
        this.buywithorderqty = buywithorderqty;
    }

    public String getOffertitle() {
        return offertitle;
    }

    public void setOffertitle(String offertitle) {
        this.offertitle = offertitle;
    }

    public String getOffercaption() {
        return offercaption;
    }

    public void setOffercaption(String offercaption) {
        this.offercaption = offercaption;
    }

    public int getImageicon() {
        return imageicon;
    }

    public void setImageicon(int imageicon) {
        this.imageicon = imageicon;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductSpecifications() {
        return productSpecifications;
    }

    public void setProductSpecifications(String productSpecifications) {
        this.productSpecifications = productSpecifications;
    }



    public int getTypeoflayout() {
        return typeoflayout;
    }

    public void setTypeoflayout(int typeoflayout) {
        this.typeoflayout = typeoflayout;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getProductimg_url() {
        return productimg_url;
    }

    public void setProductimg_url(String productimg_url) {
        this.productimg_url = productimg_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(optionValuesArrayList);
    }


}

