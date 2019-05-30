package com.light.museumguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    public static ArrayList<Map<String, Object>> list1 = new ArrayList<>();
    private ListView list;
    private TextView txt;
    public static TextView txtTitle;
    public static ImageView image;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTheme(R.style.AppTheme);
        System.out.println("THIS IS ONCREATE LIST!!!!::: " + list1);
        list = findViewById(R.id.historylist_view);
        txt = findViewById(R.id.textIfEmpty);
        SharedPreferences sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
//        if (sPref.getBoolean(MainActivity.isFirstExpoScanned, false) && !MainActivity.isFirstExpoScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.expoimage1);
//            map.put("Text", "First Expo");
//            MainActivity.isFirstExpoScannedH = true;
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isSecondExpoScanned, false) && !MainActivity.isSecondExpoScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.expoimage1);
//            MainActivity.isSecondExpoScannedH = true;
//            map.put("Text", "Second Expo");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isDombraScanned, false) && !MainActivity.isDombraScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.dombra);
//            MainActivity.isDombraScannedH = true;
//            map.put("Text", "Музыкальный инструмент «Домбра»");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isKobizScanned, false) && !MainActivity.isKobizScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.kobiz);
//            MainActivity.isKobizScannedH = true;
//            map.put("Text", "Музыкальный инструмент «Кобыз»");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isOrganScanned, false) && !MainActivity.isOrganScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.homeorgan);
//            MainActivity.isOrganScannedH = true;
//            map.put("Text", "Домашний орган");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isVarganScanned, false) && !MainActivity.isVarganScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.vargan);
//            MainActivity.isVarganScannedH = true;
//            map.put("Text", "Музыкальный инструмент «Варган»");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isMansiScanned, false) && !MainActivity.isMansiScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.mansi);
//            MainActivity.isMansiScannedH = true;
//            map.put("Text", "Культовое место манси (реконструкция)");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isYurtaScanned, false) && !MainActivity.isYurtaScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.yurta);
//            MainActivity.isYurtaScannedH = true;
//            map.put("Text", "Традиционная казахская юрта");
//            list1.add(map);
//        }
//        if (sPref.getBoolean(MainActivity.isArmyanScanned, false) && !MainActivity.isArmyanScannedH) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("Image", R.drawable.armyan);
//            MainActivity.isArmyanScannedH = true;
//            map.put("Text", "Подставка для благовоний в виде граната");
//            list1.add(map);
//        }
//        if (!MainActivity.isDombraScannedH && !MainActivity.isKobizScannedH && !MainActivity.isOrganScannedH && !MainActivity.isVarganScannedH && !MainActivity.isYurtaScannedH && !MainActivity.isMansiScannedH && !MainActivity.isArmyanScannedH) {
//            txt.setVisibility(View.VISIBLE);
//        } else {
//            txt.setVisibility(View.INVISIBLE);
//        }

        if (sPref.getBoolean(MainActivity.isDombraScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.dombra);
            MainActivity.isDombraScannedH = true;
            map.put("Text", "Музыкальный инструмент «Домбра»");
            list1.add(map);
        }
        if (sPref.getBoolean(MainActivity.isKobizScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.kobiz);
            MainActivity.isKobizScannedH = true;
            map.put("Text", "Музыкальный инструмент «Кобыз»");
            list1.add(map);
        }
        if (sPref.getBoolean(MainActivity.isOrganScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.homeorgan);
            MainActivity.isOrganScannedH = true;
            map.put("Text", "Домашний орган");
            list1.add(map);
        }
        if (sPref.getBoolean(MainActivity.isVarganScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.vargan);
            MainActivity.isVarganScannedH = true;
            map.put("Text", "Музыкальный инструмент «Варган»");
            list1.add(map);
        }
        if (sPref.getBoolean(MainActivity.isMansiScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.mansi);
            MainActivity.isMansiScannedH = true;
            map.put("Text", "Культовое место манси (реконструкция)");
            list1.add(map);
        }
        if (sPref.getBoolean(MainActivity.isYurtaScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.yurta);
            MainActivity.isYurtaScannedH = true;
            map.put("Text", "Традиционная казахская юрта");
            list1.add(map);
        }
        if (sPref.getBoolean(MainActivity.isArmyanScanned, true)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.armyan);
            MainActivity.isArmyanScannedH = true;
            map.put("Text", "Подставка для благовоний в виде граната");
            list1.add(map);
        }
        txt.setVisibility(View.INVISIBLE);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list1, R.layout.gallery_layout, new String[]{"Image", "Text"}, new int[]{R.id.icon, R.id.text1});
        list.setAdapter(simpleAdapter);
        FloatingActionButton floatingActionButton = findViewById(R.id.fabhistory);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, ScanningBarcodeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void expoClick(View view) {
        txtTitle = view.findViewById(R.id.text1);
        image = (ImageView) view.findViewById(R.id.icon);
        if (txtTitle.getText().equals("First Expo")) {
            ExpoInfoActivity.title = txtTitle.getText();
//            ExpoInfoActivity.audioResource = R.raw.palagin;
            ExpoInfoActivity.isWithPlayer = true;
            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Second Expo")) {
            ExpoInfoActivity.title = txtTitle.getText();
//            ExpoInfoActivity.audioResource = R.raw.catmeow;
            ExpoInfoActivity.isWithPlayer = false;

            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Музыкальный инструмент «Кобыз»")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.kobiz;
            ExpoInfoActivity.isWithPlayer = true;

            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Музыкальный инструмент «Домбра»")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.dombra;
            ExpoInfoActivity.isWithPlayer = true;

            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Домашний орган")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.orghan;
            ExpoInfoActivity.isWithPlayer = true;

            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Музыкальный инструмент «Варган»")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.komus;
            ExpoInfoActivity.isWithPlayer = true;
            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Традиционная казахская юрта")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.komus;
            ExpoInfoActivity.isWithPlayer = false;
            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Культовое место манси (реконструкция)")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.komus;
            ExpoInfoActivity.isWithPlayer = false;
            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
        if (txtTitle.getText().equals("Подставка для благовоний в виде граната")) {
            ExpoInfoActivity.title = txtTitle.getText();
            ExpoInfoActivity.audioResource = R.raw.komus;
            ExpoInfoActivity.isWithPlayer = false;
            ExpoInfoActivity.imageRes = image.getDrawable();
            Intent intent = new Intent(HistoryActivity.this, ExpoInfoActivity.class);
            startActivity(intent);
        }
    }
}
