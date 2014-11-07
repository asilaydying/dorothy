package com.example.asilaydying.dorothy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by asilaydying on 11/5/2014.
 */
public class KategoriakListaAdapter extends BaseAdapter {

    private Activity activity;

    public KategoriakListaAdapter(Activity act) {
        this.activity = act;
        //imageLoader = new ImageLoader(act);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Kategoriak.Category_ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item,null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtText = (TextView) convertView.findViewById(R.id.KategoriaNev);
        //  holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

        holder.txtText.setText(CategoryList.Category_name.get(position));
        // imageLoader.DisplayImage(Utils.AdminPageURL+CategoryList.Category_image.get(position), holder.imgThumb);

        return convertView;
    }

    static class ViewHolder {
        TextView txtText;
        // ImageView imgThumb;
    }
}
