package com.light.museumguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsFragment extends Fragment {
    public static boolean isNetworkError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ListView list = view.findViewById(R.id.news_list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), MainActivity.dataFromSite, R.layout.news_view_layout, new String[]{"Zag", "Date", "News"}, new int[]{R.id.textZagol, R.id.textDate, R.id.textNews});
        list.setAdapter(simpleAdapter);
        TextView txt = view.findViewById(R.id.textView);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("http://sibmuseum.ru/news");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);
            }
        });
        TextView errText = view.findViewById(R.id.networkErrorText);
        if (isNetworkError) {
            errText.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            errText.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
