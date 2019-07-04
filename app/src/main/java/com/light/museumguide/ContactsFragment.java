package com.light.museumguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends Fragment {
    TextView tv;
    ImageView logo;
    private int easterEggCounter;
    private View fragmentView;
    ImageView vk;
    ImageView instagram;
    ImageView twitter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        easterEggCounter = 0;
        fragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        logo = fragmentView.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easterEggCounter++;
                if(easterEggCounter==3){
                    Toast.makeText(getContext(), "Хм... Кажется тут что-то есть.", Toast.LENGTH_SHORT).show();
                }
                if(easterEggCounter==5) {
                    Toast.makeText(fragmentView.getContext(), "Вы нашли пасхалку. Приятного пользования by AlexLightovich :)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), EasterActivity.class);
                    startActivity(intent);
                }
            }
        });
        tv = fragmentView.findViewById(R.id.textContacts);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri gmmIntentUri = Uri.parse("geo:54.980051,73.378484");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Омский государственный историко - краеведческий музей");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
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
                Uri address = Uri.parse("https://vk.com/ogikmuseum");
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

    @Override
    public void onStop() {
        super.onStop();
        easterEggCounter = 0;
    }
}
