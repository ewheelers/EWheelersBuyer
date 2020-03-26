package com.ewheelers.ewheelersbuyer.Volley;

public class Apis {
    public static String base = "https://www.ewheelers.in//app-api/2.0/";
    public static String login = base + "guest-user/login"; // done
    public static String logout = base+ "guest-user/logout";
    public static String register = base + "guest-user/register"; //done
    public static String forgotpassword = base + "guest-user/forgot-password"; //done
    public static String home = base + "home"; // done
    public static String productdetails = base + "products/view/";
    public static String addtocart = base + "cart/add";
    public static String cartListing = base + "cart/listing";
    public static String removecartItems = base + "cart/remove";
    public static String updatecartitems = base + "cart/update";
    public static String applypromocode = base + "cart/apply-promo-code";
    public static String removepromocode = base + "cart/remove-promo-code";
    public static String shippingsummary = base + "checkout/shipping-summary";
    public static String testdrive = base + "test-drive/setup"; //done
    public static String bookproduct = base + "cart/add";
    public static String collectionView = base+"collections/search";
    public static String profileInfo = base+"account/profile-info";
    public static String UpdateProfileInfo = base+"account/update-profile-info";
    public static String getCountries = base+"home/countries";
    public static String getStates = base+"home/states/";
    public static String getcities = base+"home/cities/";
    public static String offerslisting = base+"buyer/search-offers";
    public static String setupAddress = base+"addresses/set-up-address";
    public static String getaddresses = base+"account/search-addresses";
    public static String deleteaddress = base+"addresses/delete-record";
    public static String setupaddressselection = base+"checkout/set-up-address-selection"; //billing_address_id,shipping_address_id

    public static String setuppushnotification = base+"guest-user/set-user-push-notification-token";

}
