package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MyActivity extends Activity {

    Button btnlogin;
    Button btnregister;

    EditText loginuser;
    EditText loginpass;
    String user;

    EditText regemail;
    EditText regfullname;
    EditText regphone;
    EditText reguser;
    EditText regpass;
    EditText regpassagain;

    ProgressBar preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);

        btnlogin = (Button) findViewById(R.id.button_login);
        btnregister = (Button) findViewById(R.id.button_reg);

        loginpass = (EditText) findViewById(R.id.login_pass);
        loginuser = (EditText) findViewById(R.id.login_login);

        regemail = (EditText) findViewById(R.id.reg_email);
        regfullname = (EditText) findViewById(R.id.reg_fullname);
        regphone = (EditText) findViewById(R.id.reg_phone);
        reguser = (EditText) findViewById(R.id.reg_user);
        regpass = (EditText) findViewById(R.id.reg_pass);
        regpassagain = (EditText) findViewById(R.id.reg_passagain);

        preLoader = (ProgressBar) findViewById(R.id.preLoader);
        preLoader.setVisibility(View.GONE);

        if (user != null) {
            Intent intent = new Intent(this, Kategoriak.class);
            //Intent intent = new Intent(this, Etelek.class);
            //intent.putExtra("catID", "1");
            startActivity(intent);
            this.finish();
        } else {

        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://dorothy.hu/Android/Login";
                final String username = String.valueOf(loginuser.getText());
                String pass = String.valueOf(loginpass.getText());
                link = link + "?login=" + username + "&passwordhash=" + pass;
                final MyDownloadManager manager = new MyDownloadManager(link);

                manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String message) {
                        if (message.equals("OK")) {

                            SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username", username);
                            editor.commit();

                            Intent intent = new Intent(MyActivity.this, Kategoriak.class);
                            startActivity(intent);
                            MyActivity.this.finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Hibás felhasználónév vagy jelszó!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                manager.start();
                preLoader.setVisibility(View.VISIBLE);
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regemail.getText().toString().matches("")
                        || regfullname.getText().toString().matches("")
                        || regpass.getText().toString().matches("")
                        || regpassagain.getText().toString().matches("")
                        || regphone.getText().toString().matches("")
                        || reguser.getText().toString().matches("")
                        )//nem üres egyik mező sem
                {
                    Toast.makeText(getApplicationContext(), "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(regemail.getText().toString()).matches())//Email check
                {
                    Toast.makeText(getApplicationContext(), "Hibás email cím!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.PHONE.matcher(regphone.getText().toString()).matches())//Phone check
                {
                    Toast.makeText(getApplicationContext(), "Hibás telefonszám!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (regpass.getText().toString().length()<6)
                {
                    Toast.makeText(getApplicationContext(), "A jelszónak legalább 6 karakterből kell állnia!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!regpass.getText().toString().equals(regpassagain.getText().toString()))//Phone check
                {
                    Toast.makeText(getApplicationContext(), "A jelszavak nem egyeznek!", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String username = String.valueOf(reguser.getText());

                String link = GlobalHelper.BaseAndroidURL + "RegisterFromAndroid?";
                link += "UserName=" + Uri.encode(String.valueOf(reguser.getText()));
                link += "&Password=" + Uri.encode(String.valueOf(regpass.getText()));
                link += "&Email=" + Uri.encode(String.valueOf(regemail.getText()));
                link += "&Nev=" + Uri.encode(String.valueOf(regfullname.getText()));
                link += "&Telefonszam=" + Uri.encode(String.valueOf(regphone.getText()));

                final MyDownloadManager manager = new MyDownloadManager(link);
                manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(final String message) {
                        if (message.equals("OK")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("username", username);
                                    editor.commit();

                                    Intent intent = new Intent(MyActivity.this, Kategoriak.class);
                                    startActivity(intent);
                                    MyActivity.this.finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    preLoader.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
                manager.start();

                preLoader.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
