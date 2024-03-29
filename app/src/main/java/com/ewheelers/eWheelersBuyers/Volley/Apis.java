package com.ewheelers.eWheelersBuyers.Volley;

public class Apis {
    //live url
    public static String base = "https://www.ewheelers.in/app-api/2.0/";
    //stage url
    /*public static String base = "https://www.beta.ewheelers.in/app-api/2.0/";*/
    public static String login = base + "guest-user/login";
    public static String logout = base+ "guest-user/logout";
    public static String register = base + "guest-user/register";
    public static String forgotpassword = base + "guest-user/forgot-password";
    public static String home = base + "home";
    public static String productdetails = base + "products/view/";
    public static String addtocart = base + "cart/add";
    public static String cartListing = base + "cart/listing";
    public static String removecartItems = base + "cart/remove";
    public static String updatecartitems = base + "cart/update";
    public static String applypromocode = base + "cart/apply-promo-code";
    public static String removepromocode = base + "cart/remove-promo-code";
    public static String shippingsummary = base + "checkout/shipping-summary";
    public static String testdrive = base + "test-drive/setup";
    public static String collectionView = base+"collections/search";
    public static String profileInfo = base+"account/profile-info";
    public static String UpdateProfileInfo = base+"account/update-profile-info";
    public static String getCountries = base+"home/countries";
    public static String getStates = base+"home/states/";
    public static String getcities = base+"home/cities/";
    public static String validcoupons = base + "checkout/get-valid-discount-coupons";
    public static String offerslisting = base+"buyer/search-offers";
    public static String setupAddress = base+"addresses/set-up-address";
    public static String getaddresses = base+"account/search-addresses";
    public static String deleteaddress = base+"addresses/delete-record";
    public static String setdefaultaddress = base+"addresses/set-default";
    public static String setupaddressselection = base+"checkout/set-up-address-selection";
    public static String sellerslist = base+"products/sellers/";
    public static String notificationslist = base+"account/notifications";
    public static String markalertread = base+"account/mark-notification-read/";
    public static String uploadprofilepic = base+"account/upload-profile-image";
    public static String paymentsummary = base+"checkout/payment-summary";
    public static String filteredproducts = base+"products/get-filtered-products";
    public static String setupshippingmethod = base+"checkout/set-up-shipping-method";
    public static String setuppushnotification = base+"guest-user/set-user-push-notification-token";
    public static String userewardpoints = base+"checkout/use-reward-points";
    public static String removerewardpoints = base+"checkout/remove-reward-points";
    public static String confirmorder = base+"checkout/confirm-order";
    public static String orderslist = base+"buyer/order-search-listing";
    public static String vieworderdetails = base+"buyer/view-order/";
    public static String getrewardpoints = base+"buyer/reward-points-search";
    public static String getcreditpoints = base+"account/credit-search";
    public static String getallshops = base+"shops/search";
    public static String viewshopbyid = base+"shops/view/";
    public static String alltestdriverequests = base+"test-drive/search-requests";
    public static String getdriveinfo = base+"test-drive/request-info/";
    public static String cancelrequest = base+"test-drive/cancel";
    public static String changerequeststatus = base+"/test-drive/change-request-status";
    public static String walletpayment = base+"wallet-pay/charge/";
    public static String searchbytags = base+"products/search-producttags-autocomplete";
    public static String addcompare = base+"/compare-products/add";
    public static String autocompletesearch = base+"/compare-products/autocomplete";
    public static String compareindex = base+"/compare-products/index";
    public static String filters = base+"products/filters";
    public static String chipfilters = base+"products/get-seller-products-attributes/";
    public static String chargeStations = base+"addresses/search-stations";
    public static String spproductslist = base+"products/get-seller-products-by-criteria";
    public static String sellerlistprofile = base+"home/get-seller-list-by-profile-attributes";
    public static String sellerprofileattributes = base+"home/get-seller-profile-attributes";
    public static String validateOtp = base+"guest-user/validate-otp";
    public static String resendotp = base+"guest-user/resend-otp/";
    public static String passwordUpdate = base+"account/update-password";
    public static String sendloginOtp = base+"guest-user/send-login-otp";


}
