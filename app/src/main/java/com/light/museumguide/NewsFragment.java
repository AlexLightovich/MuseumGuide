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
    public static String firstTitle;
    public static String firstDate;
    public static String firstText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ListView list = view.findViewById(R.id.news_list);
//        ArrayList<Map<String,Object>> list1 = new ArrayList<>();
//        HashMap<String,Object> map = new HashMap<>();
//        map.put("Zag",firstTitle);
//        map.put("Date", firstDate);
//        map.put("News",firstText);
//        list1.add(map);
//        HashMap<String,Object> map1 = new HashMap<>();
//        map1.put("Zag","Заголовок 2");
//        map1.put("Date", "14.04.19");
//        map1.put("News","НОВОСТЬ 2");
//        list1.add(map1);
//        HashMap<String,Object> map2 = new HashMap<>();
//        map2.put("Zag","Заголовок 3");
//        map2.put("Date", "15.04.19");
//        map2.put("News","НОВОСТЬ 3");
//        list1.add(map2);
//        HashMap<String,Object> map3 = new HashMap<>();
//        map3.put("Zag","Заголовок 4");
//        map3.put("Date", "16.04.19");
//        map3.put("News","НОВОСТЬ 4");
//        list1.add(map3);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), MainActivity.dataFromSite, R.layout.news_view_layout, new String[]{"Zag","Date", "News"}, new int[]{R.id.textZagol,R.id.textDate,R.id.textNews});
        list.setAdapter(simpleAdapter);
        return view;
    }
}
