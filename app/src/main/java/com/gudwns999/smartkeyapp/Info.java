package com.gudwns999.smartkeyapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kim on 2016-11-06.
 */

public class Info extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView listView=(ListView)findViewById(R.id.listView);

        ArrayList<ListViewItem> data=new ArrayList<>();
        for(int i=0;i<4;i++) {
            ListViewItem lion = new ListViewItem(R.drawable.infob, "Lion", "ss");
            data.add(lion);
        }
        ListViewAdapter adapter=new ListViewAdapter(this,R.layout.info_item, data);
        listView.setAdapter(adapter);

    }
}
