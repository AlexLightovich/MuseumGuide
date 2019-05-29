package com.light.museumguide;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    public static boolean isFirstEntry;
    private AboutFragment aboutFragment;
    private boolean isMainVisible;
    public static int id;
    private boolean isNewsVisible;
    public static boolean isFirstExpoScannedH;
    public static boolean isSecondExpoScannedH;
    public static boolean isYurtaScannedH;
    public static boolean isMansiScannedH;
    public static boolean isAllExpoH;
    public static boolean isKobizScannedH;
    public static boolean isDombraScannedH;
    public static boolean isOrganScannedH;
    public static boolean isVarganScannedH;
    public static boolean isArmyanScannedH;
    private boolean isMapVisible;
    private boolean isContactVisible;
    private boolean isAboutVisible;
    public static boolean isQRScanned;
    private boolean isGalleryVisible;
    private FloatingActionButton fab;
    private FloatingActionButton fabMap;
    public static ArrayList<HashMap<String, String>> dataFromSite;
    public static HashMap<String, String> linkToNews = new HashMap<>();
    public static final String APP_PREFERENCES = "isqrscanned";
    public static final String isFirstEntrySP = "isfirstentry";
    public static final String isFirstExpoScanned = "is1exposcanned";
    public static final String isKobizScanned = "isKobScan";
    public static final String isDombraScanned = "isDombraScan";
    public static final String isYurtaScanned = "isYurtScan";
    public static final String isArmyanScanned = "isBlgvnScan";
    public static final String isMansiScanned = "isMansiScan";
    public static final String isOrganScanned = "isOrgScan";
    public static final String isVarganScanned = "isVargScan";
    public static final String isKobizRate = "isKobRate";
    public static final String isDombraRate = "isDombraRate";
    public static final String isAllExpo = "isAllExpo";
    public static final String isOrganRate = "isOrgRate";
    public static final String isYurtaRate = "isYurtRate";
    public static final String isMansiRate = "isMansiRate";
    public static final String isVarganRate = "isVargRate";
    public static final String isArmyanRate = "isVargRate";
    public static final String isSecondExpoScanned = "is2exposcanned";
    public static final String isWithWayCheck = "iswithway";
    public static String ANDROID_ID;
    public static SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mainFragment = new MainFragment();
        aboutFragment = new AboutFragment();
        contactsFragment = new ContactsFragment();
        galleryFragment = new GalleryFragment();
        mapFragment = new MapFragment();
        lockFragment = new LockFragment();
        newsFragment = new NewsFragment();
        isMainVisible = true;
        isContactVisible = false;
        isMapVisible = false;
        isAboutVisible = false;
        isGalleryVisible = false;
        isNewsVisible = false;
        MyConnection myConnection = new MyConnection();
        myConnection.execute();
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
        sPref = getSharedPreferences("qrscan", MODE_PRIVATE);
        isQRScanned = sPref.getBoolean(MainActivity.APP_PREFERENCES, false);
        isFirstEntry = sPref.getBoolean(isFirstEntrySP,true);
        isAllExpoH = sPref.getBoolean(isAllExpo, false);
        MapFragment.isOrganScanned = sPref.getBoolean(isOrganScanned, false);
        MapFragment.isKobizScanned = sPref.getBoolean(isKobizScanned, false);
        MapFragment.isVarganScanned = sPref.getBoolean(isVarganScanned, false);
        MapFragment.isDombraScanned = sPref.getBoolean(isDombraScanned, false);
        MapFragment.isYurtaScanned = sPref.getBoolean(isYurtaScanned, false);
        MapFragment.isMansiScanned = sPref.getBoolean(isMansiScanned, false);
        MapFragment.isArmyanScanned = sPref.getBoolean(isArmyanScanned, false);
        MapFragment.isWithWay = sPref.getBoolean(isWithWayCheck, false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(mainFragment, "DFS");
        fragmentTransaction.replace(R.id.include, mainFragment);
        fragmentTransaction.commit();
        if (id == R.id.nav_manage) {
            if (isMapVisible) {

            } else {
                if (isQRScanned) {
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
                } else {
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
    public void setFirstEntryState(boolean state) {
        isFirstEntry = state;
        SharedPreferences sharedPreferences = sPref;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(isFirstEntrySP, state);
        edit.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    protected void newsClick(View view) {
        TextView txtTitle = view.findViewById(R.id.textZagol);
        String link = MainActivity.linkToNews.get(txtTitle.getText());
        Uri address = Uri.parse(link);
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openLinkIntent);
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
                fragmentTransaction.commit();
                isMainVisible = true;
                isMapVisible = false;
                isContactVisible = false;
                isGalleryVisible = false;
                isAboutVisible = false;
                isNewsVisible = false;
                fab.show();
                fabMap.hide();

            }

        } else if (id == R.id.nav_slideshow) {

            if (isNewsVisible) {

            } else {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.include, newsFragment);
                fragmentTransaction.commit();
                isMainVisible = false;
                isContactVisible = false;
                isAboutVisible = false;
                isGalleryVisible = false;
                isNewsVisible = true;
                isMapVisible = false;
                fab.hide();
                fabMap.hide();
            }
        } else if (id == R.id.nav_manage) {
            if (isMapVisible) {

            } else {
                if (isQRScanned) {
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, mapFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isMapVisible = true;
                    isContactVisible = false;
                    isAboutVisible = false;
                    isNewsVisible = false;
                    isGalleryVisible = false;
                    fab.show();
                    fabMap.show();
                } else {
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.include, lockFragment);
                    fragmentTransaction.commit();
                    isMainVisible = false;
                    isMapVisible = true;
                    isContactVisible = false;
                    isAboutVisible = false;
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
                fragmentTransaction.replace(R.id.include, contactsFragment);
                fragmentTransaction.commit();
                isMainVisible = false;
                isMapVisible = false;
                isAboutVisible = false;
                isContactVisible = true;
                isNewsVisible = false;
                isGalleryVisible = false;
                fab.hide();
                fabMap.hide();
            }
        } else if (id == R.id.nav_about) {
            if (isAboutVisible) {

            } else {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.include, aboutFragment);
                fragmentTransaction.commit();
                isMainVisible = false;
                isMapVisible = false;
                isContactVisible = false;
                isNewsVisible = false;
                isAboutVisible = true;
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
            Document doc = null;
            dataFromSite = new ArrayList<>();
            dataFromSite.add(new HashMap<String, String>());
            dataFromSite.add(new HashMap<String, String>());
            dataFromSite.add(new HashMap<String, String>());
            dataFromSite.add(new HashMap<String, String>());
            try {
                doc = Jsoup.connect("http://sibmuseum.ru/").get();
                Elements newsHeadlines = doc.select("li.views-row > span:nth-child(1) > span:nth-child(1) > a:nth-child(1)");
                for (int i = 0; i < newsHeadlines.size(); i++) {
                    Element headline = newsHeadlines.get(i);
                    HashMap<String, String> hashMap = dataFromSite.get(i);
                    String text = headline.text();
                    hashMap.put("Zag", text);
                    String href = headline.absUrl("href");
                    linkToNews.put(text, href);

                }
                Elements newsDatalines = doc.select("li.views-row > div:nth-child(3) > div:nth-child(1) > span:nth-child(1)");
                for (int i = 0; i < newsDatalines.size(); i++) {
                    Element headline = newsDatalines.get(i);
                    HashMap<String, String> hashMap = dataFromSite.get(i);
                    String text = headline.text();
                    hashMap.put("Date", text);
                }
                Elements newsTextlines = doc.select("li.views-row > div:nth-child(4) > div:nth-child(1) > p:nth-child(1)");
                for (int i = 0; i < newsTextlines.size(); i++) {
                    Element headline = newsTextlines.get(i);
                    HashMap<String, String> hashMap = dataFromSite.get(i);
                    String text = headline.text();
                    hashMap.put("News", text);
                }
                NewsFragment.isNetworkError = false;
            } catch (Exception e) {
                e.printStackTrace();
                NewsFragment.isNetworkError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}
