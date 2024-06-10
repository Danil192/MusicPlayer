package com.kefirstudio.musicplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";
    private TextView trackTitle;
    private TextView trackArtist;
    private SeekBar seekBar;
    private Button buttonPlayPause;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    private boolean isPrepared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        trackTitle = findViewById(R.id.trackTitle);
        trackArtist = findViewById(R.id.trackArtist);
        seekBar = findViewById(R.id.seekBar);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);

        buttonPlayPause.setEnabled(false);

        initializePlayer();
    }

    private void initializePlayer() {
        // Получение данных о треке из Intent
        String title = getIntent().getStringExtra("title");
        String artist = getIntent().getStringExtra("artist");
        String trackUriString = getIntent().getStringExtra("trackPath");

        if (trackUriString == null) {
            Log.e(TAG, "trackUriString is null");
            Toast.makeText(this, "Error: Track path is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title != null) {
            trackTitle.setText(title);
        }
        if (artist != null) {
            trackArtist.setText(artist);
        }

        Log.d(TAG, "Track Path: " + trackUriString);

        // Инициализация MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mp -> {
            isPrepared = true;
            seekBar.setMax(mediaPlayer.getDuration());
            buttonPlayPause.setEnabled(true);  // Делаем кнопку активной после подготовки
            buttonPlayPause.setText("Play");
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
            Toast.makeText(PlayerActivity.this, "MediaPlayer error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
            return false;
        });

        try {
            mediaPlayer.setDataSource(trackUriString);
            mediaPlayer.prepareAsync();  // Используем prepareAsync для асинхронной подготовки
        } catch (IOException e) {
            Log.e(TAG, "Error setting data source", e);
            Toast.makeText(this, "Error loading audio file", Toast.LENGTH_SHORT).show();
        }

        buttonPlayPause.setOnClickListener(v -> {
            if (isPrepared) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    buttonPlayPause.setText("Play");
                } else {
                    mediaPlayer.start();
                    buttonPlayPause.setText("Pause");
                    updateSeekBar();
                }
            } else {
                Toast.makeText(this, "Player is not ready yet", Toast.LENGTH_SHORT).show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isPrepared) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSeekBar() {
        if (isPrepared) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(this::updateSeekBar, 1000);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
    }
}
