package com.kefirstudio.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {
    private ListView listViewPlaylists;
    private LibraryManager libraryManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        listViewPlaylists = view.findViewById(R.id.listViewPlaylists);

        // Получение списка плейлистов из библиотеки
        MainActivity mainActivity = (MainActivity) getActivity();
        libraryManager = mainActivity.getLibraryManager();
        List<Playlist> playlistList = libraryManager.getPlaylistList();

        // Создание адаптера для ListView
        List<String> playlistTitles = new ArrayList<>();
        for (Playlist playlist : playlistList) {
            playlistTitles.add(playlist.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, playlistTitles);
        listViewPlaylists.setAdapter(adapter);

        return view;
    }
}
