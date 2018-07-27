package com.prittysoft.jat;

public class SondaMeasurements {

    private String number;
    private String time;
    private String temperature;

    SondaMeasurements(String number, String time, String temperature) {
        this.number = number;
        this.time = time;
        this.temperature = temperature;
    }

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

}
