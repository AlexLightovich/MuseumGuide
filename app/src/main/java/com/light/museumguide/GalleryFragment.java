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
        ArrayList<Map<String,Object>> list1 = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        map.put("First",R.drawable.expoimage1);
        map.put("FirstTXT", getResources().getString(R.string.first_expo));
        list1.add(map);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), list1, R.layout.gallery_layout, new String[]{"First","FirstTXT", "Second","SecondTXT"}, new int[]{R.id.icon,R.id.text1});
        list.setAdapter(simpleAdapter);


        return view;
    }
}
