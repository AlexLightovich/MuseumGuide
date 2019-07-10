package com.light.museumguide;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

public class SetTextSizeActivity extends AppCompatActivity {
    float textSize;
    TextView addInfo;
    AlertDialog infoDialog;
    AlertDialog.Builder infoBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_text_size);
        infoBuilder = new AlertDialog.Builder(SetTextSizeActivity.this)
                .setTitle("Помощь")
                .setCancelable(true)
                .setMessage("Используйте кнопки + и -, для изменения размера текста.\nЗатем, нажмите на галочку, для сохранения изменений.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        infoDialog.cancel();
                    }
                });
        infoDialog = infoBuilder.create();
        infoDialog.show();
        TextView title = findViewById(R.id.setSizeTextTitle);
        addInfo = findViewById(R.id.setSizeTextAddInfo);
        title.setText("Музыкальный инструмент «Варган»");
        textSize = 12f;
        DatabaseHelper databaseHelper = new DatabaseHelper(SetTextSizeActivity.this);
        Cursor dataFromDB = databaseHelper.getDataFromDB();
        while (dataFromDB.moveToNext()) {
            if (dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Варган»")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfo.setText(add_info);
            }
        }
        FloatingActionButton fabPlus = findViewById(R.id.fab_plus);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize+=1f;
                addInfo.setTextSize(textSize);
                System.out.println(textSize);
            }
        });
        FloatingActionButton fabMinus = findViewById(R.id.fab_minus);
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize-=1f;
                addInfo.setTextSize(textSize);
                System.out.println(textSize);
            }
        });
        FloatingActionButton fabDone = findViewById(R.id.fab_done);
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.textAddInfoSize = textSize;
                SharedPreferences.Editor edit = MainActivity.sPref.edit();
                edit.putFloat(MainActivity.textSizeSP, textSize);
                edit.apply();
                Toast.makeText(SetTextSizeActivity.this, "В любой момент вы сможете изменить размер текста, нажав на три точки в правом верхнем углу.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}
