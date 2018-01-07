package de.tum.mw.ftm.praktikum.wghelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
 * Created by felix on 23.12.2017.
 */

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    }

    public class NewWGTask extends AsyncTask<String, Void, JSONObject> {

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
                String data = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8")
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
                return new JSONObject(sb.toString());
            } catch (MalformedURLException e) {e.printStackTrace();
            } catch (IOException e) {e.printStackTrace();
            } catch (JSONException e) {e.printStackTrace();
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
            if(jsonObject != null){
                try {
                    if(jsonObject.getBoolean("success")){
                        LoginActivity.Login(jsonObject.getLong("userid"), username.getText().toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "User existiert bereits",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
