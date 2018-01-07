package de.tum.mw.ftm.praktikum.wghelper.Login;

import android.os.AsyncTask;
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

import de.tum.mw.ftm.praktikum.wghelper.LoginActivity;

/**
 * Created by felix on 07.01.2018.
 */

public class LoginTaskActivity extends AsyncTask<String, Void, JSONObject> {

    private EditText username;


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
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);

                }
                return new JSONObject(sb.toString());
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
                        LoginActivity.Login.login(jsonObject.getLong("userid"), username.getText().toString());
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
}
