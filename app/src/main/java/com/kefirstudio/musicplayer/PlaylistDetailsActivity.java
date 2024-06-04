package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailsActivity extends AppCompatActivity {

    private ListView listViewTracks;
    private LibraryManager libraryManager;
    private ArrayAdapter<String> adapter;
    private List<Track> trackList;
    private List<String> trackTitles;
    private String playlistTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);

        listViewTracks = findViewById(R.id.listViewTracks);
        Button buttonAddTrack = findViewById(R.id.buttonAddTrack);

        // Получение данных о плейлисте из Intent
        playlistTitle = getIntent().getStringExtra("playlistTitle");

        // Получение списка треков из библиотеки
        MusicPlayerApp app = (MusicPlayerApp) getApplication();
        libraryManager = app.getLibraryManager();
        trackList = libraryManager.getTracksByPlaylist(playlistTitle);

        // Создание адаптера для ListView
        trackTitles = new ArrayList<>();
        for (Track track : trackList) {
            trackTitles.add(track.getTitle() + " - " + track.getArtist());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trackTitles);
        listViewTracks.setAdapter(adapter);

        listViewTracks.setOnItemClickListener((parent, view, position, id) -> {
            Track selectedTrack = trackList.get(position);
            Intent intent = new Intent(PlaylistDetailsActivity.this, PlayerActivity.class);
            intent.putExtra("title", selectedTrack.getTitle());
            intent.putExtra("artist", selectedTrack.getArtist());
            intent.putExtra("trackPath", selectedTrack.getTrackPath());
            startActivity(intent);
        });

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTrack();
            }
        });
    }

    private void addNewTrack() {
        // Здесь можно реализовать логику для добавления новой песни. Например, можно открыть новое Activity для ввода данных о новой песне.
        // Для простоты, мы добавим тестовую песню.
        Track newTrack = new Track("New Song", "New Artist", playlistTitle, 180, 0, "path/to/new_song.mp3");
        trackList.add(newTrack);
        trackTitles.add(newTrack.getTitle() + " - " + newTrack.getArtist());
        adapter.notifyDataSetChanged();
        libraryManager.addTrack(newTrack);
        for (Playlist playlist : libraryManager.getPlaylistList()) {
            if (playlist.getTitle().equals(playlistTitle)) {
                playlist.getTrackList().add(newTrack);
                break;
            }
        }
    }
}
