package de.tum.mw.ftm.praktikum.wghelper;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static de.tum.mw.ftm.praktikum.wghelper.LoginActivity.USERID;
import static de.tum.mw.ftm.praktikum.wghelper.LoginActivity.WGID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    TextView userName, WGIDView;
    public static String WG_ID_STR;
    public static String BENUTZER_ID_STR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                .add(R.id.container, new FragmentKuehlschrank()).commit(); }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        WGIDView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_wgid);
        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFGROUP_USER, Context.MODE_PRIVATE);
        userName.setText("Hallo "+prefs.getString(LoginActivity.USERNAME, getResources().getString(R.string.header_subname)));
        //WGID.setText(prefs.getString(LoginActivity.WGID, getResources().getString(R.string.header_subsubname)));
        WG_ID_STR = getSharedPreferences(LoginActivity.PREFGROUP_USER, MODE_PRIVATE).getString(WGID, "null");
        BENUTZER_ID_STR = getSharedPreferences(LoginActivity.PREFGROUP_USER, MODE_PRIVATE).getString(USERID, "null");
        WGIDView.setText("Deine WG-ID lautet: "+WG_ID_STR);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentManager = getFragmentManager();


        if (id == R.id.nav_kuelschrank) {
            FragmentKuehlschrank fragmentKuehlschrank = new FragmentKuehlschrank();
            fragmentManager.beginTransaction().replace(R.id.container, fragmentKuehlschrank).commit();

        } else if (id == R.id.nav_kasse) {
            FragmentKasse fragmentKasse = new FragmentKasse();
            fragmentManager.beginTransaction().replace(R.id.container, fragmentKasse).commit();

        } else if (id == R.id.nav_gießme) {
            FragmentGiessme fragmentGiessme = new FragmentGiessme();
            fragmentManager.beginTransaction().replace(R.id.container, fragmentGiessme).commit();

        } else if (id == R.id.nav_trinkspiele) {
            FragmentTrinkspiele fragmentTrinkspiele = new FragmentTrinkspiele();
            fragmentManager.beginTransaction().replace(R.id.container, fragmentTrinkspiele).commit();

        } else if (id == R.id.nav_settings) {
            /* FragmentSettings fragmentSettings = new FragmentSettings();
            fragmentManager.beginTransaction().replace(R.id.container, fragmentSettings).commit(); */

        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(LoginActivity.PREFGROUP_USER, MODE_PRIVATE).edit();
            editor.putBoolean(LoginActivity.IS_LOGGED_IN, false).apply();
            SharedPreferences.Editor ed = getApplicationContext().getSharedPreferences(LoginActivity.PREFGROUP_WG, MODE_PRIVATE).edit();
            ed.putBoolean(LoginActivity.IS_REGISTERED, false).apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
