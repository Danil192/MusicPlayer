package com.kefirstudio.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.List;

public class LibraryManager {
    private static final String PREFS_NAME = "LibraryPrefs";
    private static final String KEY_LIBRARY_INITIALIZED = "LibraryInitialized";

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    public LibraryManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
        // Проверка на существование трека в базе данных
        if (!dbHelper.trackExists(track)) {
            dbHelper.addTrack(track);
        } else {
            Log.d("LibraryManager", "Track already exists: " + track.getTitle());
        }
    }

    public List<Track> getTrackList() {
        return dbHelper.getAllTracks();
    }

    public void deleteTrack(Track track) {
        dbHelper.deleteTrack(track);
    }

    public void addAlbum(Album album) {
        // Добавление альбома в базу данных (если требуется)
    }

    public void addPlaylist(Playlist playlist) {
        dbHelper.addPlaylist(playlist);
    }

    public List<Playlist> getAllPlaylists() {
        return dbHelper.getAllPlaylists();
    }

    public List<Album> getAlbumList() {
        // Получение списка альбомов из базы данных (если требуется)
        return null;
    }

    public List<Playlist> getPlaylistList() {
        return getAllPlaylists();
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
