package com.kefirstudio.musicplayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistManager {
    private Map<String, Playlist> playlistMap;

    public PlaylistManager() {
        playlistMap = new HashMap<>();
    }

    public void createPlaylist(String title, List<Track> trackList) {
        Playlist playlist = new Playlist(title, trackList);
        playlistMap.put(title, playlist);
    }

    public void editPlaylist(String title, List<Track> newTrackList) {
        if (playlistMap.containsKey(title)) {
            Playlist playlist = playlistMap.get(title);
            playlist.setTrackList(newTrackList);
        }
    }

    public void deletePlaylist(String title) {
        playlistMap.remove(title);
    }

    public Playlist getPlaylist(String title) {
        return playlistMap.get(title);
    }
}
