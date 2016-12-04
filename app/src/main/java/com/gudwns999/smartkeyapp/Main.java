package com.gudwns999.smartkeyapp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class Main extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Splash에서 넘어온 값 받기
        TabHost tabhost = getTabHost();

        TabHost.TabSpec spec;
        Intent intent;
        Resources res = getResources();

        intent = new Intent().setClass(this, Key.class);
        spec = tabhost.newTabSpec("Key!");
        spec.setIndicator("Key", res.getDrawable(R.drawable.keyb));
        spec.setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, Map.class);
        spec = tabhost.newTabSpec("Map!");
        spec.setIndicator("Map", res.getDrawable(R.drawable.mapb));
        spec.setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, Parking.class);
        spec = tabhost.newTabSpec("Parking!");
        spec.setIndicator("Parking", res.getDrawable(R.drawable.parkingb));
        spec.setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, Info.class);
        spec = tabhost.newTabSpec("Info!");
        spec.setIndicator("Info", res.getDrawable(R.drawable.infob));
        spec.setContent(intent);
        tabhost.addTab(spec);

    }
}
