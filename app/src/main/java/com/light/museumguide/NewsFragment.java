package com.light.museumguide;

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

public class NewsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ListView list = view.findViewById(R.id.news_list);
        ArrayList<Map<String,Object>> list1 = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        map.put("First","");
        map.put("FirstTXT", getResources().getString(R.string.first_expo));
        list1.add(map);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), list1, android.R.layout.simple_list_item_2, new String[]{"First","FirstTXT", "Second","SecondTXT"}, new int[]{R.id.icon,R.id.text1});
        list.setAdapter(simpleAdapter);
        return view;
    }
}
