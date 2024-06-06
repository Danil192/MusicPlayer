package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    private LibraryManager libraryManager;
    private EditText editTextPlaylistName;
    private Button buttonAddPlaylist;
    private ListView listViewPlaylists;
    private ArrayAdapter<String> adapter;
    private List<String> playlistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        libraryManager = ((MusicPlayerApp) getApplication()).getLibraryManager();

        editTextPlaylistName = findViewById(R.id.editTextPlaylistName);
        buttonAddPlaylist = findViewById(R.id.buttonAddPlaylist);
        listViewPlaylists = findViewById(R.id.listViewPlaylists);

        loadPlaylists();

        buttonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playlistName = editTextPlaylistName.getText().toString();
                if (!playlistName.isEmpty()) {
                    Playlist newPlaylist = new Playlist(playlistName, new ArrayList<>());
                    libraryManager.addPlaylist(newPlaylist);
                    loadPlaylists();
                    editTextPlaylistName.setText(""); // Очистить поле после добавления
                }
            }
        });

        listViewPlaylists.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPlaylist = playlistList.get(position);
            Intent intent = new Intent(PlaylistActivity.this, PlaylistDetailsActivity.class);
            intent.putExtra("playlistName", selectedPlaylist);
            startActivity(intent);
        });
    }

    private void loadPlaylists() {
        List<Playlist> playlists = libraryManager.getAllPlaylists();
        playlistList = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistList.add(playlist.getTitle());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playlistList);
        listViewPlaylists.setAdapter(adapter);
    }
}
