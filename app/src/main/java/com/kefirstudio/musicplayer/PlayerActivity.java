package com.kefirstudio.musicplayer;

import android.annotation.SuppressLint;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";
    private TextView trackTitle;
    private TextView trackArtist;
    private SeekBar seekBar;
    private Button buttonPlayPause;
    private Button buttonPrevious;
    private Button buttonNext;
    private TextView startTime;
    private TextView endTime;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    private boolean isPrepared = false;
    private LibraryManager libraryManager;
    private List<Track> trackList;
    private int currentTrackIndex = 0;
    private boolean isPlaylist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        trackTitle = findViewById(R.id.trackTitle);
        trackArtist = findViewById(R.id.trackArtist);
        seekBar = findViewById(R.id.seekBar);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        libraryManager = new LibraryManager(this);

        // Получение данных из Intent
        int playlistId = getIntent().getIntExtra("playlistId", -1);
        currentTrackIndex = getIntent().getIntExtra("trackIndex", 0);

        if (playlistId != -1) {
            trackList = libraryManager.getTracksByPlaylist(playlistId);
            isPlaylist = true;
        } else {
            trackList = libraryManager.getTrackList();
        }

        if (trackList == null || trackList.isEmpty()) {
            Toast.makeText(this, "Нет треков для воспроизведения", Toast.LENGTH_SHORT).show();
            return;
        }

        initializePlayer(currentTrackIndex);

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

        buttonPrevious.setOnClickListener(v -> playPreviousTrack());

        buttonNext.setOnClickListener(v -> playNextTrack());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isPrepared) {
                    mediaPlayer.seekTo(progress);
                    startTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initializePlayer(int trackIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        Track currentTrack = trackList.get(trackIndex);

        trackTitle.setText(currentTrack.getTitle());
        trackArtist.setText(currentTrack.getArtist());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mp -> {
            isPrepared = true;
            int duration = mediaPlayer.getDuration();
            seekBar.setMax(duration);
            endTime.setText(formatTime(duration));
            buttonPlayPause.setEnabled(true);
            buttonPlayPause.setText("Play");
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
            Toast.makeText(PlayerActivity.this, "MediaPlayer error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
            return false;
        });

        try {
            mediaPlayer.setDataSource(this, Uri.parse(currentTrack.getTrackPath()));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Error setting data source", e);
            Toast.makeText(this, "Error loading audio file", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekBar() {
        if (isPrepared) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            startTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(this::updateSeekBar, 1000);
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
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

    private void playPreviousTrack() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--;
            initializePlayer(currentTrackIndex);
        } else {
            Toast.makeText(this, "Это первый трек", Toast.LENGTH_SHORT).show();
        }
    }

    private void playNextTrack() {
        if (currentTrackIndex < trackList.size() - 1) {
            currentTrackIndex++;
            initializePlayer(currentTrackIndex);
        } else {
            Toast.makeText(this, "Это последний трек", Toast.LENGTH_SHORT).show();
        }
    }
}
