package de.tum.mw.ftm.praktikum.wghelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by fabischramm on 23.01.18.
 */

public class ArrayAdapterSaldo extends ArrayAdapter<Bewohner> {

    private Context context;
    private List<Bewohner> bewohnerList;
    private int resource;


    public ArrayAdapterSaldo(@NonNull Context context, int resource, @NonNull List<Bewohner> objects) {

        super(context, resource, objects);
        this.context = context;
        this.bewohnerList = objects;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        TextView txtName = (TextView) rowView.findViewById(R.id.list_saldo_name);
        TextView txtSaldo = (TextView) rowView.findViewById(R.id.list_saldo_saldo);

        txtName.setText(bewohnerList.get(position).getName());
        txtSaldo.setText(String.valueOf(bewohnerList.get(position).getSaldo()));
        return rowView;
    }
}
