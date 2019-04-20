package com.light.museumguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ScanningBarcodeActivity extends AppCompatActivity {
    CameraView cameraView;
    Button btnDetect;
    android.app.AlertDialog waitingDialog;
    AlertDialog withWay;
    private int backPressCounter = 0;

    @Override
    protected void onResume() {
        super.onResume();
        backPressCounter = 0;
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backPressCounter = 0;
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_barcode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraView = (CameraView) findViewById(R.id.cameraview);
        btnDetect = (Button) findViewById(R.id.btn_detect);
        backPressCounter = 0;
        waitingDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait")
                .setCancelable(false)
                .build();

        AlertDialog.Builder withWayBuilder = new AlertDialog.Builder(this)
                .setMessage("Хотите ли вы пройти по маршруту, который рекомендуют наши специалисты?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ScanningBarcodeActivity.this, "YESSSSSSSS", Toast.LENGTH_SHORT).show();
                        MapFragment.isWithWay = true;
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.isWithWayCheck, true);
                        edit.commit();
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.id = R.id.nav_manage;
                        Intent intent = new Intent(ScanningBarcodeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ScanningBarcodeActivity.this, "NOOOOOOOOO", Toast.LENGTH_SHORT).show();
                        MapFragment.isWithWay = false;
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.isWithWayCheck, false);
                        edit.commit();
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.id = R.id.nav_manage;
                        Intent intent = new Intent(ScanningBarcodeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).setCancelable(false);
        withWay = withWayBuilder.create();

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();
                runDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCounter++;
        if (backPressCounter == 1) {
            Toast.makeText(this, "Нажмите кнопку Назад еще раз, чтобы вернутся на главный экран.", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter == 2) {
            backPressCounter = 0;
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void runDetector(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_QR_CODE,
                        FirebaseVisionBarcode.FORMAT_AZTEC
                )
                .build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        detector.detectInImage(image).
                addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanningBarcodeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                Toast.makeText(ScanningBarcodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size() == 0) {
            Toast.makeText(this, "QR код не найден. Повторите попытку.", Toast.LENGTH_LONG).show();
            cameraView.start();
        }
        for (FirebaseVisionBarcode item : firebaseVisionBarcodes) {
            int value_type = item.getValueType();
            switch (value_type) {
                case FirebaseVisionBarcode.TYPE_TEXT: {
                    String rawValue = item.getRawValue();
                    Toast.makeText(this, rawValue, Toast.LENGTH_SHORT).show();

//                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
//                    builder.setMessage(item.getRawValue());
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    android.support.v7.app.AlertDialog dialog = builder.create();
//                    dialog.show();
                    cameraView.start();
                    if (rawValue.equals("Go to back!")) {
                        cameraView.stop();
                        Intent intent = new Intent(ScanningBarcodeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (rawValue.equals("EXPO={ETNOGRAPH}")) {
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.APP_PREFERENCES, true);
                        edit.putBoolean(MainActivity.isWithWayCheck, true);
                        edit.commit();
                        boolean aBoolean = sPref.getBoolean(MainActivity.APP_PREFERENCES, false);
                        System.out.println(aBoolean);
                        withWay.show();

                    } else if (rawValue.equals("FirstDebugExpo")&& !MainActivity.isFirstExpoScannedH) {
                        cameraView.stop();
                        HistoryActivity ha = new HistoryActivity();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Image",R.drawable.expoimage1);
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.isFirstExpoScanned, true);
                        edit.commit();
                        map.put("Text", "First Expo");
                        ha.list1.add(map);
                        System.out.println("HA LIST"+ha.list1);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.id = R.id.nav_manage;
                        ExpoInfoActivity.imageRes = getDrawable(R.drawable.expoimage1);
                        ExpoInfoActivity.title = "First Expo";
                        MainActivity.isFirstExpoScannedH = true;
                        Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                        startActivity(intent);
                    } else if (rawValue.equals("SecondDebugExpo")&& !MainActivity.isSecondExpoScannedH) {
                        cameraView.stop();
                        HistoryActivity ha = new HistoryActivity();
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.isSecondExpoScanned, true);
                        edit.commit();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Image",R.drawable.expoimage1);
                        map.put("Text", "Second Expo");
                        ha.list1.add(map);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.id = R.id.nav_manage;
                        ExpoInfoActivity.imageRes = getDrawable(R.drawable.expoimage1);
                        ExpoInfoActivity.title = "Second Expo";
                        MainActivity.isSecondExpoScannedH = true;
                        Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                        startActivity(intent);
                    }
                    break;
                }

                default: {
                    Toast.makeText(this, "Вы отсканировали QR код не находящийся в музее.", Toast.LENGTH_SHORT).show();
                    cameraView.start();
                    break;
                }
            }
        }
        waitingDialog.dismiss();
    }
}