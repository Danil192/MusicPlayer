package com.kefirstudio.musicplayer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "musicplayer.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_TRACKS = "tracks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_ALBUM = "album";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_TRACK_PATH = "track_path";

    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String COLUMN_PLAYLIST_ID = "playlist_id";
    private static final String COLUMN_PLAYLIST_NAME = "playlist_name";

    private static final String TABLE_PLAYLIST_TRACKS = "playlist_tracks";
    private static final String COLUMN_PLAYLIST_TRACK_ID = "playlist_track_id";
    private static final String COLUMN_PLAYLIST_ID_FK = "playlist_id";
    private static final String COLUMN_TRACK_ID_FK = "track_id";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating database tables");
        String createTableTracks = "CREATE TABLE " + TABLE_TRACKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_ARTIST + " TEXT, " +
                COLUMN_ALBUM + " TEXT, " +
                COLUMN_DURATION + " INTEGER, " +
                COLUMN_TRACK_PATH + " TEXT" +
                ")";
        db.execSQL(createTableTracks);

        String createTablePlaylists = "CREATE TABLE " + TABLE_PLAYLISTS + " (" +
                COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYLIST_NAME + " TEXT" +
                ")";
        db.execSQL(createTablePlaylists);

        String createTablePlaylistTracks = "CREATE TABLE " + TABLE_PLAYLIST_TRACKS + " (" +
                COLUMN_PLAYLIST_TRACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYLIST_ID_FK + " INTEGER, " +
                COLUMN_TRACK_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_PLAYLIST_ID_FK + ") REFERENCES " + TABLE_PLAYLISTS + "(" + COLUMN_PLAYLIST_ID + "), " +
                "FOREIGN KEY(" + COLUMN_TRACK_ID_FK + ") REFERENCES " + TABLE_TRACKS + "(" + COLUMN_ID + ")" +
                ")";
        db.execSQL(createTablePlaylistTracks);
        Log.d("DatabaseHelper", "Database tables created");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST_TRACKS);
        onCreate(db);
    }


    public void addTrack(Track track) {
        if (!trackExists(track)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, track.getTitle());
            values.put(COLUMN_ARTIST, track.getArtist());
            values.put(COLUMN_ALBUM, track.getAlbum());
            values.put(COLUMN_DURATION, track.getDuration());
            values.put(COLUMN_TRACK_PATH, track.getTrackPath());
            db.insert(TABLE_TRACKS, null, values);
            db.close();
        } else {
            Log.d("DatabaseHelper", "Track already exists: " + track.getTitle());
        }
    }

    public List<Track> getAllTracks() {
        List<Track> trackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRACKS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Track track = new Track(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
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
        int deletedRows = db.delete(TABLE_TRACKS, COLUMN_ID + " = ?", new String[]{String.valueOf(track.getId())});
        db.close();
        Log.d("DatabaseHelper", "Deleted rows: " + deletedRows);
    }

    public void addPlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_NAME, playlist.getTitle());
        long playlistId = db.insert(TABLE_PLAYLISTS, null, values);
        for (Track track : playlist.getTrackList()) {
            addTrackToPlaylist((int) playlistId, track.getId());
        }
        db.close();
    }

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlistList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYLISTS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Playlist playlist = new Playlist(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYLIST_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PLAYLIST_NAME)),
                        getTracksByPlaylist(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYLIST_ID)))
                );
                playlistList.add(playlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return playlistList;
    }

    public void addTrackToPlaylist(int playlistId, int trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_ID_FK, playlistId);
        values.put(COLUMN_TRACK_ID_FK, trackId);
        db.insert(TABLE_PLAYLIST_TRACKS, null, values);
        db.close();
    }

    public List<Track> getTracksByPlaylist(int playlistId) {
        List<Track> trackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.* FROM " + TABLE_TRACKS + " t " +
                "JOIN " + TABLE_PLAYLIST_TRACKS + " pt ON t." + COLUMN_ID + " = pt." + COLUMN_TRACK_ID_FK +
                " WHERE pt." + COLUMN_PLAYLIST_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(playlistId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Track track = new Track(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
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

    @SuppressLint("Range")
    public int getPlaylistIdByName(String playlistName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PLAYLIST_ID + " FROM " + TABLE_PLAYLISTS + " WHERE " + COLUMN_PLAYLIST_NAME + " = ?", new String[]{playlistName});
        int playlistId = -1;
        if (cursor.moveToFirst()) {
            playlistId = cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYLIST_ID));
        }
        cursor.close();
        db.close();
        return playlistId;
    }
    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST_TRACKS, COLUMN_PLAYLIST_ID_FK + " = ? AND " + COLUMN_TRACK_ID_FK + " = ?", new String[]{String.valueOf(playlistId), String.valueOf(trackId)});
        db.close();
    }
    public void deletePlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST_TRACKS, COLUMN_PLAYLIST_ID_FK + " = ?", new String[]{String.valueOf(playlist.getId())});
        db.delete(TABLE_PLAYLISTS, COLUMN_PLAYLIST_ID + " = ?", new String[]{String.valueOf(playlist.getId())});
        db.close();
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean trackExists(Track track) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_TRACKS + " WHERE " +
                        COLUMN_TITLE + " = ? AND " +
                        COLUMN_ARTIST + " = ? AND " +
                        COLUMN_ALBUM + " = ? AND " +
                        COLUMN_TRACK_PATH + " = ?",
                new String[]{track.getTitle(), track.getArtist(), track.getAlbum(), track.getTrackPath()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}
