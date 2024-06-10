package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
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

        MainActivity mainActivity = (MainActivity) getActivity();
        libraryManager = mainActivity.getLibraryManager();
        trackList = libraryManager.getTrackList();

        adapter = new TrackAdapter(getContext(), trackList, libraryManager);
        listViewLibrary.setAdapter(adapter);

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
        if (requestCode == ADD_TRACK_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null && data.hasExtra("newTrack")) {
                Track newTrack = (Track) data.getSerializableExtra("newTrack");
                addNewTrack(newTrack);
            }
        }
    }

    private void addNewTrack(Track newTrack) {
        libraryManager.addTrack(newTrack);
        trackList.add(newTrack);  // Добавляем новый трек в локальный список
        adapter.notifyDataSetChanged();  // Уведомляем адаптер об изменениях
    }

    public void updateTrackList(List<Track> filteredTracks) {
        trackList.clear();
        trackList.addAll(filteredTracks);
        adapter.notifyDataSetChanged();
    }
}
