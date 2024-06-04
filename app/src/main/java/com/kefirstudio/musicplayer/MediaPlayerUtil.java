package com.kefirstudio.musicplayer;

import android.media.MediaPlayer;

public class MediaPlayerUtil {   //Проигрывание треков
    private MediaPlayer mediaPlayer;

    public void playTrack(String trackPath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(trackPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stopTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}