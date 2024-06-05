package com.kefirstudio.musicplayer;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;

public class LibraryManager {
    private static final String PREFS_NAME = "LibraryPrefs";
    private static final String KEY_LIBRARY_INITIALIZED = "LibraryInitialized";

    private List<Track> trackList;
    private List<Album> albumList;
    private List<Playlist> playlistList;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    public LibraryManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        trackList = dbHelper.getAllTracks();
        albumList = new ArrayList<>();
        playlistList = new ArrayList<>();
    }

    public boolean isLibraryInitialized() {
        return sharedPreferences.getBoolean(KEY_LIBRARY_INITIALIZED, false);
    }

    public void setLibraryInitialized() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_LIBRARY_INITIALIZED, true);
        editor.apply();
    }

    public void addTrack(Track track) {
        dbHelper.addTrack(track);
        trackList = dbHelper.getAllTracks();
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void addAlbum(Album album) {
        albumList.add(album);
    }

    public void addPlaylist(Playlist playlist) {
        playlistList.add(playlist);
    }

    public void deleteTrack(Track track) {
        dbHelper.deleteTrack(track);
        trackList = dbHelper.getAllTracks();
    }

    public void deleteAlbum(Album album) {
        albumList.remove(album);
    }

    public void deletePlaylist(Playlist playlist) {
        playlistList.remove(playlist);
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public List<Track> getTracksByPlaylist(String playlistTitle) {
        for (Playlist playlist : playlistList) {
            if (playlist.getTitle().equals(playlistTitle)) {
                return playlist.getTrackList();
            }
        }
        return new ArrayList<>();
    }
}
