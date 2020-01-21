package com.example.authapplication;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<History> {

    private Activity context;
    List<History> artists;

    public HistoryListAdapter(Activity context, ArrayList<History> artists) {
        super(context, R.layout.activity_show_history, artists);
        this.context = context;
        this.artists = artists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.history_view_layout, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = listViewItem.findViewById(R.id.textViewUrl);

        History artist = artists.get(position);
        textViewName.setText(artist.getDomain());
        textViewGenre.setText(artist.getUrl());

        return listViewItem;
    }
}