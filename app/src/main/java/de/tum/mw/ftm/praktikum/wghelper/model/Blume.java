package de.tum.mw.ftm.praktikum.wghelper.model;

/**
 * Created by Maxan on 26.01.18.
 */

public class Blume {
    private String name;
    private String ort;
    private long gegossen;

    public Blume() {
    }

    public Blume(String name, String ort, long gegossen) {
        this.name = name;
        this.ort = ort;
        this.gegossen = gegossen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public long getGegossen() {
        return gegossen;
    }

    public void setGegossen(long gegossen) {
        this.gegossen = gegossen;
    }
}
