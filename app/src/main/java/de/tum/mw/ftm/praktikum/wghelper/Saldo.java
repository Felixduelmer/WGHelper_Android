package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by fabischramm on 14.01.18.
 */

public class Saldo extends Activity {
    InputStream is;
    JSONObject json_data;
    TableLayout table;
	TextView columnOneText;
	TextView columnTwoText;
	String columnOneString;
	String columnTwoString;
	private static final String KONTOSTAND_URL = "http://pr-android.ftm.mw.tum.de/android/wghelper/kontostand.php";

	private Button zurueck;

        	@Override
	public void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.layout);

        	zurueck =(Button) findViewById(R.id.zurueck);

			zurueck.setOnClickListener(new View.OnClickListener() {
											  @Override
											  public void onClick(View v) {
												  Intent intent = new Intent(Saldo.this, MainActivity.class);
												  startActivity(intent);
											  }
										  }

			);

				new Kontostand().execute(KONTOSTAND_URL, "1");
        	}

    public class Kontostand extends AsyncTask<String, Void, JSONObject> {


		String result = "";
        @Override
        protected JSONObject doInBackground(String... strings) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			try	{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://pr-android.ftm.mw.tum.de/android/wghelper/kontostand.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}catch(Exception e){
				Log.e("log_tag", "Fehler bei der http Verbindung "+e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "n");
				}
				is.close();
				result=sb.toString();
			}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}


            return null;
        }

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			try {
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					columnOneString = json_data.get("Name").toString();
					columnTwoString = json_data.get("Saldo").toString();
					fillList();
				}
			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}


		}
	}



        	public void fillList() {

        	table = (TableLayout) findViewById(R.id.TableLayout01);

        	TableRow row = new TableRow(this);
        	columnOneText = new TextView(this);
        	columnTwoText = new TextView(this);
        	columnOneText.setText(columnOneString);
        	columnTwoText.setText(columnTwoString);
        	row.addView(columnOneText);
         row.addView(columnTwoText);

         table.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        	}
}


