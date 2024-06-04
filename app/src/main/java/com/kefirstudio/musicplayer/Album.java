package com.kefirstudio.musicplayer;

import java.util.List;

public class Album {
    private String title;
    private String cover;
    private List<Track> trackList;

    public Album(String title, String cover, List<Track> trackList) {
        this.title = title;
        this.cover = cover;
        this.trackList = trackList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }
}
