package com.gudwns999.smartkeyapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Kim on 2016-11-07.
 */

public class BlackBox extends Activity {
    private static final String MOVIE_URL = "http://hooni.net/s_512kb.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blackbox);

        VideoView videoView = (VideoView) findViewById(R.id.VideoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        // Set video link (mp4 format )
        Uri video = Uri.parse(MOVIE_URL);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.start();

    }
}