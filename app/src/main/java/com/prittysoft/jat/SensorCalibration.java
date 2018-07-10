package com.prittysoft.jat;

public class SensorCalibration {
    private String main_id;
    private String timestamp;
    private String s1;

    public SensorCalibration(String main_id, String timestamp, String s1) {
        this.main_id = main_id;
        this.timestamp = timestamp;
        this.s1 = s1;
    }

    public String getMain_id() {
        return main_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getS1() {
        return s1;
    }

}
