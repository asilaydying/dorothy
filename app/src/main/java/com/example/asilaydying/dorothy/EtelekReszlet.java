package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class EtelekReszlet extends Activity {

    ImageView imageView;
    TextView ar, leiras,nev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etelek_reszlet);

        Intent currentintent= getIntent();
        Bundle bundle = currentintent.getExtras();

        if(bundle!=null)
        {
            String etelnev =(String) bundle.get("etelNev");
            String etelAr = (String) bundle.get("etelAr");
            String etelLeiras = (String) bundle.get("etelLeiras");
            Bitmap bmp = (Bitmap)bundle.get("etelKep");

            imageView = (ImageView) findViewById(R.id.EtelReszletKep);
            ar = (TextView) findViewById(R.id.EtelReszletesAr);
            leiras = (TextView) findViewById(R.id.EtelReszletLeiras);
            nev = (TextView) findViewById(R.id.EtelReszletNev);

            imageView.setImageBitmap(bmp);
            ar.setText(etelAr);
            leiras.setText(etelLeiras);
            nev.setText(etelnev);

        }
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
