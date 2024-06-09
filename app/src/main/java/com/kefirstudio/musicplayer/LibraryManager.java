package com.kefirstudio.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

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
        trackList = new ArrayList<>();
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
        // Проверка на существование трека в библиотеке
        for (Track existingTrack : trackList) {
            if (existingTrack.equals(track)) {
                return; // Трек уже существует, выходим из метода
            }
        }

        dbHelper.addTrack(track);
        trackList.add(track);
    }

    public List<Track> getTrackList() {
        trackList = dbHelper.getAllTracks();
        return trackList;
    }

    public void deleteTrack(Track track) {
        dbHelper.deleteTrack(track);
        trackList.remove(track);
    }

    public void addAlbum(Album album) {
        albumList.add(album);
    }

    public void addPlaylist(Playlist playlist) {
        dbHelper.addPlaylist(playlist);
        if (!playlistList.contains(playlist)) {
            playlistList.add(playlist);
        }
    }

    public List<Playlist> getAllPlaylists() {
        playlistList = dbHelper.getAllPlaylists();
        return playlistList;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void addTrackToPlaylist(int playlistId, int trackId) {
        dbHelper.addTrackToPlaylist(playlistId, trackId);
    }

    public List<Track> getTracksByPlaylist(int playlistId) {
        return dbHelper.getTracksByPlaylist(playlistId);
    }

    public int getPlaylistIdByName(String playlistName) {
        return dbHelper.getPlaylistIdByName(playlistName);
    }
    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        dbHelper.removeTrackFromPlaylist(playlistId, trackId);
    }
    public void deletePlaylist(Playlist playlist) {
        dbHelper.deletePlaylist(playlist);
    }

}
