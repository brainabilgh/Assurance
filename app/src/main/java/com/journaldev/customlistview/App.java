package com.journaldev.customlistview;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by Nabil on 27/05/2017.
 */

public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(R.id.glide_tag);
    }
}