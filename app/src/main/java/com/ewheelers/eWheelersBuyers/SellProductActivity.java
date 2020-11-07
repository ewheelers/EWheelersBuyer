package com.ewheelers.eWheelersBuyers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.eWheelersBuyers.Volley.VolleySingleton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.ADD_USER_URL;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_ACTION;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_BRAND;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_CITY;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_ID;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_IMAGE;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_MANUFACTYR;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_MODEL;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_PHONE;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_PINCODE;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_REGYR;
import static com.ewheelers.eWheelersBuyers.Interface.Configuration.KEY_STATE;

public class SellProductActivity extends AppCompatActivity {
    Button take_pic,saveDetails;
    ImageView imageView;
    private static final String TAG = SellProductActivity.class.getSimpleName();
    private int REQUEST_CAMERA = 100;
    private String userChoosenTask;
    EditText phNo,cityName,stateName,pinCode,brand,model,manyr,regYr;
    String userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);
        saveDetails = findViewById(R.id.save_details);
        imageView = findViewById(R.id.update_img);
        take_pic = findViewById(R.id.take_pic);
        phNo = findViewById(R.id.ph_no);
        cityName = findViewById(R.id.city_name);
        stateName = findViewById(R.id.state_name);
        pinCode = findViewById(R.id.pin_code);
        brand = findViewById(R.id.brand);
        model = findViewById(R.id.model);
        manyr = findViewById(R.id.man_yr);
        regYr = findViewById(R.id.reg_yr);
        ImagePickerActivity.clearCache(this);
        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                Dexter.withActivity(SellProductActivity.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataInExcel();
            }
        });
    }

    private void postDataInExcel() {
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest strRequest = new StringRequest(Request.Method.POST, ADD_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("resp",response);
                        Toast.makeText(SellProductActivity.this,response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                String phone = phNo.getText().toString();
                String city = cityName.getText().toString();
                String state = stateName.getText().toString();
                String pincode = pinCode.getText().toString();
                String brandIs = brand.getText().toString();
                String modelIs = model.getText().toString();
                String man_yr = manyr.getText().toString();
                String reg_yr = regYr.getText().toString();
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put(KEY_ID,"123456");
                data3.put(KEY_PHONE,phone);
                data3.put(KEY_IMAGE,userImage);
                data3.put(KEY_CITY,city);
                data3.put(KEY_STATE,state);
                data3.put(KEY_PINCODE,pincode);
                data3.put(KEY_BRAND,brandIs);
                data3.put(KEY_MODEL,modelIs);
                data3.put(KEY_MANUFACTYR,man_yr);
                data3.put(KEY_REGYR,reg_yr);
                data3.put("id", "1tIq1mSOiDRn-JORShYAPzl7EY_FH45yxra8KcFYn1VA");
                return data3;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                cameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                showFileChooser();
            }
        });
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SellProductActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SellProductActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    showFileChooser();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(SellProductActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void cameraIntent() {
        Intent intent = new Intent(SellProductActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri filePath = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(SellProductActivity.this.getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                    userImage = getStringImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}