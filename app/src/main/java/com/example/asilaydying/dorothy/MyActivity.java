package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        regemail= (EditText) findViewById(R.id.reg_email);
        regfullname= (EditText) findViewById(R.id.reg_fullname);
        regphone= (EditText) findViewById(R.id.reg_phone);
        reguser= (EditText) findViewById(R.id.reg_user);
        regpass= (EditText) findViewById(R.id.reg_pass);
        regpassagain= (EditText) findViewById(R.id.reg_passagain);


        if (user != null) {
            Intent intent = new Intent(this, Kategoriak.class);
            //Intent intent = new Intent(this, Etelek.class);
            //intent.putExtra("catID", "1");
            startActivity(intent);

        } else {

        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://dorothy.hu/Android/Login";
                final String username = String.valueOf(loginuser.getText());
                String pass = String.valueOf(loginpass.getText());
                link=link + "?login=" + username + "&passwordhash=" + pass;
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

                        } else {
                            Toast.makeText(getApplicationContext(), "Hibás felhasználónév vagy jelszó!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                manager.start();

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
                }
                if (Patterns.EMAIL_ADDRESS.matcher(regemail.getText().toString()).matches())//Email check
                {
                    Toast.makeText(getApplicationContext(), "Hibás email cím!", Toast.LENGTH_SHORT).show();
                }
                if (Patterns.PHONE.matcher(regphone.getText().toString()).matches())//Phone check
                {
                    Toast.makeText(getApplicationContext(), "Hibás telefonszám!", Toast.LENGTH_SHORT).show();
                }
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
