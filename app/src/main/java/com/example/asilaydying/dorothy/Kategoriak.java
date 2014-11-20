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

    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategoriak);


        //check username
        try {

            SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
            user= settings.getString("username", null);
            if (user==null)
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", "zabomate@gmail.com");
                editor.commit();
            }
            user= settings.getString("username", null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter = new KategoriakListaAdapter(Kategoriak.this);

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
        new getDataTask().execute();
    }

    void clearData() {
        kategoriakLista.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kategoriak, menu);

        try {
            item = menu.findItem(R.id.action_settings);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Intent intent = new Intent(Kategoriak.this, Kosar.class);

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

    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask() {
//            if(!prgLoading.isShown()){
//                prgLoading.setVisibility(0);
//                txtAlert.setVisibility(8);
//            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            parseJSONData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // when finish parsing, hide progressbar
//            prgLoading.setVisibility(8);
            listView.setAdapter(adapter);
            // if internet connection and data available show data on list
            // otherwise, show alert text
//            if((Category_ID.size() > 0) && (IOConnect == 0)){
//                listCategory.setVisibility(0);
//                listCategory.setAdapter(cla);
//            }else{
//                txtAlert.setVisibility(0);
//            }
        }
    }

    public void parseJSONData() {

        clearData();

        try {
            // request data from Category API
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(KategoriakLink);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

            String line;
            String str = "";
            while ((line = in.readLine()) != null) {
                str += line;
            }

            // parse json data and store into arraylist variables
            //JSONObject json = new JSONObject(str);
            JSONArray data = new JSONArray(str);

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

//                JSONObject category = object.getJSONObject("Category");
                KategoriakItem item = new KategoriakItem();
                item.Category_ID = Long.parseLong(object.getString("ID"));
                item.Category_name = object.getString("KategoriaNev");
                // Category_image.add(category.getString("Category_image"));
                kategoriakLista.add(item);

            }

            for (int i = 0; i < kategoriakLista.size(); i++) {
                String path = GlobalHelper.KategoriaKepLink + kategoriakLista.get(i).Category_ID + ".jpg";
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(new URL(path).openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                kategoriakLista.get(i).KategoriaKep = bmp;
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //IOConnect = 1;
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
