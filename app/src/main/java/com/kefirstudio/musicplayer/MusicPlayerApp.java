package com.kefirstudio.musicplayer;

import android.app.Application;

public class MusicPlayerApp extends Application {
    private LibraryManager libraryManager;

    @Override
    public void onCreate() {
        super.onCreate();
        libraryManager = new LibraryManager(this);
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
