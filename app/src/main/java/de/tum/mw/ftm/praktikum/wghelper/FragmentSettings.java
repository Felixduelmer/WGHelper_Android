package de.tum.mw.ftm.praktikum.wghelper;

import android.preference.PreferenceFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
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

import static de.tum.mw.ftm.praktikum.wghelper.LoginActivity.strwgpasswort;
import static de.tum.mw.ftm.praktikum.wghelper.MainActivity.WG_ID_STR;


public class FragmentSettings extends PreferenceFragment {
    public static final String MITGLIEDER_URL= "http://pr-android.ftm.mw.tum.de/android/wghelper/Mitgliederauslesen.php";
    private List<Bewohner> list = new ArrayList<Bewohner>();
    private BewohnerAdapter bewohnerAdapter;
    private ListView mitgliederlist;
    private TextView passwortview;



    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        new getMember().execute(MITGLIEDER_URL,WG_ID_STR);
        mitgliederlist = (ListView) view.findViewById(R.id.Mitglieder);
        bewohnerAdapter = new BewohnerAdapter(getActivity(), R.layout.bewohneradapter, list);
        mitgliederlist.setAdapter(bewohnerAdapter);
        passwortview = (TextView) view.findViewById(R.id.PW);

        passwortview.setText(strwgpasswort);



        return view;
    }


    public class getMember extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {

            HttpURLConnection httpURLConnection = null;
            try {
//Verbindung zum Server aufbauen
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
//Name und Passwort encoden, damit der Server diese aus dem "name" und "password" Feld auslesen knn
                String data = URLEncoder.encode("WG_ID", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
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
                    for (int i = 0; i<jsonArray.length(); i++){

                        try {
                            list.add(new Bewohner(jsonArray.getJSONObject(i)));
                            bewohnerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

        }
    }
}
