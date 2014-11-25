package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;


public class Etelek extends Activity {

    static ArrayList<EtelItem> eteleklista = new ArrayList<EtelItem>();
    ListView listView;
    EtelekListaAdapter adapter;
    ProgressBar prgLoading;
    String EtelekLink;
    MenuItem item;

    ProgressBar preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etelek);

        //kategoria kivétele
        Intent currentintent = getIntent();
        Bundle bundle = currentintent.getExtras();

        if (bundle != null) {
            String catID = (String) bundle.get("catID");
            EtelekLink = "http://dorothy.hu/Android/GetEtelekByKategoriaJson/" + catID;
        }

        adapter = new EtelekListaAdapter(Etelek.this);
        listView = (ListView) findViewById(R.id.listEtelek);
        preLoader= (ProgressBar) findViewById(R.id.preLoader);
        preLoader.setVisibility(View.VISIBLE);

        MyDownloadManager manager = new MyDownloadManager(EtelekLink);

        manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {

            @Override
            public void onDownloadSuccess(String message) {
                clearData();
                try {
                    JSONArray data = new JSONArray(message);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        EtelItem item = new EtelItem();
                        item.setId(UUID.fromString(object.getString("ID")));
                        item.setPrice( object.getString("Ar"));
                        item.setLink(GlobalHelper.Website + object.getString("Link"));
                        item.setFilesize( object.getString("PictureFileSize"));

                        item.setName(object.getString("EtelNev"));

                        item.setDesc(object.getString("Description"));
                        item.setNeedAdditional(Boolean.parseBoolean(object.getString("NeedAdditionalCategory")));
                        item.setAdditionalKaja(object.getString("AdditionalCategoryId"));

                        eteleklista.add(item);


                        String path = GlobalHelper.Website + object.getString("Link");

                        Bitmap bmp = GlobalHelper.CheckFile(object.getString("ID"), object.getString("PictureFileSize"), path, getApplicationContext());
                        if (bmp == null)
                        {

                        }
//                        Bitmap bmp = null;
//                        try {
//                            bmp = BitmapFactory.decodeStream(new URL(path).openConnection().getInputStream());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        eteleklista.get(i).setEtelKep(bmp);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                        preLoader.setVisibility(View.GONE);
                    }
                });


            }

        });

        manager.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Etelek.this, EtelekReszlet.class);
                try {

                    EtelItem etelItem = (EtelItem) (parent.getItemAtPosition(position));

                    intent.putExtra("etelLink", etelItem.getLink());
                    intent.putExtra("etelfilesize", etelItem.getFilesize());
                    intent.putExtra("etelNev", etelItem.getName());
                    intent.putExtra("etelLeiras", etelItem.getDesc());
                    intent.putExtra("etelAr", Integer.parseInt(etelItem.getPrice()));
                    intent.putExtra("etelID", etelItem.getId().toString());
                    intent.putExtra("eteladdID", etelItem.getAdditionalKaja());
                    intent.putExtra("needaddID", etelItem.isNeedAdditional());

                } catch (Exception e) {
                    Log.d("", "");
                }
                startActivity(intent);
            }
        });
    }

    void clearData() {
        eteleklista.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.etelek, menu);

        try {
            item = menu.findItem(R.id.action_settings);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Intent intent = new Intent(Etelek.this, Kosar.class);

                    startActivity(intent);

                    return false;
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
