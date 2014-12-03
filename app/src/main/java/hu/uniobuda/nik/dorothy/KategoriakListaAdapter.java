package hu.uniobuda.nik.dorothy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hu.uniobuda.nik.dorothy.R;

/**
 * Created by asilaydying on 11/5/2014.
 */
public class KategoriakListaAdapter extends BaseAdapter {

    private Activity activity;

    public KategoriakListaAdapter(Activity act) {
        this.activity = act;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Kategoriak.kategoriakLista.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Kategoriak.kategoriakLista.get(position).Category_ID;
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
            convertView = inflater.inflate(R.layout.kategoria_lista_elem,null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtText = (TextView) convertView.findViewById(R.id.KategoriaNev);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.kepKat);

        holder.txtText.setText(Kategoriak.kategoriakLista.get(position).Category_name);
       // imageLoader.DisplayImage(GlobalHelper.KategoriaKepLink+Kategoriak.Category_ID.get(position) + ".jpg", holder.imgThumb);

        holder.imgThumb.setImageBitmap(Kategoriak.kategoriakLista.get(position).KategoriaKep);
        return convertView;
    }

    static class ViewHolder {
        TextView txtText;
        ImageView imgThumb;
    }
}
