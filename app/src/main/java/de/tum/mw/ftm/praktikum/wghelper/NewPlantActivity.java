package de.tum.mw.ftm.praktikum.wghelper;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class NewPlantActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    public Button hzfg;
    String [] blumenlistearray = {"Sanseviera","Sonnenblume", "Elefantenfuß", "Efeutute", "Mimose", "Schusterpalme", "Drachenbaum", "Rose"};
    //String [] zimmerlistearray = {"Wohnzimmer", "Schlafzimmer", "Küche", "Balkon"};
    private String blume = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);

        hzfg = findViewById(R.id.hzfg);
        hzfg.setOnClickListener(this);


        //ID der Listview im Layout
        ListView blumenlist = findViewById(R.id.blumenlist);
        //ArrayAdapter mit Liste und SingleChoice
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, blumenlistearray);
        //Adapeter und Listview zusammenführen
        blumenlist.setAdapter(adapter);
        //SingleChoice einfügen
        blumenlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        blumenlist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Toast.makeText(this, blumenlistearray[position], Toast.LENGTH_LONG).show();
        blume = blumenlistearray[position];
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.hzfg:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(FragmentGiessme.BLUME, blume);
                setResult(FragmentGiessme.REQUEST_CODE, resultIntent);
                NewPlantActivity.this.finish();
                break;

        }
    }
}

