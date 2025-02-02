package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailsActivity extends AppCompatActivity {

    private ListView listViewTracks;
    private LibraryManager libraryManager;
    private ArrayAdapter<String> adapter;
    private List<Track> trackList;
    private List<String> trackTitles;
    private String playlistName;
    private int playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);

        listViewTracks = findViewById(R.id.listViewTracks);
        Button buttonAddTrack = findViewById(R.id.buttonAddTrack);

        // Получение данных о плейлисте из Intent
        playlistName = getIntent().getStringExtra("playlistName");

        // Получение списка треков из библиотеки
        MusicPlayerApp app = (MusicPlayerApp) getApplication();
        libraryManager = app.getLibraryManager();
        playlistId = libraryManager.getPlaylistIdByName(playlistName);
        trackList = libraryManager.getTracksByPlaylist(playlistId);

        // Создание адаптера для ListView
        trackTitles = new ArrayList<>();
        for (Track track : trackList) {
            trackTitles.add(track.getTitle() + " - " + track.getArtist());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trackTitles);
        listViewTracks.setAdapter(adapter);

        listViewTracks.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(PlaylistDetailsActivity.this, PlayerActivity.class);
            intent.putExtra("playlistId", playlistId);
            intent.putExtra("trackIndex", position);
            startActivity(intent);
        });

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTrackDialog();
            }
        });
    }

    private void showAddTrackDialog() {
        // Получение списка треков из библиотеки
        List<Track> allTracks = libraryManager.getTrackList();
        List<String> trackTitles = new ArrayList<>();
        for (Track track : allTracks) {
            trackTitles.add(track.getTitle() + " - " + track.getArtist());
        }

        // Создание AlertDialog для отображения списка треков
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите песню для добавления");
        builder.setItems(trackTitles.toArray(new String[0]), (dialog, which) -> {
            Track selectedTrack = allTracks.get(which);
            addTrackToPlaylist(selectedTrack);
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void addTrackToPlaylist(Track track) {
        // Добавление трека в плейлист и обновление списка
        trackList.add(track);
        trackTitles.add(track.getTitle() + " - " + track.getArtist());
        adapter.notifyDataSetChanged();

        // Обновление плейлиста в LibraryManager
        libraryManager.addTrackToPlaylist(playlistId, track.getId());
    }
}
