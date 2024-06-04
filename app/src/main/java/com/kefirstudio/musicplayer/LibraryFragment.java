package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    private ListView listViewLibrary;
    private LibraryManager libraryManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        listViewLibrary = view.findViewById(R.id.listViewLibrary);

        // Получение списка треков из библиотеки
        MainActivity mainActivity = (MainActivity) getActivity();
        libraryManager = mainActivity.getLibraryManager();
        final List<Track> trackList = libraryManager.getTrackList();

        // Создание адаптера для ListView
        List<String> trackTitles = new ArrayList<>();
        for (Track track : trackList) {
            trackTitles.add(track.getTitle() + " - " + track.getArtist());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, trackTitles);
        listViewLibrary.setAdapter(adapter);

        listViewLibrary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = trackList.get(position);
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("title", selectedTrack.getTitle());
                intent.putExtra("artist", selectedTrack.getArtist());
                intent.putExtra("trackPath", selectedTrack.getTrackPath()); // Предположим, у Track есть метод getTrackPath()
                startActivity(intent);
            }
        });

        return view;
    }
}
