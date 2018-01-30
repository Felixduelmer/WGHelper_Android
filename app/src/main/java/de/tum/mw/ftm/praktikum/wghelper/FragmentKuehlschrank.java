package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static de.tum.mw.ftm.praktikum.wghelper.LoginActivity.WGID;
import static de.tum.mw.ftm.praktikum.wghelper.MainActivity.WG_ID_STR;

/**
 * Created by fabischramm on 07.01.18.
 */

public class FragmentKuehlschrank extends Fragment {

    private Button refresh_btn, food;
    private EditText newfood;
    private FloatingActionButton fab;

    private List<Essen> list1 = new ArrayList<Essen>();
    private List<Essen> list2 = new ArrayList<Essen>();
    private ListView listviewex, listviewnot;
    private ArrayAdapterEssen arrayAdapterEssen1, arrayAdapterEssen2;
    private Essen essen1, essen2;
    private static final String LEBENSMITTELCHECKPOSITIV = "http://pr-android.ftm.mw.tum.de/android/wghelper/Lebensmittelcheckpositiv.php";
    private static final String LEBENSMITTELCHECKNEGATIV = "http://pr-android.ftm.mw.tum.de/android/wghelper/Lebensmittelchecknegativ.php";


    private static final String SALDO_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/kuehlschrank.php";

    public FragmentKuehlschrank() {
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_kuehlschrank, container, false);

        //WG_ID = getSharedPreferences(LoginActivity.PREFGROUP_WG, MODE_PRIVATE).getString(WGID, "hallo");
        refresh_btn = (Button) view.findViewById(R.id.refresh_refrig);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);


        listviewex = (ListView) view.findViewById(R.id.listview_existing);
        listviewnot = (ListView) view.findViewById(R.id.listview_needed);

        new Lebenspositiv().execute(LEBENSMITTELCHECKPOSITIV, WG_ID_STR);
        new Lebensnegativ().execute(LEBENSMITTELCHECKNEGATIV, WG_ID_STR);



        arrayAdapterEssen1 = new ArrayAdapterEssen(getActivity(), R.layout.listview_kuehlschrank_item, list2);
        arrayAdapterEssen2 = new ArrayAdapterEssen(getActivity(), R.layout.listview_kuehlschrank_item, list1);


        listviewex.setAdapter(arrayAdapterEssen1);
        listviewnot.setAdapter(arrayAdapterEssen2);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Pop.class);
                startActivity(i);


            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //new Lebenspositiv().execute(LEBENSMITTELCHECKPOSITIV, "83");
                //new Lebensnegativ().execute(LEBENSMITTELCHECKNEGATIV, "83");
                //Intent intent = new Intent(getActivity(), MainActivity.class);
                //startActivity(intent);
                //arrayAdapterEssen1.notifyDataSetChanged();

            }


        });


        return view;
    }


    public class Lebenspositiv extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                String data = URLEncoder.encode("WG_ID", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);

                }
                return new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {

                try {
                    JSONArray ja = jsonObject.getJSONArray("positiv");
                    arrayAdapterEssen1.clear();
                    //List<Bewohner> list = new ArrayList<Bewohner>();
                    for (int i = 0; i < ja.length(); i++) {


                        list2.add(new Essen(ja.getJSONObject(i)));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapterEssen1.notifyDataSetChanged();

            }


        }
    }


    public class Lebensnegativ extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                String data = URLEncoder.encode("WG_ID", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);

                }
                return new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {


                try {
                    JSONArray ja = jsonObject.getJSONArray("negativ");
                    arrayAdapterEssen2.clear();
                    for (int i = 0; i < ja.length(); i++) {


                        list1.add(new Essen(ja.getJSONObject(i)));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrayAdapterEssen2.notifyDataSetChanged();

            }

        }
    }
}




