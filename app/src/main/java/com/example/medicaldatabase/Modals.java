package com.example.medicaldatabase;

public class Modals {
    private  String ID;
    private String NAME;
    private String DATE;
    private String TIME;

    public Modals() {
    }

    public Modals(String ID, String NAME, String DATE, String TIME) {
        this.ID = ID;
        this.NAME = NAME;
        this.DATE = DATE;
        this.TIME = TIME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }
}
