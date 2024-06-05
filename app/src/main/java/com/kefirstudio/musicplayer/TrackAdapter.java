package com.kefirstudio.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {
    private Context context;
    private List<Track> tracks;
    private LibraryManager libraryManager;

    public TrackAdapter(Context context, List<Track> tracks, LibraryManager libraryManager) {
        super(context, 0, tracks);
        this.context = context;
        this.tracks = tracks;
        this.libraryManager = libraryManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item, parent, false);
        }

        Track track = getItem(position);

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewArtist = convertView.findViewById(R.id.textViewArtist);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

        textViewTitle.setText(track.getTitle());
        textViewArtist.setText(track.getArtist());

        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("title", track.getTitle());
                intent.putExtra("artist", track.getArtist());
                intent.putExtra("trackPath", track.getTrackPath());
                context.startActivity(intent);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryManager.deleteTrack(track);
                tracks.remove(track);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
