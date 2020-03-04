package com.ewheelers.ewheelersbuyer.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OptionValues implements Parcelable {
    private String optionValuenames;
    private String optionUrlValue;

    public String getOptionUrlValue() {
        return optionUrlValue;
    }

    public void setOptionUrlValue(String optionUrlValue) {
        this.optionUrlValue = optionUrlValue;
    }

    public String getOptionValuenames() {
        return optionValuenames;
    }

    public void setOptionValuenames(String optionValuenames) {
        this.optionValuenames = optionValuenames;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(optionValuenames);
        dest.writeString(optionUrlValue);
    }
    public static final Parcelable.Creator<OptionValues> CREATOR = new Parcelable.Creator<OptionValues>() {
        public OptionValues createFromParcel(Parcel in) {
            return new OptionValues(in);
        }
        public OptionValues[] newArray(int size) {
            return new OptionValues[size];
        }
    };
    private OptionValues(Parcel in) {

        optionValuenames = in.readString();
        optionUrlValue = in.readString();
    }
    public OptionValues() {
    }
    @NonNull
    public String toString() {
        return optionValuenames;
    }

}
