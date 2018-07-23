package com.prittysoft.jat;


public class MainMeasurements {
    private String ID ;
    private String client;
    private String equipment;
    private String equipment_serial;
    private String date;
    private String equipment_model;

    MainMeasurements(String ID, String client, String equipment, String equipment_serial, String equipment_model) {
        this.ID = ID;
        this.client = client;
        this.equipment = equipment;
        this.equipment_serial = equipment_serial;
        this.equipment_model = equipment_model;
    }

    public String getID(){
        return ID;
    }

    public String getClient() {
        return client;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getEquipment_serial() {
        return equipment_serial;
    }

    public String getDate() {
        return date;
    }

    public String getEquipment_model() {
        return equipment_model;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setEquipment_serial(String equipment_serial) {
        this.equipment_serial = equipment_serial;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
