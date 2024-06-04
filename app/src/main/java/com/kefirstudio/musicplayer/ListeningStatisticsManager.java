package com.kefirstudio.musicplayer;

import java.util.ArrayList;
import java.util.List;

public class ListeningStatisticsManager {  //Просмотр истории прослушивания и аналитики
    private List<Track> recentTracks;
    private long listeningTime;

    public ListeningStatisticsManager() {
        recentTracks = new ArrayList<>();
        listeningTime = 0;
    }

    public void addTrackToHistory(Track track) {
        recentTracks.add(track);
    }

    public void addListeningTime(long time) {
        listeningTime += time;
    }

    public List<Track> getRecentTracks() {
        return recentTracks;
    }

    public long getListeningTime() {
        return listeningTime;
    }
}