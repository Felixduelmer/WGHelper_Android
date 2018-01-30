package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import static de.tum.mw.ftm.praktikum.wghelper.LoginActivity.WGID;

/**
 * Created by felix on 24.01.2018.
 */

public class Pop extends Activity{

    Button btn_hinzu;
    EditText Lebensmittel;
    CheckBox cb;
    Boolean checkbox = false;
    String WG_ID;


    private static final String LEBENSMITTEL_ADD = "http://pr-android.ftm.mw.tum.de/android/wghelper/LEBENSMITTEL_ADD.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);


        WG_ID = getSharedPreferences(LoginActivity.PREFGROUP_WG, MODE_PRIVATE).getString(WGID, "hallo");



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        btn_hinzu = (Button) findViewById(R.id.btn_newfood);
        Lebensmittel = (EditText) findViewById(R.id.pop_text);
        cb = (CheckBox) findViewById(R.id.pop_checkbox);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    checkbox = true;
                }
                else{
                    checkbox = false;
                }
            }
        });
        btn_hinzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strtext = Lebensmittel.getText().toString();
                String strcheckbox = String.valueOf(checkbox);
                String strwgid = WG_ID;
                if(!strtext.isEmpty()){
                    new New_Food().execute(LEBENSMITTEL_ADD, strtext, strcheckbox, strwgid);
            }
        }
    });

}

    public class New_Food extends AsyncTask<String, Void, JSONObject> {


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
                String data = URLEncoder.encode("strtext", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("strcheckbox", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                data += "&" + URLEncoder.encode("strwgid", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Einfügen nicht erfolgreich", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e){
                    e.printStackTrace();

                }
            }
        }
    }
}

