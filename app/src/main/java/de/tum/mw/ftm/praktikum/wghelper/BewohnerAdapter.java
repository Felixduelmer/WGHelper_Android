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

public class BewohnerAdapter extends ArrayAdapter<Bewohner> {

    private Context context;
    private List<Bewohner> bewohnerList;
    private int resource;


    public BewohnerAdapter(@NonNull Context context, int resource, @NonNull List<Bewohner> objects) {

        super(context, resource, objects);
        this.context = context;
        this.bewohnerList = objects;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        final TextView txtName = rowView.findViewById(R.id.bewohnerliste);

        txtName.setText(bewohnerList.get(position).getName());
        return rowView;
    }
}