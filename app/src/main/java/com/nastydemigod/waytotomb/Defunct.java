package com.nastydemigod.waytotomb;

public class Defunct {

    public String getFNO() {
        return FNO;
    }

    public void setFNO(String FNO) {
        this.FNO = FNO;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getCemen() {
        return cemen;
    }

    public void setCemen(String cemen) {
        this.cemen = cemen;
    }

    public String getZah() {
        return zah;
    }

    public void setZah(String zah) {
        this.zah = zah;
    }

    public String getUchast() {
        return uchast;
    }

    public void setUchast(String uchast) {
        this.uchast = uchast;
    }

    public String getLocahion() {
        return locahion;
    }

    public void setLocahion(String locahion) {
        this.locahion = locahion;
    }

    public Defunct(String FNO, String dates, String cemen, String zah, String uchast, String locahion) {
        this.FNO = FNO;
        this.dates = dates;
        this.cemen = cemen;
        this.zah = zah;
        this.uchast = uchast;
        this.locahion = locahion;
    }

    private String FNO;
    private String dates;
    private String cemen;
    private String zah;
    private String uchast;
    private String locahion;
}
