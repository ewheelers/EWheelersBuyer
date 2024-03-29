package com.ewheelers.eWheelersBuyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewheelers.eWheelersBuyers.ModelClass.ServiceProvidersClass;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScanQRCode extends AppCompatActivity implements View.OnClickListener {
    ImageView flashOff, scan_image;
    TextView textView;
    BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    boolean flashmode = false;
    private Camera camera = null;
    String service_type;
    List<ServiceProvidersClass> serviceProvidersClassList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        service_type = getIntent().getStringExtra("icontype");
        //Toast.makeText(this, service_type, Toast.LENGTH_SHORT).show();
        flashOff = findViewById(R.id.flash_off);
        cameraView = findViewById(R.id.surfaceView);
        scan_image = findViewById(R.id.scanByImage);
        textView = findViewById(R.id.scanbyText);
        flashOff.setOnClickListener(this);
        //       assert getSupportActionBar() != null;   //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialiseDetectorsAndSources();
        String carListAsString = getIntent().getStringExtra("chargelist");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ServiceProvidersClass>>() {
        }.getType();
        serviceProvidersClassList = gson.fromJson(carListAsString, type);

        if (service_type.equals("Parking")) {
            textView.setText("Scan parking hub QR Code to book parking");
            scan_image.setImageResource(R.drawable.parking);
        } else if (service_type.equals("Charge")) {
            textView.setText("Scan QR code displayed on the charging hub");
            scan_image.setImageResource(R.drawable.ic_electric_charge);
        }else if (service_type.equals("Repair")) {
            textView.setText("Scan QR code displayed on the Repair Shop");
            scan_image.setImageResource(R.drawable.ic_mechanics);
        }else if (service_type.equals("Puncture")) {
            textView.setText("Scan QR code displayed on the Puncture Shop");
            scan_image.setImageResource(R.drawable.ic_tyre);
        }else if (service_type.equals("Batteries")) {
            textView.setText("Scan QR code displayed on the Puncture Shop");
            scan_image.setImageResource(R.drawable.ic_battery);
        }else if (service_type.equals("Key Repair")) {
            textView.setText("Scan QR code displayed on the Key Repair Shop");
            scan_image.setImageResource(R.drawable.ic_key);
        }else if (service_type.equals("Bike Wash")) {
            textView.setText("Scan QR code displayed on the Bike Wash Shop");
            scan_image.setImageResource(R.drawable.ic_waterwash);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    private void flashOnButton() {
        camera = getCamera(cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
                if (flashmode) {
                    //showToast("Flash Switched ON");
                    flashOff.setBackground(getResources().getDrawable(R.drawable.round_red_strike));
                } else {
                    //showToast("Flash Switched Off");
                    flashOff.setBackground(getResources().getDrawable(R.drawable.round_button));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void showToast(String flash_switched_on) {
        Toast.makeText(this, flash_switched_on, Toast.LENGTH_SHORT).show();
    }

    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash_off:
                flashOnButton();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText(barcodes.valueAt(0).displayValue);
                            //textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            String identifier = null;
                            for (int i = 0; i < serviceProvidersClassList.size(); i++) {
                                identifier = serviceProvidersClassList.get(i).getUaidentifier();
                                if (barcodes.valueAt(0).displayValue.equals(identifier)) {
                                    Intent intent = new Intent(getApplicationContext(), SPProductsListActivity.class);
                                    intent.putExtra("identifier2", barcodes.valueAt(0).displayValue);
                                    intent.putExtra("serviceprovider", service_type);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }
                    });
                }
            }
        });
    }

}
