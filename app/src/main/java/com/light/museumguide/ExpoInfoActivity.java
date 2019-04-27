package com.light.museumguide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ExpoInfoActivity extends AppCompatActivity {
    private float rate;
    public static CharSequence title;
    public static Drawable imageRes;
    private AlertDialog warningDialog;
    public static CharSequence addInfo;
    private boolean isDialogShowed = false;
    public static boolean isWithPlayer;
    public static int audioResource;
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private ImageButton playBtn;
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
                .setCancelable(true);
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
                if(!manager.isWiredHeadsetOn()){
                    Toast.makeText(ExpoInfoActivity.this, "НАУШНИКИ НЕ ПОДКЛЮЧЕНЫ!!!АААААААА!!!!!!!", Toast.LENGTH_SHORT).show();
                    if(!isDialogShowed) {
                        warningDialog.show();
                    }
                } else if(manager.isWiredHeadsetOn()){
                    Toast.makeText(ExpoInfoActivity.this, "УРА! НАУШНИКИ ПОДКЛЮЧЕНЫ!", Toast.LENGTH_SHORT).show();
                    isDialogShowed = true;
                }
                if(!isPlaying && isFirstPlaying && isDialogShowed){
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
                    System.out.println("GET DURATION: "+mediaPlayer.getDuration());
                    PlayingProgress setPlaying = new PlayingProgress();
                    setPlaying.execute();
                } else if(!isPlaying && !isFirstPlaying && isDialogShowed){
                    isPlaying = true;
                    mediaPlayer.start();
                    playBtn.setImageResource(R.drawable.ic_pause);
                }else {
                    isPlaying = false;
                    mediaPlayer.pause();
                    playBtn.setImageResource(R.drawable.ic_play_arrow_48px);
                }
            }
        });
        playingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
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
        isFirstPlaying = true;
        releaseMP();
    }

    private class PlayingProgress extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            while(mediaPlayer.getCurrentPosition()!= mediaPlayer.getDuration()) {
                System.out.println(mediaPlayer.getCurrentPosition());
                playingBar.setProgress(mediaPlayer.getCurrentPosition());
                if(isFirstPlaying) {
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
}
