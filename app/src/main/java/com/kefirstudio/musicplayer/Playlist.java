package com.kefirstudio.musicplayer;

import java.util.List;

public class Playlist {
    private int id;
    private String title;
    private List<Track> trackList;

    public Playlist(int id, String title, List<Track> trackList) {
        this.id = id;
        this.title = title;
        this.trackList = trackList;
    }

    public Playlist(String title, List<Track> trackList) {
        this.title = title;
        this.trackList = trackList;
    }

    // Геттеры и сеттеры

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

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }
}
