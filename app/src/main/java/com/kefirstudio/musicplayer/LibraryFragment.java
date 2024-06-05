package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.util.List;

public class LibraryFragment extends Fragment {
    private static final int ADD_TRACK_REQUEST = 1;
    private ListView listViewLibrary;
    private LibraryManager libraryManager;
    private TrackAdapter adapter;
    private List<Track> trackList;

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
        adapter = new TrackAdapter(getContext(), trackList, libraryManager);
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
                Intent intent = new Intent(getActivity(), AddTrackActivity.class);
                startActivityForResult(intent, ADD_TRACK_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TRACK_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null && data.hasExtra("newTrack")) {
                Track newTrack = (Track) data.getSerializableExtra("newTrack");
                addNewTrack(newTrack);
            }
        }
    }

    private void addNewTrack(Track newTrack) {
        // Добавление новой песни в библиотеку
        libraryManager.addTrack(newTrack);
        trackList.add(newTrack);
        adapter.notifyDataSetChanged();
    }
}
