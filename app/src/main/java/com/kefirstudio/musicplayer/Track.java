package com.kefirstudio.musicplayer;

import java.io.Serializable;
import java.util.Objects;

public class Track implements Serializable {
    private int id;
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

    public Track(int id, String title, String artist, String album, long duration, long playTime, String trackPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.playTime = playTime;
        this.trackPath = trackPath; // Инициализация пути к файлу
    }

    // Getters и setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return title.equals(track.title) &&
                artist.equals(track.artist) &&
                album.equals(track.album) &&
                trackPath.equals(track.trackPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, album, trackPath);
    }
}
