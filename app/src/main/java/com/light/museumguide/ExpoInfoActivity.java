package com.light.museumguide;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ExpoInfoActivity extends AppCompatActivity {
    private float rate;
    public static CharSequence title;
    public static Drawable imageRes;
    public static CharSequence addInfo;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExpoInfoActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expo_info);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView titleText = findViewById(R.id.textTitle);
        TextView addInfoText = findViewById(R.id.textAddInfo);
        ImageView image = findViewById(R.id.imageHistory);
        titleText.setText(title);
        addInfoText.setText(addInfo);
        image.setImageDrawable(imageRes);
        DatabaseHelper databaseHelper = new DatabaseHelper(ExpoInfoActivity.this);
        Cursor dataFromDB = databaseHelper.getDataFromDB();
        while(dataFromDB.moveToNext()){
            if(MainActivity.isFirstExpoScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("First Expo")){
                if(title.equals("First Expo")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                }
            }
            if(MainActivity.isSecondExpoScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Second Expo")){
                if(title.equals("Second Expo")){
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                }

            }
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
                System.out.println("fromUser:"+rate);
            }
        });
    }
}
