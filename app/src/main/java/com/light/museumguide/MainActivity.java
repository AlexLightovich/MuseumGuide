package com.light.museumguide;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainFragment mainFragment;
    private ContactsFragment contactsFragment;
    private GalleryFragment galleryFragment;
    private NewsFragment newsFragment;
    private MapFragment mapFragment;
    private LockFragment lockFragment;
    private boolean isMainVisible;
    public static int id;
    private boolean isNewsVisible;
    public static boolean isFirstExpoScannedH;
    public static boolean isSecondExpoScannedH;
    private boolean isMapVisible;
    private boolean isContactVisible;
    private boolean isQRScanned;
    private boolean isGalleryVisible;
    private FloatingActionButton fab;
    private FloatingActionButton fabMap;
    public static final String APP_PREFERENCES = "isqrscanned";
    public static final String isFirstExpoScanned = "is1exposcanned";
    public static final String isSecondExpoScanned = "is2exposcanned";
    public static final String isWithWayCheck = "iswithway";
    SharedPreferences sPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setTheme(R.style.AppTheme_NoActionBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainFragment = new MainFragment();
        contactsFragment = new ContactsFragment();
        galleryFragment = new GalleryFragment();
        mapFragment = new MapFragment();
        lockFragment = new LockFragment();
        newsFragment = new NewsFragment();
        isMainVisible = true;
        isContactVisible = false;
        isMapVisible = false;
        isGalleryVisible = false;
        isNewsVisible = false;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanningBarcodeActivity.class);
                startActivity(intent);

            }
        });
        fabMap = (FloatingActionButton) findViewById(R.id.fab1);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        fab.show();
        fabMap.hide();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        sPref = getSharedPreferences("qrscan",MODE_PRIVATE);
        isQRScanned = sPref.getBoolean(MainActivity.APP_PREFERENCES, false);
        MapFragment.isWithWay = sPref.getBoolean(isWithWayCheck, false);
        System.out.println(isQRScanned);
        if (isQRScanned) {
            Toast.makeText(this, "QR SCANNED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "QR NOT SCANNED", Toast.LENGTH_SHORT).show();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(mainFragment,"DFS");
        fragmentTransaction.replace(R.id.include, mainFragment);
        fragmentTransaction.commit();
        if(id==R.id.nav_manage){
            if (isMapVisible) {

            } else {
                if(isQRScanned) {
                    supportFragmentManager = getSupportFragmentManager();
                    fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, mapFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isMapVisible = true;
                    isContactVisible = false;
                    isNewsVisible = false;
                    isGalleryVisible = false;
                    fab.show();
                    fabMap.show();
                }
                else{
                    supportFragmentManager = getSupportFragmentManager();
                    fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, lockFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isMapVisible = true;
                    isContactVisible = false;
                    isNewsVisible = false;
                    isGalleryVisible = false;
                    fab.show();
                    fabMap.hide();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (isMainVisible) {

            } else {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.include, mainFragment);
//                fragmentTransaction.add(R.id.include, mainFragment);
                fragmentTransaction.commit();
                isMainVisible = true;
                isMapVisible = false;
                isContactVisible = false;
                isGalleryVisible = false;
                isNewsVisible = false;
                fab.show();
                fabMap.hide();

            }

        } else if (id == R.id.nav_gallery) {
            if (!isGalleryVisible) {
                if (isQRScanned) {
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, galleryFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isContactVisible = false;
                    isGalleryVisible = true;
                    isGalleryVisible = false;
                    isMapVisible = false;
                    fab.show();
                    fabMap.hide();
                }else{
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, lockFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isContactVisible = false;
                    isGalleryVisible = true;
                    isGalleryVisible = false;
                    isMapVisible = false;
                    fab.show();
                    fabMap.hide();
                }
            }
        } else if (id == R.id.nav_slideshow) {

            if (isNewsVisible) {

            } else {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.include,newsFragment);
//                fragmentTransaction.add(R.id.include, contactsFragment);
                fragmentTransaction.commit();
                isMainVisible = false;
                isContactVisible = true;
                isGalleryVisible = false;
                isNewsVisible = true;
                isMapVisible = false;
                fab.hide();
                fabMap.hide();
            }
        } else if (id == R.id.nav_manage) {
            if (isMapVisible) {

            } else {
                if(isQRScanned) {
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, mapFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isMapVisible = true;
                    isContactVisible = false;
                    isNewsVisible = false;
                    isGalleryVisible = false;
                    fab.show();
                    fabMap.show();
                }
                else{
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, lockFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isMapVisible = true;
                    isContactVisible = false;
                    isNewsVisible = false;
                    isGalleryVisible = false;
                    fab.show();
                    fabMap.hide();
                }
            }

        } else if (id == R.id.nav_contacts) {
            if (isContactVisible) {

            } else {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.include,contactsFragment);
//                fragmentTransaction.add(R.id.include, contactsFragment);
                fragmentTransaction.commit();
                isMainVisible = false;
                isMapVisible = false;
                isContactVisible = true;
                isNewsVisible = false;
                isGalleryVisible = false;
                fab.hide();
                fabMap.hide();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class MyConnection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String content = "";
            try {
                URL url = new URL("https://forum.advance-rp.ru/");
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection connection = (HttpURLConnection) urlConnection;
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                while(scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    content += s;

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(content);
            return null;
        }
    }
}
