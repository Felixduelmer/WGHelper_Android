package de.tum.mw.ftm.praktikum.wghelper;

import android.app.AlertDialog;
import android.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabischramm on 07.01.18.
 */

public class FragmentGiessme extends Fragment implements View.OnClickListener {

    private static String TAG = FragmentGiessme.class.getSimpleName();

    public static int REQUEST_CODE = 18913;
    public static String BLUME = "BLUME";

    private Button hzfgGiessme;
    private List<String> blumen = new ArrayList();
    private BlumenAdapter adapter;
    private ListView plantid;

    public FragmentGiessme() {
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_giessme, container, false);

        hzfgGiessme = view.findViewById(R.id.hzfg);
        hzfgGiessme.setOnClickListener(this);

        plantid = view.findViewById(R.id.plantid);
        adapter = new BlumenAdapter(getActivity(), R.layout.test, blumen);
        plantid.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.hzfg:
                Intent intent = new Intent(getActivity(), NewPlantActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Wasser aber zackig")
                .setMessage(message)

//              .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialoginterface, int i) {
//                      dialoginterface.cancel();}})

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String selected = data.getStringExtra(BLUME);
        Log.d(TAG, selected);
        blumen.add(selected);
        adapter.notifyDataSetChanged();
    }
}
