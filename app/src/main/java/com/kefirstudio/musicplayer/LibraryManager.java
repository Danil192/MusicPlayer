package com.kefirstudio.musicplayer;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class LibraryManager {
    private List<Track> trackList;
    private List<Album> albumList;
    private List<Playlist> playlistList;
    private DatabaseHelper dbHelper;

    public LibraryManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        trackList = new ArrayList<>();
        albumList = new ArrayList<>();
        playlistList = new ArrayList<>();
    }

    public void addTrack(Track track) {
        dbHelper.addTrack(track);
        if (!trackList.contains(track)) {
            trackList.add(track);
        }
    }

    public List<Track> getTrackList() {
        trackList = dbHelper.getAllTracks();
        return trackList;
    }

    public void addAlbum(Album album) {
        albumList.add(album);
    }

    public void addPlaylist(Playlist playlist) {
        playlistList.add(playlist);
    }

    public void deleteTrack(Track track) {
        trackList.remove(track);
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
