package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CimValasztas extends Activity {

    RadioGroup cimekgroup;
    String user;
    Button btnmegerosit;
    EditText utca;
    EditText hazszam;
    EditText emelet;
    EditText kapucsengo;
    EditText megjegyzes;
    Spinner kerulet;
    ProgressBar preLoader;
    List<String> keruletlista = Arrays.asList("I. kerület", "II. kerület", "XII. kerület");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cim_valasztas);

        SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);

        cimekgroup = (RadioGroup) findViewById(R.id.cimekgroup);
        btnmegerosit = (Button) findViewById(R.id.buttonmegerosit);
        preLoader = (ProgressBar) findViewById(R.id.preLoader);
        preLoader.setVisibility(View.VISIBLE);

        utca = (EditText) findViewById(R.id.txtutca);
        hazszam = (EditText) findViewById(R.id.txthazszam);
        emelet = (EditText) findViewById(R.id.txtemelet);
        kapucsengo = (EditText) findViewById(R.id.txtkapucsengo);
        megjegyzes = (EditText) findViewById(R.id.txtmegjegyzes);
        kerulet = (Spinner) findViewById(R.id.kerulet);

        String link = "http://dorothy.hu/Android/GetAddresses?username=" + user;

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(CimValasztas.this, R.layout.spinner_item/*android.R.layout.simple_spinner_dropdown_item*/, keruletlista);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        kerulet.setAdapter(adapter);

        final MyDownloadManager manager = new MyDownloadManager(link);

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
                    radio.setTag(-1);
                    radio.setText("Új cím");
                    radio.setTextColor(Color.WHITE);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cimekgroup.addView(radio);
                            if (array.length() == 0) {
                                cimekgroup.check(-1);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        preLoader.setVisibility(View.GONE);
                    }
                });
            }
        });
        manager.start();


        btnmegerosit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = cimekgroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) cimekgroup.findViewById(radioButtonID);
                final int id = (Integer) radioButton.getTag();
                if (id == -1)//új cím
                {
                    if (utca.getText().toString().matches("") || hazszam.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Az utca és a házszám kitöltése kötelező!", Toast.LENGTH_LONG).show();
                        return;
                    } else {//új cím felvétele
                        String link = GlobalHelper.BaseAndroidURL + "CimMentes?Username=" + user;
                        link += "&Kerulet=" + Uri.encode(kerulet.getItemAtPosition(kerulet.getSelectedItemPosition()).toString());
                        link += "&Utca=" + Uri.encode(utca.getText().toString());
                        link += "&Hazszam=" + Uri.encode(hazszam.getText().toString());
                        link += "&EmeletAjto=" + Uri.encode(emelet.getText().toString());
                        link += "&Kapucsengo=" + Uri.encode(kapucsengo.getText().toString());
                        link += "&Megjegyzes=" + Uri.encode(megjegyzes.getText().toString());

                        MyDownloadManager cimManager = new MyDownloadManager(link);
                        cimManager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(final String message) {
                                if (message.equals("HIBA")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Nem sikerült felvenni az új cimet!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject object = new JSONObject(message);

                                                Intent intent = new Intent(CimValasztas.this, Megerosites.class);

                                                intent.putExtra("cim", object.getString("CimString"));
                                                intent.putExtra("cimid", object.getInt("CimId"));

                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        cimManager.start();
                    }
                } else {
                    Intent intent = new Intent(CimValasztas.this, Megerosites.class);

                    intent.putExtra("cim", radioButton.getText().toString());
                    intent.putExtra("cimid", id);

                    startActivity(intent);
                }
            }
        });
        cimekgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton radioButton = (RadioButton) cimekgroup.findViewById(checkedId);
                final int id = (Integer) radioButton.getTag();

                if (id == -1) {
                    kerulet.setVisibility(View.VISIBLE);
                    utca.setVisibility(View.VISIBLE);
                    hazszam.setVisibility(View.VISIBLE);
                    emelet.setVisibility(View.VISIBLE);
                    kapucsengo.setVisibility(View.VISIBLE);
                    megjegyzes.setVisibility(View.VISIBLE);
                } else {
                    kerulet.setVisibility(View.GONE);
                    utca.setVisibility(View.GONE);
                    hazszam.setVisibility(View.GONE);
                    emelet.setVisibility(View.GONE);
                    kapucsengo.setVisibility(View.GONE);
                    megjegyzes.setVisibility(View.GONE);
                }
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
        radio.setTag(cimid);
        radio.setText(cim);
        radio.setTextColor(Color.WHITE);


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
