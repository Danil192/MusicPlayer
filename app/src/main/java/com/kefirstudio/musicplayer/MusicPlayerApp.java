package com.kefirstudio.musicplayer;

import android.app.Application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicPlayerApp extends Application {
    private LibraryManager libraryManager;

    @Override
    public void onCreate() {
        super.onCreate();
        libraryManager = new LibraryManager();
        initializeLibrary();
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    private void initializeLibrary() {
        // Пример путей к файлам треков
        String trackPath1 = "path/to/song_one.mp3";
        String trackPath2 = "path/to/song_two.mp3";
        String trackPath3 = "path/to/song_three.mp3";

        // Создание треков
        Track track1 = new Track("Song One", "Artist One", "Album One", 210, 0, trackPath1);
        Track track2 = new Track("Song Two", "Artist Two", "Album Two", 180, 0, trackPath2);
        Track track3 = new Track("Song Three", "Artist One", "Album One", 200, 0, trackPath3);

        // Добавление треков в библиотеку
        libraryManager.addTrack(track1);
        libraryManager.addTrack(track2);
        libraryManager.addTrack(track3);

        // Создание плейлистов
        List<Track> playlist1Tracks = new ArrayList<>(Arrays.asList(track1, track2));
        Playlist playlist1 = new Playlist("Playlist One", playlist1Tracks);

        List<Track> playlist2Tracks = new ArrayList<>(Arrays.asList(track2, track3));
        Playlist playlist2 = new Playlist("Playlist Two", playlist2Tracks);

        // Добавление плейлистов в библиотеку
        libraryManager.addPlaylist(playlist1);
        libraryManager.addPlaylist(playlist2);
    }
}
