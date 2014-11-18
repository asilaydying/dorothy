package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class Kosar extends Activity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kosar);

        txt = (TextView) findViewById(R.id.Kosarszumma);

        String link = "http://dorothy.hu/Android/KosarLekerdez?UserName=judit.komlosi@gmail.com";

        final MyDownloadManager manager = new MyDownloadManager(link);

        manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(message);
                    final String TotalAmount = obj.getString("TotalAmount");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(TotalAmount);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.kosar, menu);
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
