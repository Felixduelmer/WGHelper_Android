package de.tum.mw.ftm.praktikum.wghelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by felix on 29.01.2018.
 */

public class Essen {

    private int WG_ID, Nummer;
    private boolean Zustand;
    private String Art;

    public Essen(JSONObject jsonObject) {
        try {
            this.WG_ID = jsonObject.getInt("WG_ID");
            Nummer = jsonObject.getInt("Zahl");
            Zustand = jsonObject.getBoolean("Zustand");
            Art = jsonObject.getString("art");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getWG_ID() {
        return WG_ID;
    }

    public void setWG_ID(int WG_ID) {
        this.WG_ID = WG_ID;
    }

    public int getNummer() {
        return Nummer;
    }

    public void setNummer(int nummer) {
        Nummer = nummer;
    }

    public boolean isZustand() {
        return Zustand;
    }

    public void setZustand(boolean zustand) {
        Zustand = zustand;
    }

    public String getArt() {
        return Art;
    }

    public void setArt(String art) {
        Art = art;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
