package hu.uniobuda.nik.dorothy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import hu.uniobuda.nik.dorothy.R;

/**
 * Created by supergep on 2014.11.14..
 */
public class EtelekReszletListaAdapter extends BaseAdapter {

    private Activity activity;

    public EtelekReszletListaAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return EtelekReszlet.etelekreszleteklista.size();
    }

    @Override
    public Object getItem(int position) {
        return EtelekReszlet.etelekreszleteklista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.etelek_reszlet_lista_elem,null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lista = (Spinner) convertView.findViewById(R.id.lista);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, EtelekReszlet.addlista);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        int selected = adapter.getPosition(EtelekReszlet.etelekreszleteklista.get(position).getName());

        holder.lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                EtelekReszlet.etelekreszleteklista.get(position).setAddID(EtelekReszlet.addlistakey.get(position2));
                EtelekReszlet.etelekreszleteklista.get(position).setName(EtelekReszlet.addlista.get(position2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.lista.setAdapter(adapter);
        holder.lista.setSelection(selected);

        return convertView;
    }

    static class ViewHolder {
        Spinner lista;
    }
}
