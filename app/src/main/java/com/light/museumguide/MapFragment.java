package com.light.museumguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ortiz.touchview.TouchImageView;


public class MapFragment extends Fragment {
    private TouchImageView img;
    public static boolean isOrganScanned;
    private AlertDialog.Builder welcomeBuilder;
    private AlertDialog welcomeDialog;
    public static boolean isKobizScanned;
    public static boolean isVarganScanned;
    public static boolean isDombraScanned;
    public static boolean isMansiScanned;
    public static boolean isYurtaScanned;
    public static boolean isArmyanScanned;
    public static boolean isWithWay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Button firstFloorBtn = view.findViewById(R.id.button1floor);
        Button secondFloorBtn = view.findViewById(R.id.button2floor);
        System.out.println(MainActivity.isFirstEntry);
        img = view.findViewById(R.id.touchImageView);
        welcomeBuilder = new AlertDialog.Builder(view.getContext())
                .setTitle("Добро пожаловать!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        welcomeDialog.cancel();
                        MainActivity mainActivity = new MainActivity();
                        MainActivity.isFirstEntry = false;
                        mainActivity.setSharedPreferencesState(MainActivity.isFirstEntrySP,false);
                    }
                })
                .setMessage(R.string.welcome_dialog);
        welcomeDialog = welcomeBuilder.create();
        if(MainActivity.isFirstEntry) {
            welcomeDialog.show();
        }
        if(isWithWay) {
            if(!isDombraScanned && !isOrganScanned && !isVarganScanned && !isKobizScanned && !isYurtaScanned && !isMansiScanned && !isArmyanScanned) {
                img.setImageResource(R.drawable.ic_entry_hall_withway);
                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_entry_hall_withway);
                    }
                });
                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_main_hall_withway);
                    }
                });
            }
// else if(!isDombraScanned && !isVarganScanned && !isKobizScanned && isOrganScanned && !isYurtaScanned && !isMansiScanned && !isArmyanScanned){
//                img.setImageResource(R.drawable.ic_switho);
//                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_entry_hall_withway);
//                    }
//                });
//                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_switho);
//                    }
//                });
//            }else if(!isDombraScanned && !isVarganScanned && !isKobizScanned && isOrganScanned && isYurtaScanned && !isMansiScanned && !isArmyanScanned){
//                img.setImageResource(R.drawable.ic_swithoy);
//                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_entry_hall_withway);
//                    }
//                });
//                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_swithoy);
//                    }
//                });
//            }else if(!isVarganScanned && (isKobizScanned || isDombraScanned) && isOrganScanned && isYurtaScanned && !isMansiScanned && !isArmyanScanned){
//                img.setImageResource(R.drawable.ic_swithoyk);
//                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_entry_hall_withway);
//                    }
//                });
//                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_swithoyk);
//                    }
//                });
//            }else if(isVarganScanned && (isKobizScanned || isDombraScanned) && isOrganScanned && isYurtaScanned && !isMansiScanned && !isArmyanScanned){
//                img.setImageResource(R.drawable.ic_swithoykv);
//                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_entry_hall_withway);
//                    }
//                });
//                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_swithoykv);
//                    }
//                });
//            } else if(isVarganScanned && (isKobizScanned || isDombraScanned) && isOrganScanned && isYurtaScanned && !isMansiScanned && isArmyanScanned){
//                img.setImageResource(R.drawable.ic_swithoykva);
//                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_entry_hall_withway);
//                    }
//                });
//                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img.setImageResource(R.drawable.ic_swithoykva);
//                    }
//                });
            else if(isVarganScanned && (isKobizScanned || isDombraScanned) && isOrganScanned && isYurtaScanned && isMansiScanned && isArmyanScanned){
                img.setImageResource(R.drawable.ic_swithoykva);
                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_ewithall);
                    }
                });
                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_swithoykva);
                    }
                });
            } else {
                img.setImageResource(R.drawable.ic_entry_hall_withway);
                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_entry_hall_withway);
                    }
                });
                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_main_hall_withway);
                    }
                });
            }
        } else {
            if(!isDombraScanned && !isOrganScanned && !isVarganScanned && !isKobizScanned && !isYurtaScanned && !isMansiScanned && !isArmyanScanned) {
                img.setImageResource(R.drawable.ic_entry_hall_withoutway);
                firstFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_entry_hall_withoutway);
                    }
                });
                secondFloorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img.setImageResource(R.drawable.ic_main_hall_withoutway);
                    }
                });
            }
        }
        return view;

    }
}
