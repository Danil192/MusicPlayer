package com.kefirstudio.musicplayer;

import java.util.ArrayList;
import java.util.List;

public class SearchManager {
    private LibraryManager libraryManager;

    public SearchManager(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    public List<Track> searchTracks(String query) {
        List<Track> result = new ArrayList<>();
        for (Track track : libraryManager.getTrackList()) {
            if (track.getTitle().contains(query) || track.getArtist().contains(query)) {
                result.add(track);
            }
        }
        return result;
    }

    public List<Album> searchAlbums(String query) {
        List<Album> result = new ArrayList<>();
        for (Album album : libraryManager.getAlbumList()) {
            if (album.getTitle().contains(query)) {
                result.add(album);
            }
        }
        return result;
    }

    public List<Playlist> searchPlaylists(String query) {
        List<Playlist> result = new ArrayList<>();
        for (Playlist playlist : libraryManager.getPlaylistList()) {
            if (playlist.getTitle().contains(query)) {
                result.add(playlist);
            }
        }
        return result;
    }
}
