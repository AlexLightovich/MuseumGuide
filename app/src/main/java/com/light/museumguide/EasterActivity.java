package com.light.museumguide;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class EasterActivity extends AppCompatActivity {
    private int hits;
    private TextView tv;
    private ImageView image;
    private TextView catVoice;
    private Button exitButton;
    private AlertDialog.Builder exitDialogBuilder;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter);
        tv = findViewById(R.id.colvo);

        exitButton = findViewById(R.id.exitButton);
        hits = 0;
        exitDialogBuilder = new AlertDialog.Builder(EasterActivity.this).setCancelable(false)
                .setTitle("Уже уходишь?")
                .setMessage("Постой! Ты еще не выполнил мое задание! Ты действтельно хочешь выйти? :(")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(EasterActivity.this, "Пока-пока! Возвращайся еще! :(", Toast.LENGTH_SHORT).show();
                        exitDialog.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((hits + 5) != 100) && ((hits + 5) <= 100)) {
                            Toast.makeText(EasterActivity.this, "Молодец! Вот тебе 5 очков для продолжения! :)", Toast.LENGTH_SHORT).show();
                            hits += 5;
                        } else {
                            Toast.makeText(EasterActivity.this, "Молодец! Осталось ведь совсем чуть-чуть!", Toast.LENGTH_SHORT).show();
                        }
                        exitDialog.cancel();
                    }
                });
        exitDialog = exitDialogBuilder.create();
        catVoice = findViewById(R.id.voiceCat);
        tv.setText("Количество нажатий: " + hits);
        image = (ImageView) findViewById(R.id.catImage);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hits < 100) {
                    //dialog
                    exitDialog.show();
                } else {
                    Toast.makeText(EasterActivity.this, "Пока-пока! Спасибо, что выполнил мое задание. Приходи ещё, если захочешь! :)", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setScaleX(1f);
                image.setScaleY(0.5f);
                hits++;
//                System.out.println(hits);
                tv.setText("Количество нажатий: " + hits);
                if (hits == 10) {
                    Toast.makeText(EasterActivity.this, "Кот >> Сможешь нажать на меня 100 раз?", Toast.LENGTH_SHORT).show();
                }
                if (hits == 30) {
                    Toast.makeText(EasterActivity.this, "Кот >> Дава-а-а-ай! Ты сможешь!", Toast.LENGTH_SHORT).show();
                }
                if (hits == 50) {
                    Toast.makeText(EasterActivity.this, "Кот >> Половина пути пройдена! Так держать!", Toast.LENGTH_SHORT).show();
                }
                if (hits == 75) {
                    Toast.makeText(EasterActivity.this, "Кот >> Давай!!! Осталось совсем чуть-чуть!!!", Toast.LENGTH_SHORT).show();
                }
                if (hits == 100) {
                    catVoice.setText("Молодец! Я люблю тех людей, которые умеют добиваться целей! :*");
                    Toast.makeText(EasterActivity.this, "Thanks for using! \nWith love from alexlight0vich and _.bpolina._ ^_^", Toast.LENGTH_LONG).show();
                }
                if (hits == 150) {
                    Toast.makeText(EasterActivity.this, "Кот >> Ты уже выполнил мое задание, дальше ничего нет!", Toast.LENGTH_LONG).show();
                }
                if (hits == 200) {
                    Toast.makeText(EasterActivity.this, "Кот >> Ты все-таки решил продолжить, но дальше все равно ничего нет", Toast.LENGTH_LONG).show();
                }
                if (hits == 300) {
                    Toast.makeText(EasterActivity.this, "Кот >> Ты серьезно?!", Toast.LENGTH_LONG).show();
                }
                if (hits == 400) {
                    Toast.makeText(EasterActivity.this, "Кот >> Может хватит?", Toast.LENGTH_LONG).show();
                }
                if (hits == 500) {
                    Toast.makeText(EasterActivity.this, "Кот >> Похоже, тебя не остановить. Как закончишь - расскажи о музее и приложении своим друзьям ;)", Toast.LENGTH_LONG).show();
                }
                RestoreSize restoreSize = new RestoreSize();
                restoreSize.execute();
            }
        });
    }

    public class RestoreSize extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setScaleX(1f);
            image.setScaleY(1f);
            return null;
        }
    }
}
