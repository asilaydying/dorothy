package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EtelekReszlet extends Activity {
    Button AddButton, pluszButton, minuszButton;
    EditText count;
    ImageView imageView;
    TextView ar, leiras, nev;
    UUID ID;
    String additionalID;
    Boolean NeedAdditional;
    ListView listView;


    static ArrayList<EtelekReszletItem> etelekreszleteklista = new ArrayList<EtelekReszletItem>();
    static List<String> addlista = new ArrayList<String>();
    static List<String> addlistakey = new ArrayList<String>();
    EtelekReszletListaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etelek_reszlet);

        Intent currentintent = getIntent();
        Bundle bundle = currentintent.getExtras();

        if (bundle != null) {
            String etelnev = (String) bundle.get("etelNev");
            String etelAr = (String) bundle.get("etelAr");
            String etelLeiras = (String) bundle.get("etelLeiras");
            //Bitmap bmp = (Bitmap) bundle.get("etelKep");
            ID = (UUID) bundle.get("etelID");
            additionalID = (String) bundle.get("eteladdID");
            NeedAdditional = (Boolean) bundle.get("needaddID");

            Bitmap bmp = GlobalHelper.CheckFile(ID.toString(), (String) bundle.get("etelfilesize"), (String) bundle.get("etelLink"), getApplicationContext());

            imageView = (ImageView) findViewById(R.id.EtelReszletKep);
            ar = (TextView) findViewById(R.id.EtelReszletesAr);
            leiras = (TextView) findViewById(R.id.EtelReszletLeiras);
            nev = (TextView) findViewById(R.id.EtelReszletNev);


            imageView.setImageBitmap(bmp);
            ar.setText(etelAr);
            leiras.setText(etelLeiras);
            nev.setText(etelnev);

        }


        MyDownloadManager manager = new MyDownloadManager("http://dorothy.hu/Android/GetEtelekByKategoriaJson/" + additionalID);



        adapter = new EtelekReszletListaAdapter(EtelekReszlet.this);

        AddButton = (Button) findViewById(R.id.btnAdd);
        pluszButton = (Button) findViewById(R.id.plusz);
        minuszButton = (Button) findViewById(R.id.minusz);
        count = (EditText) findViewById(R.id.count);
        listView = (ListView) findViewById(R.id.ListReszlet);

        manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                try {
                    ClearData();

                    addlista.add("Nem kérek köretet");
                    addlistakey.add("null");

                    etelekreszleteklista.add(new EtelekReszletItem(addlista.get(0),addlistakey.get(0)));

                    JSONArray data = new JSONArray(message);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        addlista.add(object.getString("EtelNev"));
                        addlistakey.add(object.getString("ID"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
        manager.start();

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://dorothy.hu/Android/Kosarba?UserName=judit.komlosi@gmail.com&id=" + ID.toString();

                MyDownloadManager manager = new MyDownloadManager(link);

                manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String message) {
                        if (message.equals("OK")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "A termék a kosárba került", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Hiba történt!", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                });
                manager.start();
            }
        });
        pluszButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int darab = Integer.parseInt(String.valueOf(count.getText()));
                count.setText(String.valueOf(++darab));
                if (NeedAdditional) {
                    etelekreszleteklista.add(new EtelekReszletItem(addlista.get(0),addlistakey.get(0)));
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                        }
                    });
                }
            }

        });
        minuszButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int darab = Integer.parseInt(String.valueOf(count.getText()));
                count.setText(String.valueOf(--darab));
                if (NeedAdditional) {
                    etelekreszleteklista.remove(etelekreszleteklista.size()-1);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                        }
                    });
                }
            }
        });


    }

    private void ClearData() {
        etelekreszleteklista.clear();
        addlista.clear();
        addlistakey.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.etelek_reszlet, menu);
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
