package com.light.museumguide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExpoInfoActivity extends AppCompatActivity {
    private float rate;
    public static CharSequence title;
    public static Drawable imageRes;
    public static boolean isOrganRate;
    private AlertDialog warningDialog;
    private AlertDialog.Builder rateDialogBuider;
    private AlertDialog rateDialog;
    public static CharSequence addInfo;
    private boolean isDialogShowed = false;
    public static boolean isWithPlayer;
    public static int audioResource;
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private ImageButton playBtn;
    private SharedPreferences.Editor edit;
    private SharedPreferences sPref;
    private boolean isRate = false;
    AlertDialog.Builder builder;
    private AudioManager manager;
    private SeekBar playingBar;
    boolean isFirstPlaying = true;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExpoInfoActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expo_info);
        manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView titleText = findViewById(R.id.textTitle);
        sPref = getSharedPreferences("qrscan",MODE_PRIVATE);
        edit = sPref.edit();
        builder = new AlertDialog.Builder(ExpoInfoActivity.this);
        builder.setTitle("Внимание! Наушники не подключены!")
                .setMessage("К вашему телефону не подключены наушники. Рекомендуем их подключить, для того, чтобы не мешать окружающим. Если у вас нет наушников, пожалуйста, убавьте громкость и поднесите телефон поближе к уху.")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        warningDialog.cancel();
                        isDialogShowed = true;
                        playBtn.callOnClick();
                    }
                })
                .setCancelable(false);
        rateDialogBuider = new AlertDialog.Builder(ExpoInfoActivity.this);
        rateDialogBuider.setTitle("Оценка уже стоит")
                .setMessage("Хотите сменить оценку?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReplaceRateConnection replaceRateConnection = new ReplaceRateConnection();
                        replaceRateConnection.execute();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rateDialog.cancel();
                    }
                }).setCancelable(false);
        rateDialog = rateDialogBuider.create();
        TextView addInfoText = findViewById(R.id.textAddInfo);
        playingBar = findViewById(R.id.seekBar);
        warningDialog = builder.create();
        ImageView image = findViewById(R.id.imageHistory);
        playBtn = findViewById(R.id.playButton);
        playBtn.setImageResource(R.drawable.ic_play_arrow_48px);
        if (!isWithPlayer) {
            playBtn.setVisibility(View.INVISIBLE);
            playingBar.setVisibility(View.INVISIBLE);
        }
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manager.isWiredHeadsetOn()) {
                    Toast.makeText(ExpoInfoActivity.this, "НАУШНИКИ НЕ ПОДКЛЮЧЕНЫ!!!АААААААА!!!!!!!", Toast.LENGTH_SHORT).show();
                    if (!isDialogShowed) {
                        warningDialog.show();
                    }
                } else if (manager.isWiredHeadsetOn()) {
                    Toast.makeText(ExpoInfoActivity.this, "УРА! НАУШНИКИ ПОДКЛЮЧЕНЫ!", Toast.LENGTH_SHORT).show();
                    isDialogShowed = true;
                }
                if (!isPlaying && isFirstPlaying && isDialogShowed) {
                    playBtn.setImageResource(R.drawable.ic_pause);
                    isPlaying = true;
                    isFirstPlaying = false;
                    mediaPlayer = MediaPlayer.create(ExpoInfoActivity.this, audioResource);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isFirstPlaying = true;
                            isPlaying = false;
                        }
                    });
                    playingBar.setMax(mediaPlayer.getDuration());
                    System.out.println("GET DURATION: " + mediaPlayer.getDuration());
                    PlayingProgress setPlaying = new PlayingProgress();
                    setPlaying.execute();
                } else if (!isPlaying && !isFirstPlaying && isDialogShowed) {
                    isPlaying = true;
                    mediaPlayer.start();
                    playBtn.setImageResource(R.drawable.ic_pause);
                } else {
                    isPlaying = false;
                    mediaPlayer.pause();
                    playBtn.setImageResource(R.drawable.ic_play_arrow_48px);
                }
            }
        });
        playingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        titleText.setText(title);
        addInfoText.setText(addInfo);
        image.setImageDrawable(imageRes);
        DatabaseHelper databaseHelper = new DatabaseHelper(ExpoInfoActivity.this);
        Cursor dataFromDB = databaseHelper.getDataFromDB();
        while (dataFromDB.moveToNext()) {
            if (MainActivity.isFirstExpoScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("First Expo")) {
                if (title.equals("First Expo")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                }
            }
            if (MainActivity.isSecondExpoScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Second Expo")) {
                if (title.equals("Second Expo")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                }

            }
            if (MainActivity.isKobizScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Кобыз»")) {
                if (title.equals("Музыкальный инструмент «Кобыз»")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isKobizRate, false);
                }

            }
            if (MainActivity.isDombraScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Домбра»")) {
                if (title.equals("Музыкальный инструмент «Домбра»")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isDombraRate, false);
                }

            }
            if (MainActivity.isOrganScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Домашний орган")) {
                if (title.equals("Домашний орган")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isOrganRate,false);
                    System.out.println("IS RATE FROM ONCREATE:::::"+isRate);
                }

            }
            if (MainActivity.isVarganScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Варган»")) {
                if (title.equals("Музыкальный инструмент «Варган»")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isVarganRate, false);
                }

            }
            if (MainActivity.isYurtaScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Традиционная казахская юрта")) {
                if (title.equals("Традиционная казахская юрта")) {
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isYurtaRate, false);
                }

            }
            if (MainActivity.isMansiScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Культовое место манси (реконструкция)")) {
                if (title.equals("Культовое место манси (реконструкция)")) {
                    System.out.println("ZASHLO TO MANSI");
                    String add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isMansiRate, false);
                }

            }
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
                RateConnection rateConnection = new RateConnection();
                System.out.println("THIS IS IS RATE:::::::: "+isRate);
                if(isRate) {
                    rateDialog.show();
                } else {
                    rateConnection.execute();
                }
                System.out.println("fromUser:" + rate);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFirstPlaying = true;
        releaseMP();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(ExpoInfoActivity.this, audioResource);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRate = false;
        isFirstPlaying = true;
        releaseMP();
    }

    private class PlayingProgress extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            while (mediaPlayer.getCurrentPosition() != mediaPlayer.getDuration()) {
                System.out.println(mediaPlayer.getCurrentPosition());
                playingBar.setProgress(mediaPlayer.getCurrentPosition());
                if (isFirstPlaying) {
                    break;
                }
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class RateConnection extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                isRate = true;
                URL url = new URL("http://192.168.1.2/dashboard/addrate.php?device_id="+MainActivity.ANDROID_ID+"&expo="+title+"&rate="+rate);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                urlConnection.disconnect();
                if (title.equals("Домашний орган")) {
                    edit.putBoolean(MainActivity.isOrganRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isOrganRate,false));
                    isRate = true;
                }
                if (title.equals("Музыкальный инструмент «Домбра»")) {
                    SharedPreferences.Editor edit = sPref.edit();
                    edit.putBoolean(MainActivity.isDombraRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isDombraRate,false));
                    isRate = true;
                }
                if (title.equals("Музыкальный инструмент «Варган»")) {
                    SharedPreferences.Editor edit = sPref.edit();
                    edit.putBoolean(MainActivity.isVarganRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isVarganRate,false));
                    isRate = true;
                }
                if (title.equals("Музыкальный инструмент «Кобыз»")) {
                    SharedPreferences.Editor edit = sPref.edit();
                    edit.putBoolean(MainActivity.isKobizRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isKobizRate,false));
                    isRate = true;
                }
                if (title.equals("Культовое место манси (реконструкция)")) {
                    SharedPreferences.Editor edit = sPref.edit();
                    edit.putBoolean(MainActivity.isMansiRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isMansiRate,false));
                    isRate = true;
                }
                if (title.equals("Традиционная казахская юрта")) {
                    SharedPreferences.Editor edit = sPref.edit();
                    edit.putBoolean(MainActivity.isYurtaRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isYurtaRate,false));
                    isRate = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private class ReplaceRateConnection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                isRate = true;
                URL url = new URL("http://192.168.1.2/dashboard/replacerate.php?device_id="+MainActivity.ANDROID_ID+"&expo="+title+"&rate="+rate);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
