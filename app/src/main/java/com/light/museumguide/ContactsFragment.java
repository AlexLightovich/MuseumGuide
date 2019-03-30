package com.light.museumguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsFragment extends Fragment {
    TextView tv;
    ImageView vk;
    ImageView instagram;
    ImageView twitter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        tv = fragmentView.findViewById(R.id.textContacts);
        TextView phone = fragmentView.findViewById(R.id.textContacts2);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+73812314747"));
                startActivity(intent);
            }
        });
        vk = fragmentView.findViewById(R.id.imageView4);
        vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://vk.com/ogikmuseum/");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);

            }
        });
        instagram = fragmentView.findViewById(R.id.imageView5);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://instagram.com/ogik_museum/");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);
            }
        });
        twitter = fragmentView.findViewById(R.id.imageView6);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://twitter.com/OgikMuseum");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);
            }
        });
        return fragmentView;
    }
    public void isVisibling(boolean b) {
        if(b){
            tv.setVisibility(View.VISIBLE);
        }else{
            tv.setVisibility(View.INVISIBLE);
        }
    }
}
