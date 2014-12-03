package com.example.asilaydying.dorothy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EtelekReszlet extends Activity {
    Button AddButton, pluszButton, minuszButton;
    EditText count;
    ImageView imageView;
    TextView ar, leiras, nev;
    String ID;
    String additionalID;
    Boolean NeedAdditional;
    ListView listView;
    ProgressBar preLoader;

    String user;
    MenuItem item;


    static ArrayList<EtelekReszletItem> etelekreszleteklista = new ArrayList<EtelekReszletItem>();
    static List<String> addlista = new ArrayList<String>();
    static List<String> addlistakey = new ArrayList<String>();
    EtelekReszletListaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etelek_reszlet);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);



        SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);
        preLoader = (ProgressBar) findViewById(R.id.preLoader);
        imageView = (ImageView) findViewById(R.id.EtelReszletKep);
        ar = (TextView) findViewById(R.id.EtelReszletesAr);
        leiras = (TextView) findViewById(R.id.EtelReszletLeiras);
        nev = (TextView) findViewById(R.id.EtelReszletNev);

        AddButton = (Button) findViewById(R.id.btnAdd);
        pluszButton = (Button) findViewById(R.id.plusz);
        minuszButton = (Button) findViewById(R.id.minusz);
        count = (EditText) findViewById(R.id.count);
        listView = (ListView) findViewById(R.id.ListReszlet);

        Intent currentintent = getIntent();
        Bundle bundle = currentintent.getExtras();

        if (bundle != null) {
            String etelnev = (String) bundle.get("etelNev");
            Integer etelAr = (Integer) bundle.get("etelAr");
            String etelLeiras = (String) bundle.get("etelLeiras");
            //Bitmap bmp = (Bitmap) bundle.get("etelKep");
            ID = (String) bundle.get("etelID");
            additionalID = (String) bundle.get("eteladdID");
            NeedAdditional = (Boolean) bundle.get("needaddID");

            ar.setText(etelAr.toString());
            leiras.setText(etelLeiras);
            nev.setText(etelnev);

        }


        Bitmap bmp = GlobalHelper.CheckFile(ID.toString(), (String) bundle.get("etelfilesize"), (String) bundle.get("etelLink"), getApplicationContext());
        imageView.setImageBitmap(bmp);


        MyDownloadManager additionalManager = new MyDownloadManager("http://dorothy.hu/Android/GetEtelekByKategoriaJson/" + additionalID);


        adapter = new EtelekReszletListaAdapter(EtelekReszlet.this);


        additionalManager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                try {
                    ClearData();

                    addlista.add("Nem kérek köretet");
                    addlistakey.add("null");

                    etelekreszleteklista.add(new EtelekReszletItem(addlista.get(0), addlistakey.get(0)));

                    JSONArray data = new JSONArray(message);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        addlista.add(object.getString("EtelNev"));
                        addlistakey.add(object.getString("ID"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                listView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listView.setAdapter(adapter);
//                    }
//                });
            }
        });
        additionalManager.start();

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String link = "http://dorothy.hu/Android/KosarbaKorettel?UserName=" + user + "&productid=" + ID.toString();

                link += "&mennyiseg=" + count.getText();

                for (int i = 0; i < etelekreszleteklista.size(); i++) {
                    link += "&additionalok=" + etelekreszleteklista.get(i).getAddID();
                }

                MyDownloadManager manager = new MyDownloadManager(link);

                manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String message) {
                        if (message.equals("OK")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "A termék a kosárba került", Toast.LENGTH_LONG).show();
                                    GlobalHelper.EnableButton(v);
                                    preLoader.setVisibility(View.GONE);
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Hiba történt!", Toast.LENGTH_LONG).show();
                                    GlobalHelper.EnableButton(v);
                                    preLoader.setVisibility(View.GONE);
                                }
                            });

                        }
                    }
                });
                manager.start();
                GlobalHelper.DisableButton(v);
                preLoader.setVisibility(View.VISIBLE);
            }
        });
        pluszButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int darab = Integer.parseInt(String.valueOf(count.getText()));
                count.setText(String.valueOf(++darab));
                if (NeedAdditional) {
                    etelekreszleteklista.add(new EtelekReszletItem(addlista.get(0), addlistakey.get(0)));
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
                if (darab > 0) {
                    count.setText(String.valueOf(--darab));
                    if (NeedAdditional) {
                        etelekreszleteklista.remove(etelekreszleteklista.size() - 1);
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(new View(this).getWindowToken(), 0);

        preLoader.setVisibility(View.VISIBLE);
        MyDownloadManager mainManager = new MyDownloadManager(GlobalHelper.BaseAndroidURL + "KosarLekerdez?UserName=" + user + "&productid=" + ID);
        mainManager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                try {
                    JSONObject object = new JSONObject(message);

                    JSONArray array = new JSONArray(object.getString("ProductsViewModel"));

                    etelekreszleteklista.clear();


                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject obj = array.getJSONObject(i);

                        if (obj.getBoolean("IsAdditionalFood")) {
                            EtelekReszletItem item = new EtelekReszletItem(obj.getString("ProductName"), obj.getString("productId"));
                            etelekreszleteklista.add(item);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        int productcount = Integer.parseInt(obj.getString("ProductCnt"));
                                        count.setText(String.valueOf(productcount));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                    if (array.length() > 0 && NeedAdditional) {
                        JSONObject obj = array.getJSONObject(0);
                        int productcount = Integer.parseInt(obj.getString("ProductCnt"));
                        int emptyfields = productcount - array.length() + 1;
                        for (int i = 0; i < emptyfields; i++) {
                            etelekreszleteklista.add(new EtelekReszletItem(addlista.get(0), addlistakey.get(0)));
                        }
                    } else if (NeedAdditional) {
                        etelekreszleteklista.add(new EtelekReszletItem(addlista.get(0), addlistakey.get(0)));
                    }

                    listView.post(new Runnable() {
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
        mainManager.start();
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

        try {
            item = menu.findItem(R.id.action_settings);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Intent intent = new Intent(EtelekReszlet.this, Kosar.class);

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
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
