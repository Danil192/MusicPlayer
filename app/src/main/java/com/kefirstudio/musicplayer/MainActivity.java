package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonLibrary;
    private Button buttonPlaylists;
    private Button buttonSettings;
    private LibraryManager libraryManager;
    private TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Применение темы
        ThemeManager.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryManager = new LibraryManager(this);
        initializeLibrary();

        buttonLibrary = findViewById(R.id.buttonLibrary);
        buttonPlaylists = findViewById(R.id.buttonPlaylists);
        buttonSettings = findViewById(R.id.buttonSettings);

        buttonLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LibraryFragment());
            }
        });

        buttonPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new PlaylistFragment());
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            loadFragment(new LibraryFragment());
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Track> filteredTracks = libraryManager.searchTracks(newText);
                updateLibraryFragment(filteredTracks);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void updateLibraryFragment(List<Track> filteredTracks) {
        LibraryFragment libraryFragment = (LibraryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (libraryFragment != null) {
            libraryFragment.updateTrackList(filteredTracks);
        }
    }

    private void initializeLibrary() {
        if (!libraryManager.isLibraryInitialized()) {
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

            // Установка флага инициализации библиотеки
            libraryManager.setLibraryInitialized();
        }
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
