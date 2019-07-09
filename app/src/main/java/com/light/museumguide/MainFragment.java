package com.light.museumguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {
    AlertDialog.Builder isBlindBuilder;
    AlertDialog isBlindDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
//        isBlindBuilder = new AlertDialog.Builder(fragmentView.getContext())
//                .setTitle("Режим для слабовидящих")
//                .setCancelable(false)
//                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        isBlindDialog.cancel();
//                        MainActivity mainActivity = new MainActivity();
//                        MainActivity.isFirstEntry = false;
//                        mainActivity.setSharedPreferencesState(MainActivity.isFirstEntrySP,false);
//                    }
//                })
//                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        isBlindDialog.cancel();
//                    }
//                })
//                .setMessage("Хотите включить режим для слабовидящих?");
//        isBlindDialog = isBlindBuilder.create();
//        if(MainActivity.isFirstRun) {
//            isBlindDialog.show();
//        }
        Button newsButton = fragmentView.findViewById(R.id.newsButton);
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.id = R.id.nav_news;
                ((MainActivity)getActivity()).replaceFragments();
            }
        });
        Button contactsButton = fragmentView.findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.id = R.id.nav_contacts;
                ((MainActivity)getActivity()).replaceFragments();
            }
        });
        Button scanqrButton = fragmentView.findViewById(R.id.scanqrButton);
        scanqrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanningBarcodeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Button mapButton = fragmentView.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.id = R.id.nav_map;
                ((MainActivity)getActivity()).replaceFragments();
            }
        });
        Button expoButton = fragmentView.findViewById(R.id.expoButton);
        expoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        Button aboutButton = fragmentView.findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.id = R.id.nav_about;
                ((MainActivity)getActivity()).replaceFragments();
            }
        });

        return fragmentView;
    }


}
