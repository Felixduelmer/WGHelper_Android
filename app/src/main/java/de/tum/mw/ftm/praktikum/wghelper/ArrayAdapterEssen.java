package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.List;

/**
 * Created by felix on 29.01.2018.
 */

public class ArrayAdapterEssen extends ArrayAdapter<Essen>{

    private Context context;
    private List<Essen> listexist;
    private int resource;

    private static final String CHANGELEBENSMITEL = "http://pr-android.ftm.mw.tum.de/android/wghelper/lebensmittelchange.php";

    public ArrayAdapterEssen(@NonNull Context context, int resource, @NonNull List<Essen> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listexist = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        TextView txtEssen = (TextView) rowView.findViewById(R.id.refrig_food);
        Button btn_change = (Button) rowView.findViewById(R.id.refrig_change);

        txtEssen.setText(listexist.get(position).getArt());
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean istrue = listexist.get(position).isZustand();
                istrue ^= true;
                String stristrue = Boolean.toString(istrue);
                String strnummer = Integer.toString(listexist.get(position).getNummer());
                String strwgid = Integer.toString(listexist.get(position).getWG_ID());

                if(!stristrue.isEmpty() && !strnummer.isEmpty() && !strwgid.isEmpty()){
                    new ChangeFood().execute(CHANGELEBENSMITEL, strwgid, strnummer, stristrue);
                }

            }
        });


        return rowView;
    }

    public class ChangeFood extends AsyncTask<String, Void, JSONObject> {

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
                String data = URLEncoder.encode("WG_ID", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("Nummer", "UTF-8")
                        + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("Status", "UTF-8")
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
                try {
                    if (jsonObject.getBoolean("success")) {

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(intent);



                    }
                else {
                        Toast.makeText(getContext().getApplicationContext(), "Es ist ein Fehler aufgetreten!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
