package com.light.museumguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        backPressCounter = 0;
        cameraView.stop();
        super.onPause();
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
                .setMessage("Пожалуйста подождите...")
                .setCancelable(false)
                .build();

        AlertDialog.Builder withWayBuilder = new AlertDialog.Builder(this)
                .setMessage("Хотите ли вы пройти по маршруту, который рекомендуют наши специалисты?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapFragment.isWithWay = true;
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.isWithWayCheck, true);
                        edit.commit();
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.id = R.id.nav_map;
                        Intent intent = new Intent(ScanningBarcodeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapFragment.isWithWay = false;
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.isWithWayCheck, false);
                        edit.commit();
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.id = R.id.nav_map;
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
            finish();
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
                    cameraView.start();
                    if (rawValue.equals("Go to back!")) {
                        cameraView.stop();
                        Intent intent = new Intent(ScanningBarcodeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (rawValue.equals("EXPO={ETNOGRAPH}")) {
                        cameraView.stop();
                        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sPref.edit();
                        edit.putBoolean(MainActivity.APP_PREFERENCES, true);
                        edit.putBoolean(MainActivity.isWithWayCheck, true);
                        edit.commit();
                        MainActivity.isQRScanned = true;
                        withWay.show();

                    }
                    MainActivity ma = new MainActivity();
                    if(ma.isQRScanned && !rawValue.equals("EXPO={ETNOGRAPH}")) {
                        if (rawValue.equals("ELEMT={KOB}")) {
                            if(MainActivity.isKobizScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isKobizScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.kobiz);
                                map.put("Text", "Музыкальный инструмент «Кобыз»");
                                ExpoInfoActivity.audioResource = R.raw.kobiz;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = true;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.kobiz);
                                ExpoInfoActivity.title = "Музыкальный инструмент «Кобыз»";
                                MainActivity.isKobizScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rawValue.equals("ELEMT={DOMB}")) {
                            if(MainActivity.isDombraScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isDombraScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.dombra);
                                map.put("Text", "Музыкальный инструмент «Домбра»");
                                ExpoInfoActivity.audioResource = R.raw.dombra;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = true;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.dombra);
                                ExpoInfoActivity.title = "Музыкальный инструмент «Домбра»";
                                MainActivity.isDombraScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rawValue.equals("ELEMT={ORGN}")) {
                            if(MainActivity.isOrganScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isOrganScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.homeorgan);
                                map.put("Text", "Домашний орган");
                                ExpoInfoActivity.audioResource = R.raw.orghan;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = true;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.homeorgan);
                                ExpoInfoActivity.title = "Домашний орган";
                                MainActivity.isOrganScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rawValue.equals("ELEMT={VRGN}")) {
                            if(MainActivity.isVarganScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isVarganScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.vargan);
                                map.put("Text", "Музыкальный инструмент «Варган»");
//                        ExpoInfoActivity.audioResource = R.raw.catmeow;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = true;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.vargan);
                                ExpoInfoActivity.audioResource = R.raw.komus;
                                ExpoInfoActivity.title = "Музыкальный инструмент «Варган»";
                                MainActivity.isVarganScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rawValue.equals("ELEMT={YRT}")) {
                            if(MainActivity.isYurtaScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isYurtaScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.yurta);
                                map.put("Text", "Традиционная казахская юрта");
                                ExpoInfoActivity.audioResource = R.raw.orghan;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = false;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.yurta);
                                ExpoInfoActivity.title = "Традиционная казахская юрта";
                                MainActivity.isYurtaScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rawValue.equals("ELEMT={MNS}")) {
                            if (MainActivity.isMansiScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isMansiScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.mansi);
                                map.put("Text", "Культовое место манси (реконструкция)");
                                ExpoInfoActivity.audioResource = R.raw.orghan;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = false;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.mansi);
                                ExpoInfoActivity.title = "Культовое место манси (реконструкция)";
                                MainActivity.isMansiScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                            }
                        } else if (rawValue.equals("ELEMT={BLGVN}")) {
                            if(MainActivity.isArmyanScannedH) {
                                cameraView.stop();
                                HistoryActivity ha = new HistoryActivity();
                                SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sPref.edit();
                                edit.putBoolean(MainActivity.isArmyanScanned, true);
                                edit.commit();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Image", R.drawable.armyan);
                                map.put("Text", "Подставка для благовоний в виде граната");
                                ExpoInfoActivity.audioResource = R.raw.orghan;
                                ha.list1.add(map);
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.id = R.id.nav_map;
                                ExpoInfoActivity.isWithPlayer = false;
                                ExpoInfoActivity.imageRes = getDrawable(R.drawable.armyan);
                                ExpoInfoActivity.title = "Подставка для благовоний в виде граната";
                                MainActivity.isArmyanScannedH = true;
                                Intent intent = new Intent(ScanningBarcodeActivity.this, ExpoInfoActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(this, "Вы уже отсканировали этот экспонат", Toast.LENGTH_SHORT).show();
                                cameraView.start();
                            }
                        }else {
                            Toast.makeText(ScanningBarcodeActivity.this, "Вы отсканировали QR код не находящийся в музее.", Toast.LENGTH_SHORT).show();
                            cameraView.start();
                        }
                    }else {
                        Toast.makeText(ScanningBarcodeActivity.this, "Для начала, необходимо отсканировать QR-Код у входа выставки", Toast.LENGTH_SHORT).show();
                        cameraView.start();

                    }
                    break;
                }

                default: {
                    break;
                }
            }
        }
        waitingDialog.dismiss();
    }
}