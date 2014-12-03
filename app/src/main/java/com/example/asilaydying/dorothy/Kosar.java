package com.example.asilaydying.dorothy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Kosar extends Activity {
    TableLayout tl;
    TextView txt;
    String user;
    Button cimvalaszt;
    ProgressBar preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kosar);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);

        SharedPreferences settings = getSharedPreferences(GlobalHelper.PrefFileUserData, 0);
        user = settings.getString("username", null);
        preLoader= (ProgressBar) findViewById(R.id.preLoader);
        preLoader.setVisibility(View.VISIBLE);
        tl = (TableLayout) findViewById(R.id.tableLayout);
        cimvalaszt = (Button) findViewById(R.id.buttoncimvalasztas);

        final TableRow tr = new TableRow(this);

        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        txt = (TextView) findViewById(R.id.Kosarszumma);





        cimvalaszt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Kosar.this, CimValasztas.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tl.removeAllViews();
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

                        KosarItem item = new KosarItem();
                        item.setProductId(object.getString("productId"));
                        item.setIndex(object.getString("Index"));
                        item.setProductName(object.getString("ProductName"));
                        item.setProductCnt(object.getString("ProductCnt"));
                        item.setIsAdditionalFood(object.getString("IsAdditionalFood"));
                        item.setNeedAdditional(object.getString("NeedAdditional"));
                        item.setProductAmountSum(object.getString("ProductAmountSum"));
                        item.setAdditionalCategory(object.getString("AdditionalCategoryId"));
                        item.setPictureLink(object.getString("PictureLink"));
                        item.setFileSize(object.getString("FileSize"));
                        item.setProductDetail(object.getString("Leiras"));


                        addRow(item);

                    }
                    if (array.length()>0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt.setText(TotalAmount);
                                cimvalaszt.setEnabled(true);
                                preLoader.setVisibility(View.GONE);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt.setText("Nincs termék a kosárban!");
                                cimvalaszt.setEnabled(false);
                                preLoader.setVisibility(View.GONE);
                            }
                        });
                    }

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
        if (id==android.R.id.home)
        {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addRow(KosarItem item) {
        final TableRow tr = new TableRow(this);
        LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
        tr.setLayoutParams(lp);

        View view;
        if (!Boolean.parseBoolean(item.getIsAdditionalFood())) {
            view = LayoutInflater.from(this).inflate(R.layout.kosar_item_main, null);

            ViewHolder_main holder = new ViewHolder_main();

            holder.tvindex = (TextView) view.findViewById(R.id.kosar_index);
            holder.tvname = (TextView) view.findViewById(R.id.kosar_name);
            holder.tvcounter = (TextView) view.findViewById(R.id.kosar_count);
            holder.tvsum = (TextView) view.findViewById(R.id.kosar_sum);
            holder.btnmodosit= (Button) view.findViewById(R.id.kosar_mod_button);

            holder.tvindex.setText(item.getIndex());
            holder.tvname.setText(item.getProductName());
            holder.tvcounter.setText(item.getProductCnt());
            holder.tvsum.setText(item.getProductAmountSum());
            holder.btnmodosit.setTag(item);

            holder.btnmodosit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Kosar.this, EtelekReszlet.class);
                    KosarItem item = (KosarItem) v.getTag();
                    intent.putExtra("etelID",item.getProductId());
                    intent.putExtra("eteladdID",item.getAdditionalCategory());
                    intent.putExtra("needaddID",Boolean.parseBoolean(item.getNeedAdditional()));

                    intent.putExtra("etelNev",item.getProductName());
                    intent.putExtra("etelAr",(Integer)(Integer.parseInt(item.getProductAmountSum())/Integer.parseInt(item.getProductCnt())));
                    intent.putExtra("etelLeiras",item.getProductDetail());
                    intent.putExtra("etelfilesize",item.getFileSize());
                    intent.putExtra("etelLink",GlobalHelper.Website+item.getPictureLink().substring(1));

                    startActivity(intent);
                }
            });
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.kosar_item_sub, null);

            ViewHolder_sub holder = new ViewHolder_sub();

            holder.tvname = (TextView) view.findViewById(R.id.kosar_name);
            holder.tvcounter = (TextView) view.findViewById(R.id.kosar_count);
            holder.tvsum = (TextView) view.findViewById(R.id.kosar_sum);

            holder.tvname.setText(item.getProductName());
            holder.tvcounter.setText(item.getProductCnt());
            holder.tvsum.setText(item.getProductAmountSum());
        }

        tr.addView(view);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        });

    }

    static class ViewHolder_main {
        TextView tvindex;
        TextView tvname;
        TextView tvcounter;
        Button btnmodosit;
        TextView tvsum;
    }

    static class ViewHolder_sub {
        TextView tvname;
        TextView tvcounter;
        TextView tvsum;
    }
}
