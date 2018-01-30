package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static de.tum.mw.ftm.praktikum.wghelper.MainActivity.BENUTZER_ID_STR;
import static de.tum.mw.ftm.praktikum.wghelper.MainActivity.WG_ID_STR;

/**
 * Created by fabischramm on 28.12.17.
 */

public class FragmentKasse extends Fragment {

    private static final String SALDO_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/saldo.php";
    private static final String SALDOVIEW_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/saldoview.php";
    private Button kasse_btn;
    private Button hilfe_btn;
    private EditText kasse_wert;
    private String wert;

    private List<Bewohner> list = new ArrayList<Bewohner>();
    private ListView listView;
    private ArrayAdapterSaldo arrayAdapterSaldo;

    public FragmentKasse() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new SaldoView().execute(SALDOVIEW_URL, WG_ID_STR, BENUTZER_ID_STR);



    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_kasse, container, false);



        kasse_btn = (Button) view.findViewById(R.id.kasse_btn);
        kasse_wert = (EditText) view.findViewById(R.id.kasse_wert);
        hilfe_btn = (Button) view.findViewById(R.id.hilfe_btn);

        listView = (ListView) view.findViewById(R.id.saldo_list) ;

        arrayAdapterSaldo = new ArrayAdapterSaldo(getActivity(),R.layout.saldo_list_row, list);
        listView.setAdapter(arrayAdapterSaldo);

        kasse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wert = kasse_wert.getText().toString();

                if (!wert.isEmpty()) {
                    new NewSaldo().execute(SALDO_URL, wert, WG_ID_STR, BENUTZER_ID_STR);
                    //FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //ft.detach(getParentFragment()).attach(getParentFragment()).commit();;
                    //list.[2]

                    new SaldoView().execute(SALDOVIEW_URL, WG_ID_STR, BENUTZER_ID_STR);

                    //arrayAdapterSaldo = new ArrayAdapterSaldo(getActivity(),R.layout.saldo_list_row, list);
                    //listView.setAdapter(arrayAdapterSaldo);
                }

               // Intent intent = new Intent(getActivity(), Saldo.class);
               // startActivity(intent);


            }
        });


        hilfe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());

                //setting custom layout to dialog
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Hilfe");

                //adding text dynamically
                TextView txt = (TextView) dialog.findViewById(R.id.textView);
                txt.setText("Gebe hier bitte den Betrag in Euro ein. " +
                        "Ein Einkauf von < 10,99€ > gibst du als < 10.99 > ein. Die App speichert alle Ausgaben" +
                        " und du kannst unter Kontostände eine Übersicht einsehen. ");

                ImageView image = (ImageView)dialog.findViewById(R.id.image);
                image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));

                //adding button click event
                Button dismissButton = (Button) dialog.findViewById(R.id.button);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();




            }
        });


        return view;



    }

    public class NewSaldo extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {
//Verbindung zum Server aufbauen
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
//Name und Passwort encoden, damit der Server diese aus dem "name" und "password" Feld auslesen knn
                String data = URLEncoder.encode("Wert", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("ID", "UTF-8")
                        + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("ID_Bewohner", "UTF-8")
                        + "=" + URLEncoder.encode(params[3], "UTF-8");

//Daten an Server schicken
                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();
//Antwort vom Server einlesen und als JSON zurückgeben
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return new JSONArray(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
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
        protected void onPostExecute(JSONArray jsonArray) {
            //super.onPostExecute(jsonObject);
            if (jsonArray != null) {

                arrayAdapterSaldo.clear();



                //List<Bewohner> list = new ArrayList<Bewohner>();
                for (int i = 0; i<jsonArray.length(); i++){

                    try {
                        list.add(new Bewohner(jsonArray.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                arrayAdapterSaldo.notifyDataSetChanged();
                }




                Log.d("DebugInfo", "NEWSALDO IST GELAUFEN");
                Log.i("Info", "NEWSALDO IST GELAUFEN");


                /*

                try {
                    if (jsonObject.getBoolean("success")) {
                        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                        try {
                            i.putExtra("mID", jsonObject.getJSONObject("WG").opt("val").toString());
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Da ist etwas schief gelaufen!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
               }
            */}
        }
    }


    public class SaldoView extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            try {
//Verbindung zum Server aufbauen
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
//Name und Passwort encoden, damit der Server diese aus dem "name" und "password" Feld auslesen knn
                String data = URLEncoder.encode("WG_ID", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("Bewohner_ID", "UTF-8")
                        + "=" + URLEncoder.encode(params[2], "UTF-8");

//Daten an Server schicken
                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();
//Antwort vom Server einlesen und als JSON zurückgeben
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return new JSONArray(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
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
        protected void onPostExecute(JSONArray jsonArray) {
            //super.onPostExecute(jsonObject);
            if (jsonArray != null) {


                arrayAdapterSaldo.clear();
                //List<Bewohner> list = new ArrayList<Bewohner>();
                for (int i = 0; i<jsonArray.length(); i++){

                    try {
                        list.add(new Bewohner(jsonArray.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                arrayAdapterSaldo.notifyDataSetChanged();




                Log.d("DebugInfo", "SALDOVIEW IST GELAUFEN");
                Log.i("Info", "SALDOVIEW IST GELAUFEN");


                /*

                try {
                    if (jsonObject.getBoolean("success")) {
                        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                        try {
                            i.putExtra("mID", jsonObject.getJSONObject("WG").opt("val").toString());
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Da ist etwas schief gelaufen!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
               }
            */}
        }


    }

}
