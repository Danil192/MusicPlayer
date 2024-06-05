package com.kefirstudio.musicplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {
    private LibraryManager libraryManager;

    public TrackAdapter(Context context, List<Track> tracks, LibraryManager libraryManager) {
        super(context, 0, tracks);
        this.libraryManager = libraryManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_track, parent, false);
        }

        TextView textViewTrackTitle = convertView.findViewById(R.id.textViewTrackTitle);
        Button buttonDeleteTrack = convertView.findViewById(R.id.buttonDeleteTrack);

        textViewTrackTitle.setText(track.getTitle() + " - " + track.getArtist());

        buttonDeleteTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Track")
                        .setMessage("Are you sure you want to delete this track?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                libraryManager.deleteTrack(track);
                                remove(track);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return convertView;
    }
}
