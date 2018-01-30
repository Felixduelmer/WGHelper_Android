package de.tum.mw.ftm.praktikum.wghelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabischramm on 13.01.18.
 */

public class Bewohner {
    private int ID;
    private int Bewohner_ID;
    private int Saldo;
    private String name;

    public Bewohner(JSONObject object) {

        try {
            this.ID = object.getInt("ID");
            this.Bewohner_ID = object.getInt("ID_Bewohner");
            this.Saldo = object.getInt("Saldo");
            this.name = object.getString("Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBewohner_ID() {
        return Bewohner_ID;
    }

    public void setBewohner_ID(int bewohner_ID) {
        Bewohner_ID = bewohner_ID;
    }

    public int getSaldo() {
        return Saldo;
    }

    public void setSaldo(int saldo) {
        Saldo = saldo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

