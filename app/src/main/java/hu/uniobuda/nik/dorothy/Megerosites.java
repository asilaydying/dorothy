package hu.uniobuda.nik.dorothy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import hu.uniobuda.nik.dorothy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Megerosites extends Activity {

    String cim = "";
    int cimid;
    TextView txtcim;
    Button savebutton;
    TableLayout tl;
    EditText megjegyzes;
    TextView sum;
    String user;
    ProgressBar preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megerosites);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);

        SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);

        Intent currentintent = getIntent();
        Bundle bundle = currentintent.getExtras();

        if (bundle != null) {
            cim = (String) bundle.get("cim");
            cimid = (Integer) bundle.get("cimid");
        }
        preLoader= (ProgressBar) findViewById(R.id.preLoader);
        txtcim = (TextView) findViewById(R.id.megerosites_txtcim);
        savebutton = (Button) findViewById(R.id.megerosites_savebutton);
        tl = (TableLayout) findViewById(R.id.megerosites_tableLayout);
        sum = (TextView) findViewById(R.id.megerosites_sum);
        megjegyzes = (EditText) findViewById(R.id.megerosites_megjegyzes);

        txtcim.setText(cim);

        preLoader.setVisibility(View.VISIBLE);

        String link = "http://dorothy.hu/Android/KosarLekerdez?UserName=" + user;

        final MyDownloadManager manager = new MyDownloadManager(link);

        manager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(message);
                    final String TotalAmount = obj.getString("TotalAmount");
                    JSONArray array = new JSONArray(obj.getString("ProductsViewModel"));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        addRow(object.getString("Index"), object.getString("ProductName"), object.getString("ProductCnt"), object.getString("ProductAmountSum"), Boolean.parseBoolean(object.getString("IsAdditionalFood")));

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sum.setText(TotalAmount);
                            preLoader.setVisibility(View.GONE);
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


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String link = "http://dorothy.hu/Android/befejez?UserName=" + user + "&addressid=" + cimid + "&Megjegyzes=" + Uri.encode(String.valueOf(megjegyzes.getText()));

                final MyDownloadManager downloadManager = new MyDownloadManager(link);
                downloadManager.setOnDownloadListener(new MyDownloadManager.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String message) {
                        if (message.equals("OK")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Sikeres rendelés leadás!", Toast.LENGTH_LONG).show();
                                    GlobalHelper.EnableButton(v);
                                    preLoader.setVisibility(View.GONE);

                                    Intent intent = new Intent(Megerosites.this, Kategoriak.class);

                                    startActivity(intent);

                                    finish();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Megerosites.this, "Nem sikerült a rendelés leadása!", Toast.LENGTH_LONG).show();
                                    GlobalHelper.EnableButton(v);
                                    preLoader.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });

                downloadManager.start();
                GlobalHelper.DisableButton(v);
                preLoader.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.megerosites, menu);
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
        if (id==android.R.id.home)
        {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addRow(String index, String name, String count, String sum, boolean isAdditional) {
        final TableRow tr = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        tr.setLayoutParams(lp);

        View view;
        if (!isAdditional) {
            view = LayoutInflater.from(this).inflate(R.layout.kosar_item_main, null);

            ViewHolder_main holder = new ViewHolder_main();

            holder.tvindex = (TextView) view.findViewById(R.id.kosar_index);
            holder.tvname = (TextView) view.findViewById(R.id.kosar_name);
            holder.tvcounter = (TextView) view.findViewById(R.id.kosar_count);
            holder.tvsum = (TextView) view.findViewById(R.id.kosar_sum);

            holder.tvindex.setText(index);
            holder.tvname.setText(name);
            holder.tvcounter.setText(count);
            holder.tvsum.setText(sum);
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.kosar_item_sub, null);

            ViewHolder_sub holder = new ViewHolder_sub();

            holder.tvname = (TextView) view.findViewById(R.id.kosar_name);
            holder.tvcounter = (TextView) view.findViewById(R.id.kosar_count);
            holder.tvsum = (TextView) view.findViewById(R.id.kosar_sum);

            holder.tvname.setText(name);
            holder.tvcounter.setText(count);
            holder.tvsum.setText(sum);
        }

        tr.addView(view);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            }
        });

    }

    static class ViewHolder_main {
        TextView tvindex;
        TextView tvname;
        TextView tvcounter;
        TextView tvsum;
    }

    static class ViewHolder_sub {
        TextView tvname;
        TextView tvcounter;
        TextView tvsum;
    }
}
