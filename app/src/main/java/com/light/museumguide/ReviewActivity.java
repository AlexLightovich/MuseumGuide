package com.light.museumguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
