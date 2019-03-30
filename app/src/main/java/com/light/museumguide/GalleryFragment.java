package com.light.museumguide;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ListView list = view.findViewById(R.id.list_view);
        ArrayList<Map<String,Integer>> list1 = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<>();
        map.put("First",R.drawable.expoimage1);
        map.put("FirstTXT", 111112212);
        list1.add(map);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), list1, android.R.layout.activity_list_item, new String[]{"First","FirstTXT"}, new int[]{android.R.id.icon,android.R.id.text1});
        list.setAdapter(simpleAdapter);


        return view;
    }
}
