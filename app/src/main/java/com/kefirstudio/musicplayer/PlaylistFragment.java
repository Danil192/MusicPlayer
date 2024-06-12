package com.kefirstudio.musicplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {
    private ListView listViewPlaylists;
    private LibraryManager libraryManager;
    private ArrayAdapter<String> adapter;
    private List<String> playlistTitles;
    private List<Playlist> playlistList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
        listViewPlaylists = rootView.findViewById(R.id.listViewPlaylists);
        Button buttonCreatePlaylist = rootView.findViewById(R.id.buttonCreatePlaylist);

        // Получение списка плейлистов из библиотеки
        MainActivity mainActivity = (MainActivity) getActivity();
        libraryManager = mainActivity.getLibraryManager();
        playlistList = libraryManager.getAllPlaylists();

        // Создание адаптера для ListView
        playlistTitles = new ArrayList<>();
        for (Playlist playlist : playlistList) {
            playlistTitles.add(playlist.getTitle());
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, playlistTitles);
        listViewPlaylists.setAdapter(adapter);

        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                Playlist selectedPlaylist = playlistList.get(position);
                Intent intent = new Intent(getActivity(), PlaylistDetailsActivity.class);
                intent.putExtra("playlistName", selectedPlaylist.getTitle());
                startActivity(intent);
            }
        });

        listViewPlaylists.setOnItemLongClickListener((parent, itemView, position, id) -> {
            showDeletePlaylistDialog(position);
            return true;
        });

        buttonCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePlaylistDialog();
            }
        });

        return rootView;
    }

    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Создать плейлист");

        final EditText input = new EditText(getActivity());
        input.setHint("Название плейлиста");
        builder.setView(input);

        builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = input.getText().toString();
                if (!playlistName.isEmpty()) {
                    createPlaylist(playlistName);
                } else {
                    Toast.makeText(getActivity(), "Название не может быть пустым", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }

    private void createPlaylist(String playlistName) {
        Playlist newPlaylist = new Playlist(playlistName, new ArrayList<Track>());
        libraryManager.addPlaylist(newPlaylist);
        playlistList.add(newPlaylist);
        playlistTitles.add(playlistName);
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Плейлист создан", Toast.LENGTH_SHORT).show();
    }

    private void showDeletePlaylistDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Удалить плейлист");
        builder.setMessage("Вы уверены, что хотите удалить этот плейлист?");
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePlaylist(position);
            }
        });
        builder.setNegativeButton("Отмена", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }

    private void deletePlaylist(int position) {
        Playlist playlist = playlistList.get(position);
        libraryManager.deletePlaylist(playlist);
        playlistList.remove(position);
        playlistTitles.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Плейлист удален", Toast.LENGTH_SHORT).show();
    }
}
