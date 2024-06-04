package com.kefirstudio.musicplayer;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private List<Track> trackList;
    private List<Album> albumList;
    private List<Playlist> playlistList;

    public LibraryManager() {
        trackList = new ArrayList<>();
        albumList = new ArrayList<>();
        playlistList = new ArrayList<>();
    }

    public void addTrack(Track track) {
        trackList.add(track);
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

    // Getters for trackList, albumList, and playlistList
    public List<Track> getTrackList() {
        return trackList;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    // Добавление метода getTracksByPlaylist
    public List<Track> getTracksByPlaylist(String playlistTitle) {
        for (Playlist playlist : playlistList) {
            if (playlist.getTitle().equals(playlistTitle)) {
                return playlist.getTrackList();
            }
        }
        return new ArrayList<>();
    }
}
