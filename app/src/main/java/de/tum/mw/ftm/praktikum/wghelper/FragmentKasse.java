package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by fabischramm on 28.12.17.
 */

public class FragmentKasse extends Fragment {

    private static final String SALDO_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/saldo.php";
    private Button kasse_btn;
    private EditText kasse_wert;
    private String wert;
    private Bewohner bewohner;

    public FragmentKasse() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_kasse, container, false);


        kasse_btn = (Button) view.findViewById(R.id.kasse_btn);
        kasse_wert = (EditText) view.findViewById(R.id.kasse_wert);

        kasse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wert = kasse_wert.getText().toString();

                if (!wert.isEmpty()) {
                    new NewSaldo().execute(SALDO_URL, wert, "1", "4");
                }


            }
        });


        return view;
    }

    public class NewSaldo extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
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
                return new JSONObject(sb.toString());
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
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                Log.d("DebugInfo", "Passt");
                Log.i("Info", "Passt"); /*

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
