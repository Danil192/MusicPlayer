package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    private ListView listViewLibrary;
    private LibraryManager libraryManager;
    private ArrayAdapter<String> adapter;
    private List<Track> trackList;
    private List<String> trackTitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        listViewLibrary = view.findViewById(R.id.listViewLibrary);
        Button buttonAddTrack = view.findViewById(R.id.buttonAddTrack);

        // Получение списка треков из библиотеки
        MainActivity mainActivity = (MainActivity) getActivity();
        libraryManager = mainActivity.getLibraryManager();
        trackList = libraryManager.getTrackList();

        // Создание адаптера для ListView
        trackTitles = new ArrayList<>();
        for (Track track : trackList) {
            trackTitles.add(track.getTitle() + " - " + track.getArtist());
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, trackTitles);
        listViewLibrary.setAdapter(adapter);

        listViewLibrary.setOnItemClickListener((parent, view1, position, id) -> {
            Track selectedTrack = trackList.get(position);
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
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

        return view;
    }

    private void addNewTrack() {
        // Здесь можно реализовать логику для добавления новой песни. Например, можно открыть новое Activity для ввода данных о новой песне.
        // Для простоты, мы добавим тестовую песню.
        Track newTrack = new Track("New Song", "New Artist", "New Album", 180, 0, "path/to/new_song.mp3");
        trackList.add(newTrack);
        trackTitles.add(newTrack.getTitle() + " - " + newTrack.getArtist());
        adapter.notifyDataSetChanged();
        libraryManager.addTrack(newTrack);
    }
}
