package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by supergep on 2014.11.11..
 */
public class EtelekListaAdapter extends BaseAdapter {

    private Activity activity;

    public EtelekListaAdapter(Activity act) {
        this.activity = act;
        //imageLoader = new ImageLoader(act);
    }

    @Override
    public int getCount() {
        return Etelek.eteleklista.size();
    }

    @Override
    public Object getItem(int position) {
        return Etelek.eteleklista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.etelek_lista_elem,null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtName = (TextView) convertView.findViewById(R.id.EtelNev);
        holder.txtPrice = (TextView) convertView.findViewById(R.id.EtelAr);
        holder.txtDesc = (TextView) convertView.findViewById(R.id.EtelLeiras);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.etelKep);
        try {
            holder.txtName.setText(Etelek.eteleklista.get(position).getName());
            holder.txtPrice.setText(Etelek.eteleklista.get(position).getPrice());
            holder.txtDesc.setText(Etelek.eteleklista.get(position).getDesc());
            holder.imgThumb.setImageBitmap(Etelek.eteleklista.get(position).getEtelKep());
        }
        catch (Exception e)
        {
            Log.d("Category name", String.valueOf(e.getStackTrace()));
        }
        return convertView;
    }
    static class ViewHolder {
        TextView txtName;
        TextView txtPrice;
        TextView txtDesc;
        ImageView imgThumb;
    }
}
