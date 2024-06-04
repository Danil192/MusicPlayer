package com.kefirstudio.musicplayer;

public class Track {
    private String title;
    private String artist;
    private String album;
    private long duration;
    private long playTime;
    private String trackPath; // Добавлено поле для пути к файлу

    public Track(String title, String artist, String album, long duration, long playTime, String trackPath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.playTime = playTime;
        this.trackPath = trackPath; // Инициализация пути к файлу
    }

    // Getters и setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public String getTrackPath() {
        return trackPath;
    }

    public void setTrackPath(String trackPath) {
        this.trackPath = trackPath;
    }
}
