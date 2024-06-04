package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailsActivity extends AppCompatActivity {

    private ListView listViewTracks;
    private LibraryManager libraryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);

        listViewTracks = findViewById(R.id.listViewTracks);

        // Получение данных о плейлисте из Intent
        String playlistTitle = getIntent().getStringExtra("playlistTitle");

        // Получение списка треков из библиотеки
        MusicPlayerApp app = (MusicPlayerApp) getApplication();
        libraryManager = app.getLibraryManager();
        List<Track> trackList = libraryManager.getTracksByPlaylist(playlistTitle);

        // Создание адаптера для ListView
        List<String> trackTitles = new ArrayList<>();
        for (Track track : trackList) {
            trackTitles.add(track.getTitle() + " - " + track.getArtist());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trackTitles);
        listViewTracks.setAdapter(adapter);

        listViewTracks.setOnItemClickListener((parent, view, position, id) -> {
            Track selectedTrack = trackList.get(position);
            Intent intent = new Intent(PlaylistDetailsActivity.this, PlayerActivity.class);
            intent.putExtra("title", selectedTrack.getTitle());
            intent.putExtra("artist", selectedTrack.getArtist());
            intent.putExtra("trackPath", selectedTrack.getTrackPath());
            startActivity(intent);
        });
    }
}
