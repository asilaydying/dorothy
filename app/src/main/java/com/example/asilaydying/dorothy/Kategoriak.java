package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Kategoriak extends Activity {

    static ArrayList<KategoriakItem> kategoriakLista = new ArrayList<KategoriakItem>();

    ListView listView;
    KategoriakListaAdapter adapter;
    ProgressBar prgLoading;
    String KategoriakLink;
    String user;
    ProgressBar preLoader;


    MenuItem kosarmenuitem;
    MenuItem logoutmenuitem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategoriak);


        //check username
        try {

            SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
            user = settings.getString("username", null);
            if (user == null) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", "zabomate@gmail.com");
                editor.commit();
            }
            user = settings.getString("username", null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter = new KategoriakListaAdapter(Kategoriak.this);
        preLoader = (ProgressBar) findViewById(R.id.preLoader);
        listView = (ListView) findViewById(R.id.listCategory);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Kategoriak.this, Etelek.class);
                try {


                    long asdf = (Long) (parent.getItemAtPosition(position));
                    intent.putExtra("catID", String.valueOf(asdf));
                } catch (Exception e) {
                    Log.d("", "");
                }
                startActivity(intent);
            }
        });

        KategoriakLink = "http://dorothy.hu/Android/GetKategoriakJson";

        MyDownloadManager manager = new MyDownloadManager(KategoriakLink);

        manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                try {
                    JSONArray data = new JSONArray(message);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        KategoriakItem item = new KategoriakItem();
                        item.Category_ID = Long.parseLong(object.getString("ID"));
                        item.Category_name = object.getString("KategoriaNev");
                        // Category_image.add(category.getString("Category_image"));

                        String path = GlobalHelper.KategoriaKepLink + item.Category_ID + ".jpg";
                        item.KategoriaKep = GlobalHelper.CheckFile(object.getString("ID"), object.getString("PictureFileSize"), path, getApplicationContext());

                        kategoriakLista.add(item);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                            preLoader.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        manager.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kategoriak, menu);

        try {
            kosarmenuitem = menu.findItem(R.id.action_settings);
            kosarmenuitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Intent intent = new Intent(Kategoriak.this, Kosar.class);

                    startActivity(intent);

                    return false;
                }
            });
            logoutmenuitem = menu.findItem(R.id.logout);
            logoutmenuitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    GlobalHelper.LogOut(Kategoriak.this);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
