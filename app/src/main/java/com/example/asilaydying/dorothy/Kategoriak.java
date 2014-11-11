package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.ArrayList;


public class Kategoriak extends Activity {

    static ArrayList<Long> Category_ID = new ArrayList<Long>();
    static ArrayList<String> Category_name = new ArrayList<String>();
    static ArrayList<String> Category_image = new ArrayList<String>();
    ListView listView;
    KategoriakListaAdapter adapter;
    ProgressBar prgLoading;
    String KategoriakLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategoriak);
        adapter = new KategoriakListaAdapter(Kategoriak.this);
//        Category_ID.add(0, (long) 1);
//        Category_name.add(0,"elso elem");
//        Category_ID.add(1, (long) 1);
//        Category_name.add(1,"masodik elem");
//        Category_ID.add(2, (long) 1);
//        Category_name.add(2,"harmadik elem");
        listView = (ListView) findViewById(R.id.listCategory);

        KategoriakLink = "http://dorothy.hu/Android/GetKategoriakJson";
        new getDataTask().execute();
    }

    void clearData(){
        Category_ID.clear();
        Category_name.clear();
        Category_image.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kategoriak, menu);
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
        getDataTask(){
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

    public void parseJSONData(){

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
            while ((line = in.readLine()) != null){
                str += line;
            }

            // parse json data and store into arraylist variables
            //JSONObject json = new JSONObject(str);
            JSONArray data = new JSONArray(str);

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

//                JSONObject category = object.getJSONObject("Category");

                Category_ID.add(Long.parseLong(object.getString("ID")));
                Category_name.add(object.getString("KategoriaNev"));
               // Category_image.add(category.getString("Category_image"));
                Log.d("Category name", Category_name.get(i));

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
