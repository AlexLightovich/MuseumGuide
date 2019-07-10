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
import android.widget.Toast;


public class MainFragment extends Fragment {
    AlertDialog.Builder setTextBuilder;
    AlertDialog setTextDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        setTextBuilder = new AlertDialog.Builder(fragmentView.getContext())
                .setTitle("Изменение размера текста")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTextDialog.cancel();
                        MainActivity mainActivity = new MainActivity();
                        MainActivity.isFirstRun = false;
                        mainActivity.setSharedPreferencesState(MainActivity.isFirstRunSP,false);
                        Intent intent = new Intent(fragmentView.getContext(), SetTextSizeActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTextDialog.cancel();
                        MainActivity mainActivity = new MainActivity();
                        MainActivity.isFirstRun = false;
                        mainActivity.setSharedPreferencesState(MainActivity.isFirstRunSP,false);
                        Toast.makeText(fragmentView.getContext(), "В любой момент вы сможете изменить размер текста, нажав на три точки в правом верхнем углу.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setMessage("Добро пожаловать в Museum Guide!\n Хотите ли вы изменить размер текста под Ваше устройство?");
        setTextDialog = setTextBuilder.create();
        if(MainActivity.isFirstRun) {
            setTextDialog.show();
        }
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
                getActivity().finish();
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
