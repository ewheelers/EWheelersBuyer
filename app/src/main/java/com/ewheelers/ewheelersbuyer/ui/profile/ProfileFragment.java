package com.ewheelers.ewheelersbuyer.ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelersbuyer.MainActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.ewheelers.ewheelersbuyer.UpdateProfileActivity;
import com.ewheelers.ewheelersbuyer.Utilities.VolleyMultipartRequest;
import com.ewheelers.ewheelersbuyer.Volley.Apis;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.ewheelers.ewheelersbuyer.Volley.Apis.uploadprofilepic;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;
    private TextView user_Name, user_Email, Name, Mobile, Email, Address,dob, PrivacyPolicy, Edit, Bank, Reffer, Faq;
    String tokenValue;
    CollapsingToolbarLayout collapsingToolbar;

    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    ImageView imageView;
    Button buttonUpload;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        tokenValue = new SessionStorage().getStrings(getActivity(), SessionStorage.tokenvalue);

        collapsingToolbar = (CollapsingToolbarLayout) root.findViewById(R.id.toolbar_layout);
        user_Name = root.findViewById(R.id.userName);
        user_Email = root.findViewById(R.id.userEmail);
        Name = root.findViewById(R.id.name);
        Mobile = root.findViewById(R.id.mobileNo);
        Email = root.findViewById(R.id.emailId);
        Address = root.findViewById(R.id.address);
        PrivacyPolicy = root.findViewById(R.id.privacyPolicytext);
        Edit = root.findViewById(R.id.editProfile);
        Bank = root.findViewById(R.id.gotobank);
        Reffer = root.findViewById(R.id.referral);
        Faq = root.findViewById(R.id.faq);
        //imageView = root.findViewById(R.id.profileImage);
        dob = root.findViewById(R.id.dobDate);
        //buttonUpload = root.findViewById(R.id.upload_img);
        PrivacyPolicy.setOnClickListener(this);
        Edit.setOnClickListener(this);
        //buttonUpload.setOnClickListener(this);
        getProfile();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // textView.setText(s);
            }
        });
    }

    private void getProfile() {
        String url_link = Apis.profileInfo;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");
                    JSONObject jsonObjectProfileInfo = jsonObjectData.getJSONObject("personalInfo");
                    String user_name = jsonObjectProfileInfo.getString("user_name");
                    String user_phone = jsonObjectProfileInfo.getString("user_phone");
                    String user_profile_info = jsonObjectProfileInfo.getString("user_profile_info");
                    String user_regdate = jsonObjectProfileInfo.getString("user_regdate");
                    String credential_username = jsonObjectProfileInfo.getString("credential_username");
                    String credential_email = jsonObjectProfileInfo.getString("credential_email");
                    String user_dob = jsonObjectProfileInfo.getString("user_dob");
                    String user_address1 = jsonObjectProfileInfo.getString("user_address1");
                    String user_address2 = jsonObjectProfileInfo.getString("user_address2");
                    String user_city = jsonObjectProfileInfo.getString("user_city");
                    String user_referral_code = jsonObjectProfileInfo.getString("user_referral_code");
                    String user_order_tracking_url = jsonObjectProfileInfo.getString("user_order_tracking_url");
                    String user_img_updated_on = jsonObjectProfileInfo.getString("user_img_updated_on");
                    String country_name = jsonObjectProfileInfo.getString("country_name");
                    String state_name = jsonObjectProfileInfo.getString("state_name");
                    String userImage = jsonObjectProfileInfo.getString("userImage");

                   // private TextView PrivacyPolicy, Edit, Bank, Faq;
                    collapsingToolbar.setTitle(user_name);
                    user_Name.setText(user_name);
                    user_Email.setText(credential_email);
                    Name.setText(credential_username);
                    Email.setText(credential_email);
                    Address.setText(user_address1+" - "+user_address2+" - "+user_city+" - "+state_name+" - "+country_name);
                    Reffer.setText("Refer to friends\n Referral code: "+user_referral_code);
                    if(user_phone.isEmpty()){
                        Mobile.setText("xx-xxxxxxxxxx(update with edit option)");
                    }else {
                        Mobile.setText(user_phone);
                    }

                    if(user_dob.isEmpty()){
                        dob.setText("0000-00-00 (update with edit option)");
                    }else {
                        dob.setText(user_dob);
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
                params.put("X-TOKEN", tokenValue);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.privacyPolicytext:
                String url = "http://www.ewheelers.in/cms/view/3/1";
                showPrivacyPolicies(url);
                break;
            case R.id.editProfile:
                Intent i = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(i);
                break;
           /* case R.id.upload_img:
                if ((ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    showFileChooser();
                }
                break;*/
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    uploadBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        getActivity(),"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        String url = Apis.uploadprofilepic;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("user_profile_image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                params.put("img_data",null);

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    private void showPrivacyPolicies(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        WebView webView = new WebView(getActivity());
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        dialog.setView(webView);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
