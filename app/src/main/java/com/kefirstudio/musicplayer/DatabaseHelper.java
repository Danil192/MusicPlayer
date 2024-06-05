package com.kefirstudio.musicplayer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "musicplayer.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TRACKS = "tracks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_ALBUM = "album";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_TRACK_PATH = "track_path";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableTracks = "CREATE TABLE " + TABLE_TRACKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_ARTIST + " TEXT, " +
                COLUMN_ALBUM + " TEXT, " +
                COLUMN_DURATION + " INTEGER, " +
                COLUMN_TRACK_PATH + " TEXT" +
                ")";
        db.execSQL(createTableTracks);

        // Скопируйте базу данных в нужный каталог
        copyDatabaseToExternalStorage();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        onCreate(db);
    }

    public void addTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, track.getTitle());
        values.put(COLUMN_ARTIST, track.getArtist());
        values.put(COLUMN_ALBUM, track.getAlbum());
        values.put(COLUMN_DURATION, track.getDuration());
        values.put(COLUMN_TRACK_PATH, track.getTrackPath());
        db.insert(TABLE_TRACKS, null, values);
        db.close();
    }

    public List<Track> getAllTracks() {
        List<Track> trackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRACKS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Track track = new Track(
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ALBUM)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_DURATION)),
                        0, // playTime is not stored in this example
                        cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_PATH))
                );
                trackList.add(track);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trackList;
    }

    public void deleteTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_TRACKS, COLUMN_TITLE + " = ? AND " + COLUMN_ARTIST + " = ?", new String[]{track.getTitle(), track.getArtist()});
        db.close();
        Log.d("DatabaseHelper", "Deleted rows: " + deletedRows);  // Отладочная информация
    }

    private void copyDatabaseToExternalStorage() {
        File dbFile = new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + DATABASE_NAME);
        File externalDbFile = new File("C:/Users/mykad/AndroidStudioProjects/MusicPlayer/" + DATABASE_NAME);

        try (FileChannel src = new FileInputStream(dbFile).getChannel();
             FileChannel dst = new FileOutputStream(externalDbFile).getChannel()) {
            dst.transferFrom(src, 0, src.size());
        } catch (IOException e) {
            Log.e("DatabaseHelper", "Error copying database to external storage", e);
        }
    }
}
