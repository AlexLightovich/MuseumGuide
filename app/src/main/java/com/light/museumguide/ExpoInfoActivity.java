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
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Locale;

public class ExpoInfoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener  {
    private TextToSpeech mTts;
    private float rate;
    public static CharSequence title;
    public static Drawable imageRes;
    public static boolean isOrganRate;
    private AlertDialog warningDialog;
    private static boolean isStop;
    private AlertDialog.Builder rateDialogBuider;
    private AlertDialog rateDialog;
    public static CharSequence addInfo;
    private boolean isDialogShowed = false;
    public static boolean isWithPlayer;
    public static int audioResource;
    private boolean isPlaying = false;
    private String add_info;
    private MediaPlayer mediaPlayer;
    private ImageButton playBtn;
    private SharedPreferences.Editor edit;
    private SharedPreferences sPref;
    private boolean isRate = false;
    AlertDialog.Builder builder;
    private static PlayingProgress setPlaying;
    private AudioManager manager;
    private SeekBar playingBar;
    boolean isFirstPlaying = true;

    @Override
    public void onBackPressed() {
        isStop = true;
        releaseMP();
        Intent intent = new Intent(ExpoInfoActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expo_info);
        setPlaying = new PlayingProgress();
        mTts = new TextToSpeech(this, this);
        manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView titleText = findViewById(R.id.textTitle);
        isStop = false;
        Button btn = findViewById(R.id.listenTextBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayWords();
            }
        });
        sPref = getSharedPreferences("qrscan",MODE_PRIVATE);
        edit = sPref.edit();
        builder = new AlertDialog.Builder(ExpoInfoActivity.this);
        builder.setTitle("Внимание! Наушники не подключены!")
                .setMessage("К вашему устройству не подключены наушники. Рекомендуем их подключить, для того, чтобы не мешать окружающим. Если у вас нет наушников, пожалуйста, убавьте громкость и поднесите устройство поближе к уху.")
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
                    if (!isDialogShowed) {
                        warningDialog.show();
                    }
                } else if (manager.isWiredHeadsetOn()) {
                    isDialogShowed = true;
                }
                if (!isPlaying && isFirstPlaying && isDialogShowed) {
                    playBtn.setImageResource(R.drawable.ic_pause);
                    isPlaying = true;
                    mediaPlayer = MediaPlayer.create(ExpoInfoActivity.this, audioResource);
                    isFirstPlaying = false;
                    Toast.makeText(ExpoInfoActivity.this, R.string.player_toast_text, Toast.LENGTH_LONG).show();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isFirstPlaying = true;
                            isPlaying = false;
                            isStop = true;
                            setPlaying.cancel(false);
                        }
                    });
                    playingBar.setMax(mediaPlayer.getDuration());
                    setPlaying = new PlayingProgress();
                    setPlaying.execute();
                } else if (!isPlaying && !isFirstPlaying && isDialogShowed) {
                    isPlaying = true;
                    isStop = false;
                    mediaPlayer = MediaPlayer.create(ExpoInfoActivity.this, audioResource);
                    mediaPlayer.start();
                    setPlaying = new PlayingProgress();
                    setPlaying.execute();
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
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                }
            }
            if (MainActivity.isSecondExpoScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Second Expo")) {
                if (title.equals("Second Expo")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                }

            }
            if (MainActivity.isKobizScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Кобыз»")) {
                if (title.equals("Музыкальный инструмент «Кобыз»")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isKobizRate, false);
                }

            }
            if (MainActivity.isDombraScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Домбра»")) {
                if (title.equals("Музыкальный инструмент «Домбра»")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isDombraRate, false);
                }

            }
            if (MainActivity.isOrganScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Домашний орган")) {
                if (title.equals("Домашний орган")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isOrganRate,false);
                }

            }
            if (MainActivity.isVarganScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Музыкальный инструмент «Варган»")) {
                if (title.equals("Музыкальный инструмент «Варган»")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isVarganRate, false);
                }

            }
            if (MainActivity.isYurtaScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Традиционная казахская юрта")) {
                if (title.equals("Традиционная казахская юрта")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isYurtaRate, false);
                }

            }
            if (MainActivity.isMansiScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Культовое место манси (реконструкция)")) {
                if (title.equals("Культовое место манси (реконструкция)")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isMansiRate, false);
                }

            }
            if (MainActivity.isArmyanScannedH && dataFromDB.getString(dataFromDB.getColumnIndex("name_expo")).equals("Подставка для благовоний в виде граната")) {
                if (title.equals("Подставка для благовоний в виде граната")) {
                    add_info = dataFromDB.getString(dataFromDB.getColumnIndex("add_info"));
                    addInfoText.setText(add_info);
                    isRate = sPref.getBoolean(MainActivity.isArmyanRate, false);
                }

            }
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
                RateConnection rateConnection = new RateConnection();
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
        isStop = true;
        super.onStop();
        isPlaying = false;
        isFirstPlaying = true;
        setPlaying.cancel(false);
        releaseMP();
    }

    @Override
    protected void onResume() {
        isStop = false;
        super.onResume();
        mediaPlayer = MediaPlayer.create(ExpoInfoActivity.this, audioResource);
    }

    @Override
    protected void onPause() {
        isStop = true;
        mTts.stop();
        super.onPause();
        setPlaying.cancel(false);
        isRate = false;
        isFirstPlaying = true;
        releaseMP();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            // int result = mTts.setLanguage(Locale.US);
            // используем русский язык
            Locale locale = new Locale("ru");
            int result = mTts.setLanguage(locale);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Данный язык не поддерживается");
            } else {
//                sayWords();
            }

        } else {
            Log.e("TTS", "Не удалось инициализировать движок!");
        }


    }

    private class PlayingProgress extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            while (mediaPlayer.getCurrentPosition() != mediaPlayer.getDuration()) {
                if(isStop) {
                    break;
                }
                System.out.println(mediaPlayer.getCurrentPosition());
                playingBar.setProgress(mediaPlayer.getCurrentPosition());
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        releaseMP();
        super.onDestroy();

    }

    private void sayWords() {
        mTts.setPitch(1f);
        mTts.setSpeechRate(1f);
        // Получим текст из текстового поля
        String text = add_info;
        // Проговариваем
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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
//                URL url = new URL("http://217.25.223.243/dashboard/addrate.php?device_id="+MainActivity.ANDROID_ID+"&expo="+title+"&rate="+rate);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                urlConnection.disconnect();
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
                if (title.equals("Подставка для благовоний в виде граната")) {
                    SharedPreferences.Editor edit = sPref.edit();
                    edit.putBoolean(MainActivity.isArmyanRate, true);
                    edit.commit();
                    System.out.println(sPref.getBoolean(MainActivity.isArmyanRate,false));
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
//                URL url = new URL("http://217.25.223.243/dashboard/replacerate.php?device_id="+MainActivity.ANDROID_ID+"&expo="+title+"&rate="+rate);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
