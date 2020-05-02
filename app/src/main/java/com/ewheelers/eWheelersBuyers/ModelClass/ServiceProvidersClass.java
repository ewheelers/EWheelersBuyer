package com.ewheelers.eWheelersBuyers.ModelClass;

import java.io.Serializable;

public class ServiceProvidersClass implements Serializable {
    private String serviceProviderIs;
    private String serviceprovider_name;
    private String serviceprovider_shopname;
    private String serviceprovider_phone_number;
    private String serviceprovider_alternate_number;
    private String serviceprovider_emailid;
    private String serviceprovider_address;
    private String serviceprovider_latitude;
    private String serviceprovider_longitude;
    private String distance;
    private String city;

    private double currentlatitude;
    private double currentlongitude;

    public static final int CHARGELAY=0;
    public static final int OTHERLAY=1;
    public static final int PARKING=2;

    private int typeofLayout;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistance() {
        return distance;
    }


    public int getTypeofLayout() {
        return typeofLayout;
    }

    public void setTypeofLayout(int typeofLayout) {
        this.typeofLayout = typeofLayout;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getCurrentlatitude() {
        return currentlatitude;
    }

    public void setCurrentlatitude(double currentlatitude) {
        this.currentlatitude = currentlatitude;
    }

    public double getCurrentlongitude() {
        return currentlongitude;
    }

    public void setCurrentlongitude(double currentlongitude) {
        this.currentlongitude = currentlongitude;
    }

    public String getServiceProviderIs() {
        return serviceProviderIs;
    }

    public void setServiceProviderIs(String serviceProviderIs) {
        this.serviceProviderIs = serviceProviderIs;
    }

    public String getServiceprovider_name() {
        return serviceprovider_name;
    }

    public void setServiceprovider_name(String serviceprovider_name) {
        this.serviceprovider_name = serviceprovider_name;
    }

    public String getServiceprovider_shopname() {
        return serviceprovider_shopname;
    }

    public void setServiceprovider_shopname(String serviceprovider_shopname) {
        this.serviceprovider_shopname = serviceprovider_shopname;
    }

    public String getServiceprovider_phone_number() {
        return serviceprovider_phone_number;
    }

    public void setServiceprovider_phone_number(String serviceprovider_phone_number) {
        this.serviceprovider_phone_number = serviceprovider_phone_number;
    }

    public String getServiceprovider_alternate_number() {
        return serviceprovider_alternate_number;
    }

    public void setServiceprovider_alternate_number(String serviceprovider_alternate_number) {
        this.serviceprovider_alternate_number = serviceprovider_alternate_number;
    }

    public String getServiceprovider_emailid() {
        return serviceprovider_emailid;
    }

    public void setServiceprovider_emailid(String serviceprovider_emailid) {
        this.serviceprovider_emailid = serviceprovider_emailid;
    }

    public String getServiceprovider_address() {
        return serviceprovider_address;
    }

    public void setServiceprovider_address(String serviceprovider_address) {
        this.serviceprovider_address = serviceprovider_address;
    }

    public String getServiceprovider_latitude() {
        return serviceprovider_latitude;
    }

    public void setServiceprovider_latitude(String serviceprovider_latitude) {
        this.serviceprovider_latitude = serviceprovider_latitude;
    }

    public String getServiceprovider_longitude() {
        return serviceprovider_longitude;
    }

    public void setServiceprovider_longitude(String serviceprovider_longitude) {
        this.serviceprovider_longitude = serviceprovider_longitude;
    }
}
