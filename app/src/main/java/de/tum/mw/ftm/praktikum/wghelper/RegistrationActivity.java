package de.tum.mw.ftm.praktikum.wghelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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


    private static final String REGISTER_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/registrieren.php";
    private static final  String WG_ID= "de.tum.mw.ftm.praktikum.wghelper.EXTRA_WGID";


    private EditText Passwort, WG_Name;
    private NumberPicker Anzahl;
    private Button btn_new_wg;
    private WG wg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Referenz auf Layoutelemente erhalten
        btn_new_wg = (Button) findViewById(R.id.log_btn_create_wg);
        Passwort = (EditText) findViewById(R.id.log_passwort);
        Anzahl = (NumberPicker) findViewById(R.id.log_teilnehmer);
        WG_Name = (EditText) findViewById(R.id.log_wgname);
        Anzahl.setMinValue(1);
        Anzahl.setMaxValue(20);





        btn_new_wg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int strAn = Anzahl.getValue();
                String strAnzahl = Integer.toString(strAn);
                String strWG_Name = WG_Name.getText().toString();
                String strPassword = Passwort.getText().toString();

                if (!strWG_Name.isEmpty() && !strAnzahl.isEmpty() && !strPassword.isEmpty()) {
                    new NewWG().execute(REGISTER_URL, strWG_Name, strAnzahl, strPassword);
                }


            }
        });
    }

    public class NewWG extends AsyncTask<String, Void, JSONObject> {

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
                String data = URLEncoder.encode("WG_Name", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("Anzahl", "UTF-8")
                        + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("Passwort", "UTF-8")
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
                        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);

                        try {

                            i.putExtra("WG", jsonObject.getString("WG"));
                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(LoginActivity.PREFGROUP_WG,MODE_PRIVATE).edit();
                            editor.putBoolean(LoginActivity.IS_REGISTERED, true);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "WG existiert bereits oder es ist etwas schief gelaufen!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
   public class WG{

        private String WG_Name, Passwort;
        private int WG_ID;
        private Float Anzahl;

       public WG(String WG_Name, String passwort, int WG_ID, Float anzahl) {
           this.WG_Name = WG_Name;
           Passwort = passwort;
           this.WG_ID = WG_ID;
           Anzahl = anzahl;
       }

       public String getWG_Name() {
           return WG_Name;
       }

       public void setWG_Name(String WG_Name) {
           this.WG_Name = WG_Name;
       }

       public String getPasswort() {
           return Passwort;
       }

       public void setPasswort(String passwort) {
           Passwort = passwort;
       }

       public int getWG_ID() {
           return WG_ID;
       }

       public void setWG_ID(int WG_ID) {
           this.WG_ID = WG_ID;
       }

       public Float getAnzahl() {
           return Anzahl;
       }

       public void setAnzahl(Float anzahl) {
           Anzahl = anzahl;
       }
   }

    }

