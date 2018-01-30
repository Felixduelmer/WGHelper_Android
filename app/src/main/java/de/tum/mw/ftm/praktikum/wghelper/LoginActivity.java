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
import android.widget.Toast;

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
import java.util.Objects;

/**
 * Created by felix on 14.12.2017.
 */

public class LoginActivity extends AppCompatActivity {

    //Keys für den Intent
    public static final String PREFGROUP_WG = "de.tum.mw.ftm.praktikum.android.wghelper.PREFGROUP_WG";
    public static final String PREFGROUP_USER = "de.tum.mw.ftm.praktikum.android.wghelper.PREFGROUP_USER";
    public static final String IS_REGISTERED = "de.tum.mw.ftm.praktikum.android.wghelper.IS_REGISTERED";
    public static final String USERNAME = "de.tum.mw.ftm.praktikum.android.wghelper.USERNAME";
    public static final String PASSWORT = "de.tum.mw.ftm.praktikum.android.wghelper.PASSWORT";
    public static final String PASSWORTVOR = "de.tum.mw.ftm.praktikum.android.wghelper.PASSWORTVOR";
    public static final String WGID = "de.tum.mw.ftm.praktikum.android.wghelper.WGID";
    public static final String USERID = "de.tum.mw.ftm.praktikum.android.wghelper.USERID";
    public static final String IS_LOGGED_IN = "de.tum.mw.ftm.praktikum.android.wghelper.IS_LOGGED_IN";
    private static final String LOGIN_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/login.php";
    public static String strwgpasswort;

    private Button btnRegister;
    private Button btnLogin;
    private EditText username, wgpasswort, wg_id;
    private static long backpressed=0;
    String passwort = "hallo", nice = "haha";


    @Override
    public void onBackPressed() {

        Toast w =Toast.makeText(getBaseContext(), "Press Back again to Exit", Toast.LENGTH_LONG);
        w.show();
        backpressed = backpressed+1;
        if (backpressed>1){
            this.finish();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        //Gleich MainActivity starten wenn User noch eingeloggt
        if (getSharedPreferences(PREFGROUP_USER, MODE_PRIVATE).getBoolean(IS_LOGGED_IN, false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();

        //Referenz auf Layoutelemente erhalten
        btnLogin = (Button) findViewById(R.id.log_btn);
        username = (EditText) findViewById(R.id.log_user);
        wgpasswort = (EditText) findViewById(R.id.log_wgschluessel);
        wg_id = (EditText) findViewById(R.id.log_wg_id);
        btnRegister = (Button) findViewById(R.id.log_btn_new_WG);

        //OnClickListener setzen um auf Click Events reagieren zu können

        if (getSharedPreferences(PREFGROUP_WG, MODE_PRIVATE).getBoolean(IS_REGISTERED, false)) {
                wgpasswort.setText(getSharedPreferences(PREFGROUP_WG, MODE_PRIVATE).getString(PASSWORTVOR, nice));
                wg_id.setText(getSharedPreferences(PREFGROUP_WG, MODE_PRIVATE).getString(WGID, passwort));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = username.getText().toString();
                strwgpasswort = wgpasswort.getText().toString();
                String strwgid = wg_id.getText().toString();
                if(!strwgpasswort.isEmpty() && !strUsername.isEmpty() && !strwgid.isEmpty()){
                    new LoginTask().execute(LOGIN_URL, strUsername, strwgid, strwgpasswort);
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });



    }

    //Speichert Nutzerdaten als SharedPreferences ab
        public void login(String id, String username, String wgid) {
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(PREFGROUP_USER, MODE_PRIVATE).edit();
            editor.putString(WGID, wgid).apply();
            editor.putString(USERID, id).apply();
            editor.putString(USERNAME, username).apply();
            editor.putBoolean(IS_LOGGED_IN, true).apply();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);


        }

    public class LoginTask extends AsyncTask<String, Void, JSONObject>{


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
                String data = URLEncoder.encode("Username", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("WG_ID", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("WG_Passwort", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
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
            if(jsonObject != null){
                try {
                    if (jsonObject.getBoolean("success")) {
                        login(jsonObject.getString("bewohnerid"), jsonObject.getString("username"), jsonObject.getString("wgid"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Login nicht erfolgreich", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();

        }
    }
    }
    }
}

