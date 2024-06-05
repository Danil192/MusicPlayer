package com.kefirstudio.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {
    private final List<Track> tracks;
    private final LibraryManager libraryManager;

    public TrackAdapter(@NonNull Context context, @NonNull List<Track> objects, LibraryManager libraryManager) {
        super(context, 0, objects);
        this.tracks = objects;
        this.libraryManager = libraryManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_track, parent, false);
        }

        Track track = getItem(position);
        TextView trackTitle = convertView.findViewById(R.id.trackTitle);
        Button deleteButton = convertView.findViewById(R.id.buttonDeleteTrack);

        trackTitle.setText(track.getTitle() + " - " + track.getArtist());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Удаление трека из библиотеки
                libraryManager.deleteTrack(track);
                tracks.remove(track);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
