package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Kosar extends Activity {
    TableLayout tl;
    TextView txt;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kosar);

        SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);

        tl = (TableLayout) findViewById(R.id.tableLayout);

        final TableRow tr = new TableRow(this);

        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        txt = (TextView) findViewById(R.id.Kosarszumma);

        String link = "http://dorothy.hu/Android/KosarLekerdez?UserName="+user;

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

                        addRow(object.getString("Index"),object.getString("ProductName"),object.getString("ProductCnt"),object.getString("ProductAmountSum"),Boolean.parseBoolean(object.getString("IsAdditionalFood")));

                    }

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

    private void addRow(String index, String name, String count, String sum, boolean isAdditional)
    {
        final TableRow tr = new TableRow(this);
        LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1.0f);
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
        }
        else {
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
                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        });

//        LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1.0f);
//        tr.setLayoutParams(lp);
//
//        TextView tvLeft = new TextView(this);
//        tvLeft.setLayoutParams(lp);
//        tvLeft.setBackgroundColor(Color.WHITE);
//        tvLeft.setText(index);
//        tvLeft.setGravity(Gravity.CENTER);
//
//        TextView tvCenter = new TextView(this);
//        tvCenter.setLayoutParams(lp);
//        tvCenter.setBackgroundColor(Color.WHITE);
//        tvCenter.setText(name);
//        tvCenter.setGravity(Gravity.CENTER);
//
//        TextView tvRight = new TextView(this);
//        tvRight.setLayoutParams(lp);
//        tvRight.setBackgroundColor(Color.WHITE);
//        tvRight.setText(count);
//        tvRight.setGravity(Gravity.CENTER);
//
//        TextView tvRight2 = new TextView(this);
//        tvRight2.setLayoutParams(lp);
//        tvRight2.setBackgroundColor(Color.WHITE);
//        tvRight2.setText(sum);
//        tvRight2.setGravity(Gravity.CENTER);
//
//        tr.addView(tvLeft);
//        tr.addView(tvCenter);
//        tr.addView(tvRight);
//        tr.addView(tvRight2);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//            }
//        });

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
