package com.light.museumguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        Button howscanbtn = fragmentView.findViewById(R.id.htsBtn);
        howscanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragmentView.getContext(), HowScanActivity.class);
                startActivity(intent);
            }
        });
        Button sqBtn = fragmentView.findViewById(R.id.sqBtn);
        sqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragmentView.getContext(),ScanningBarcodeActivity.class);
                startActivity(intent);
            }
        });

        return fragmentView;
    }


}
