package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;


public class CimValasztas extends Activity {

    RadioGroup cimekgroup;
    String user;
    Button btnmegerosit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cim_valasztas);

        SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);

        cimekgroup = (RadioGroup) findViewById(R.id.cimekgroup);
        btnmegerosit= (Button) findViewById(R.id.buttonmegerosit);

        String link = "http://dorothy.hu/Android/GetAddresses?username=" + user;

        MyDownloadManager manager = new MyDownloadManager(link);

        manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                try {
                    final JSONArray array = new JSONArray(message);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        if (i == 0) {
                            addRadio(object.getString("AddressString"), Integer.parseInt(object.getString("AddressId")), true);
                        } else {
                            addRadio(object.getString("AddressString"), Integer.parseInt(object.getString("AddressId")), false);
                        }

                    }

                    final RadioButton radio = new RadioButton(CimValasztas.this);
                    radio.setId(-1);
                    radio.setText("Új cím");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cimekgroup.addView(radio);
                            if (array.length()==0) {
                                cimekgroup.check(-1);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        manager.start();


        btnmegerosit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cim_valasztas, menu);
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

    private void addRadio(String cim, final int cimid, final boolean isChecked) {
        final RadioButton radio = new RadioButton(this);
        radio.setId(cimid);
        radio.setText(cim);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cimekgroup.addView(radio);
                if (isChecked) {
                    cimekgroup.check(cimid);
                }
            }
        });

    }
}
