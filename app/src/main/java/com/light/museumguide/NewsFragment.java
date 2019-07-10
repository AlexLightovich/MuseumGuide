package com.light.museumguide;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.UpdateAppearance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class NewsFragment extends Fragment {


    public static boolean isNetworkError;
    private TextView txt2;
    private TextView errText;
    private TextView txt;
    private ListView list;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        list = view.findViewById(R.id.news_list);
        SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(), MainActivity.dataFromSite, R.layout.news_view_layout, new String[]{"Zag", "Date", "News"}, new int[]{R.id.textZagol, R.id.textDate, R.id.textNews});
        list.setAdapter(simpleAdapter);
        txt = view.findViewById(R.id.allNewsText);
        txt2 = view.findViewById(R.id.textView);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("http://sibmuseum.ru/news");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);
            }
        });
        errText = view.findViewById(R.id.networkErrorText);
        checkNetworkError();
        return view;
    }
    public void checkNetworkError() {
        if (isNetworkError) {
            errText.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            txt2.setVisibility(View.INVISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            errText.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }
}
