package com.ewheelers.ewheelersbuyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.Adapters.CouponsAdapter;
import com.ewheelers.ewheelersbuyer.Adapters.PaymentGatewayAdapter;
import com.ewheelers.ewheelersbuyer.Interface.ItemClickListener;
import com.ewheelers.ewheelersbuyer.ModelClass.PaymentGatewaysModel;
import com.ewheelers.ewheelersbuyer.ModelClass.PriceDetailsClass;
import com.ewheelers.ewheelersbuyer.ModelClass.PromoCodesModel;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.ewheelers.ewheelersbuyer.Volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ewheelers.ewheelersbuyer.Dialogs.ShowAlerts.showfailedDialog;


public class StartPaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    String TAG = "mainActivity", uniqueID, amount, phone = "7793960952",
            prodname = "Speed Glyd Testing", firstname = "ravi", email = "ceo@ewheelers.in",
            merchantId = "6837052", merchantkey = "OsTqEn5B", salt = "wOSX4BGnle";

    TextView textViewNetpay;
    String tokenvalue;
    String productkey;
    ProgressBar progressBar;
    List<PriceDetailsClass> priceDetailsClasses = new ArrayList<>();
    List<PaymentGatewaysModel> paymentGatewaysModelsList = new ArrayList<>();
    PaymentGatewayAdapter paymentGatewayAdapter;
    RecyclerView recyclerView;
    TextView rewardText,descriptiontxt;
    CheckBox walletbal;
    EditText userReward;
    Button applyReward,confirmOrder,removeReward,walletPayment;
    String pmid,orderid,ordertype;
    LinearLayout linearLayoutGateway,linearLayoutWallet;
    TextView tobepay,walamt,remainbal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_payment);
        tokenvalue = new SessionStorage().getStrings(this, SessionStorage.tokenvalue);
        progressBar = findViewById(R.id.progress);
        linearLayoutGateway = findViewById(R.id.gateway_layout);
        linearLayoutWallet = findViewById(R.id.wallet_layout);
        tobepay = findViewById(R.id.tobepaid);
        walamt = findViewById(R.id.walletamt);
        walletPayment = findViewById(R.id.wallet_confirm);
        remainbal = findViewById(R.id.remaining_bal);
        textViewNetpay = findViewById(R.id.net_amount);
        recyclerView = findViewById(R.id.payment_methods_list);
        rewardText = findViewById(R.id.reward_txt);
        walletbal = findViewById(R.id.wallet_check);
        userReward = findViewById(R.id.usereward);
        applyReward = findViewById(R.id.applyreward);
        confirmOrder = findViewById(R.id.confirm_order);
        descriptiontxt = findViewById(R.id.descript);
        removeReward = findViewById(R.id.removereward);
        removeReward.setOnClickListener(this);
        confirmOrder.setOnClickListener(this);
        applyReward.setOnClickListener(this);
        walletPayment.setOnClickListener(this);
        uniqueID = UUID.randomUUID().toString();
        //amount = getIntent().getStringExtra("netamount");

       // Toast.makeText(this, "amount"+amount, Toast.LENGTH_SHORT).show();
        walletbal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    paymentsummary("1");
                }else {
                    paymentsummary("0");
                }
            }
        });

        shippingSummary();

        //paymentsummary();

        //startpay();
    }

    JSONObject jsonObjectnew = new JSONObject();
    JSONObject subjsonobject = new JSONObject();

    public void shippingSummary() {
        progressBar.setVisibility(View.VISIBLE);
        String url_link = Apis.shippingsummary;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObjectData.getJSONArray("products");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectProducts = jsonArray.getJSONObject(i);
                            productkey = jsonObjectProducts.getString("productKey");
                           // Log.d("prodkey", productkey);
                            subjsonobject.put("shipping_type", 3);
                            subjsonobject.put("shipping_locations", 0);
                            jsonObjectnew.put(productkey, subjsonobject);
                        }

                        //Log.e("jsonnew",jsonObjectnew.toString());
                        setupshippingmethod(jsonObjectnew.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    public void setupshippingmethod(String resultjson) {
        String tokenvalue = new SessionStorage().getStrings(StartPaymentActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.setupshippingmethod;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("setupsipment",response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                JSONObject jsonObjectDat = jsonObject.getJSONObject("data");
                                String walletbalance = jsonObjectDat.getString("userWalletBalance");

                                JSONObject jsonObjectrewarddetails = jsonObjectDat.getJSONObject("rewardPointsDetail");
                                String canbeused = jsonObjectrewarddetails.getString("canBeUsed");
                                String balance = jsonObjectrewarddetails.getString("balance");
                                String convertedvalue = jsonObjectrewarddetails.getString("convertedValue");

                                rewardText.setText(canbeused+" of "+balance+" Reward Points Available For This Order (" +convertedvalue + ")");

                                paymentsummary("0");
                                /*Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();*/
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
               // Log.e("resjson", resultjson.toString());
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("data", resultjson);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void paymentsummary(String walletpaymentId) {
        paymentGatewaysModelsList.clear();
        String tokenvalue = new SessionStorage().getStrings(StartPaymentActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.paymentsummary;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                progressBar.setVisibility(View.GONE);
                                applyReward.setEnabled(true);
                                applyReward.setBackground(getResources().getDrawable(R.drawable.button_bg));
                                applyReward.setTextColor(Color.WHITE);
                                confirmOrder.setTextColor(Color.WHITE);
                                confirmOrder.setBackground(getResources().getDrawable(R.drawable.button_bg));
                                confirmOrder.setEnabled(true);

                                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                                orderid = dataJsonObject.getString("orderId");
                                ordertype = dataJsonObject.getString("orderType");

                                String displaywalletbalnce = dataJsonObject.getString("displayUserWalletBalance");
                                String applycredits = dataJsonObject.getString("walletCharged");
                                String remainingbal = dataJsonObject.getString("displayRemainingWalletBalance");
                                amount = dataJsonObject.getString("orderNetAmount");
                                if(walletpaymentId.equals("0")) {
                                    walletbal.setVisibility(View.VISIBLE);
                                    walletbal.setText("Apply Wallet Credits : " + displaywalletbalnce);
                                    linearLayoutGateway.setVisibility(View.VISIBLE);
                                    linearLayoutWallet.setVisibility(View.GONE);
                                }else {
                                    walletbal.setVisibility(View.VISIBLE);
                                    walletbal.setText("Applied Wallet Credits : " + applycredits);
                                    linearLayoutGateway.setVisibility(View.GONE);
                                    linearLayoutWallet.setVisibility(View.VISIBLE);
                                    walamt.setText("Amount in your wallet\n\n"+displaywalletbalnce);
                                    tobepay.setText("Payment to be Made\n\n"+applycredits);
                                    remainbal.setText("Remaining Wallet Balance "+remainingbal);
                                }

                                Log.i("orderid",orderid);

                                JSONObject jsonObjectNet = dataJsonObject.getJSONObject("netPayable");
                                String netpay = jsonObjectNet.getString("key");
                                String value = jsonObjectNet.getString("value");
                                textViewNetpay.setText(netpay+" : " + value);

                                JSONArray jsonArraypaymentmethods = dataJsonObject.getJSONArray("paymentMethods");
                                for (int i = 0; i < jsonArraypaymentmethods.length(); i++) {
                                    JSONObject jsonObjectmethods = jsonArraypaymentmethods.getJSONObject(i);
                                    String pmethid_id = jsonObjectmethods.getString("pmethod_id");
                                    String pmeth_name = jsonObjectmethods.getString("pmethod_name");
                                    String pmeth_code = jsonObjectmethods.getString("pmethod_code");
                                    String pmeth_desc = jsonObjectmethods.getString("pmethod_description");
                                    String pmeth_img = jsonObjectmethods.getString("image");
                                    PaymentGatewaysModel paymentGatewaysModel = new PaymentGatewaysModel();
                                    paymentGatewaysModel.setPmethidid(pmethid_id);
                                    paymentGatewaysModel.setPmethodname(pmeth_name);
                                    paymentGatewaysModel.setPmethodcode(pmeth_code);
                                    paymentGatewaysModel.setPmethoddescription(pmeth_desc);
                                    paymentGatewaysModel.setImage(pmeth_img);
                                    paymentGatewaysModelsList.add(paymentGatewaysModel);
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StartPaymentActivity.this, RecyclerView.VERTICAL,false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                paymentGatewayAdapter = new PaymentGatewayAdapter(StartPaymentActivity.this, paymentGatewaysModelsList, new ItemClickListener() {
                                    @Override
                                    public void description(String pid,String code, String description) {
                                        descriptiontxt.setText(code+"\n\n"+description);
                                        pmid = pid;

                                    }
                                });
                                recyclerView.setAdapter(paymentGatewayAdapter);
                               /* Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();*/
                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data3 = new HashMap<String, String>();
                if(walletpaymentId.equals("1")) {
                    data3.put("payFromWallet", "1");
                }else {
                    data3.put("payFromWallet", "0");
                }
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    public void startpay() {
        builder.setAmount(amount)                          // Payment amount
                .setTxnId(uniqueID)                     // Transaction ID
                .setPhone(phone)                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail(email)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);
        try {
            paymentParam = builder.build();
            calculateServerSideHashAndInitiatePayment1(paymentParam);
        } catch (Exception e) {
            Log.e(TAG, " error s " + e.toString());
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
        stringBuilder.append(salt);

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);
        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this, -1, true);
        //PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this, R.style.AppTheme_default, false);

        return paymentParam;
    }

    public static String hashCal(String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// PayUMoneySdk: Success -- payuResponse{"id":225642,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"223013","transaction_fee":"20.00","amount":"20.00","cardCategory":"domestic","discount":"0.00","addedon":"2018-12-31 09:09:43","productinfo":"a2z shop","firstname":"kamal","email":"kamal.bunkar07@gmail.com","phone":"9144040888","hash":"b22172fcc0ab6dbc0a52925ebbd0297cca6793328a8dd1e61ef510b9545d9c851600fdbdc985960f803412c49e4faa56968b3e70c67fe62eaed7cecacdfdb5b3","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","payment_source":"payu","PG_TYPE":"AXISPG","bank_ref_no":"562178","ibibo_code":"VISA","error_code":"E000","Error_Message":"No Error","name_on_card":"payu","card_no":"401200XXXXXX1112","is_seamless":1,"surl":"https://www.payumoney.com/sandbox/payment/postBackParam.do","furl":"https://www.payumoney.com/sandbox/payment/postBackParam.do"}
        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                } else {
                    //Failure Transaction
                }
                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();
                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e(TAG, "tran " + payuResponse + "---" + merchantResponse);
            } /* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wallet_confirm:
                payfromWallet(v,orderid);
                break;
            case R.id.confirm_order:
                if (pmid != null) {
                    if(pmid.equals("4")){
                        confirmOrderMethod(ordertype,orderid,pmid,v);
                    }
                    if(pmid.equals("7")) {
                        startpay();
                    }
                }else {
                    Toast.makeText(this, "Select any Payment Method", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.applyreward:
                if(userReward.getText().toString().isEmpty()){
                    userReward.setError("Enter reward points");
                }else {
                    useRewardpoints();
                }
                break;
            case R.id.removereward:
                removeRewardMethod();
                break;
        }
    }

    private void payfromWallet(View v,String orderid) {
        String url_link = Apis.walletpayment+orderid;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("walpay",response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {

                        ViewGroup viewGroup = v.findViewById(android.R.id.content);
                        final View dialogView = LayoutInflater.from(StartPaymentActivity.this).inflate(R.layout.success_layout, viewGroup, false);
                        TextView textView = dialogView.findViewById(R.id.message);
                        Button button = dialogView.findViewById(R.id.buttonOk);
                        textView.setText("Payment Done successfully");
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartPaymentActivity.this);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(StartPaymentActivity.this,MyOrdersActivity.class);
                                startActivity(i);
                                finish();
                                alertDialog.dismiss();
                            }
                        });

                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), msg, Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }else {
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), msg, Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void removeRewardMethod() {
        String url_link = Apis.removerewardpoints;
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        removeReward.setVisibility(View.GONE);
                        applyReward.setVisibility(View.VISIBLE);
                        userReward.setVisibility(View.VISIBLE);
                        paymentsummary("0");
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), msg, Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }else {
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), msg, Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void confirmOrderMethod(String ordertype, String orderid, String pmid,View v) {
        String tokenvalue = new SessionStorage().getStrings(StartPaymentActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.confirmorder;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(StartPaymentActivity.this).inflate(R.layout.success_layout, viewGroup, false);
                                TextView textView = dialogView.findViewById(R.id.message);
                                Button button = dialogView.findViewById(R.id.buttonOk);
                                textView.setText(message);
                                AlertDialog.Builder builder = new AlertDialog.Builder(StartPaymentActivity.this);
                                builder.setView(dialogView);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        Intent i = new Intent(StartPaymentActivity.this,MyOrdersActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            } else {
                                showfailedDialog(StartPaymentActivity.this, v, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("order_type", ordertype);
                data3.put("order_id", orderid);
                data3.put("pmethod_id", pmid);
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void useRewardpoints() {
        String tokenvalue = new SessionStorage().getStrings(StartPaymentActivity.this, SessionStorage.tokenvalue);
        String Login_url = Apis.userewardpoints;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                removeReward.setVisibility(View.VISIBLE);
                                applyReward.setVisibility(View.GONE);
                                userReward.setVisibility(View.GONE);
                                paymentsummary("0");
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            } else {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.linear_layout), message, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("orderId", orderid);
                data3.put("redeem_rewards", userReward.getText().toString());
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }
}
