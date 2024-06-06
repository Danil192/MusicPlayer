package com.kefirstudio.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddTrackActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextArtist;
    private EditText editTextAlbum;
    private EditText editTextDuration;
    private EditText editTextTrackPath;
    private Button buttonAddTrack;
    private Button buttonChooseFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextArtist = findViewById(R.id.editTextArtist);
        editTextAlbum = findViewById(R.id.editTextAlbum);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextTrackPath = findViewById(R.id.editTextTrackPath);
        buttonAddTrack = findViewById(R.id.buttonAddTrack);
        buttonChooseFile = findViewById(R.id.buttonChooseFile);

        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrack();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String trackPath = data.getData().getPath();
            editTextTrackPath.setText(trackPath);
        }
    }

    private void addTrack() {
        String title = editTextTitle.getText().toString();
        String artist = editTextArtist.getText().toString();
        String album = editTextAlbum.getText().toString();
        long duration = Long.parseLong(editTextDuration.getText().toString());
        String trackPath = editTextTrackPath.getText().toString();

        // Создаем новый трек без идентификатора
        Track newTrack = new Track(title, artist, album, duration, 0, trackPath);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("newTrack", newTrack);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
