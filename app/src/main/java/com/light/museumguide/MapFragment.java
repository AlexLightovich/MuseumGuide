package com.light.museumguide;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.Touch;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ortiz.touchview.TouchImageView;


public class MapFragment extends Fragment {
    private TouchImageView img;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Button firstFloorBtn = view.findViewById(R.id.button1floor);
        Button secondFloorBtn = view.findViewById(R.id.button2floor);
        img = view.findViewById(R.id.touchImageView);
        firstFloorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.ic_twitter);
            }
        });
        secondFloorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.ic_instagram);
            }
        });
        return view;

    }
}
