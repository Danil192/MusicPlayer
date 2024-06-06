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
        // Generate a unique ID for the playlist (you might want to use a different method for this)
        int playlistId = playlistMap.size() + 1;
        Playlist playlist = new Playlist(playlistId, title, trackList);
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
