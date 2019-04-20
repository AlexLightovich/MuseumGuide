package com.light.museumguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ortiz.touchview.TouchImageView;


public class MapFragment extends Fragment {
    private TouchImageView img;
    public static boolean isWithWay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Button firstFloorBtn = view.findViewById(R.id.button1floor);
        Button secondFloorBtn = view.findViewById(R.id.button2floor);
        System.out.println(new HistoryActivity().list1);
        img = view.findViewById(R.id.touchImageView);
        if(isWithWay) {
            img.setImageResource(R.drawable.ic_entry_hall_withway);
            firstFloorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img.setImageResource(R.drawable.ic_entry_hall_withway);
                }
            });
            secondFloorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img.setImageResource(R.drawable.ic_main_hall_withway);
                }
            });
        } else {
            img.setImageResource(R.drawable.ic_entry_hall_withoutway);
                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_entry_hall_withoutway);
                    }
                });
                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_main_hall_withoutway);
                    }
                });
        }
        return view;

    }
}
