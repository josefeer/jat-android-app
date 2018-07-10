package com.prittysoft.jat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    /*DB tables*/
    private static final String DB_NAME = "JAT_DB";
    private static final String TABLE1_NAME = "main_measurements";
    private static final String TABLE2_NAME = "isotermos1";
    private static final String TABLE3_NAME = "isotermos2";
    private static final String TABLE4_NAME = "sensor_calibration";

    /*TABLE1 fields*/
    private static final String TABLE1_COL1 = "main_id";
    private static final String TABLE1_COL2 = "type";
    private static final String TABLE1_COL3 = "date";
    private static final String TABLE1_COL4 = "start_time";
    private static final String TABLE1_COL5 = "end_time";
    private static final String TABLE1_COL6 = "equipment_name";
    private static final String TABLE1_COL7 = "equipment_serial";
    private static final String TABLE1_COL8 = "client";
    private static final String TABLE1_COL9 = "range";
    private static final String TABLE1_COL10 = "range_unit";
    private static final String TABLE1_COL11 = "period";
    private static final String TABLE1_COL12 = "period_unit";

    /*TABLE2 fields*/
    private static final String TABLE2_COL1 = "id_isotermos1";
    private static final String TABLE2_COL2 = "main_id";
    private static final String TABLE2_COL3 = "timestamp";
    private static final String TABLE2_COL4 = "S1";
    private static final String TABLE2_COL5 = "S2";
    private static final String TABLE2_COL6 = "S3";
    private static final String TABLE2_COL7 = "S4";

    /*TABLE3 fields*/
    private static final String TABLE3_COL1 = "id_isotermos2";
    private static final String TABLE3_COL2 = "main_id";
    private static final String TABLE3_COL3 = "timestamp";
    private static final String TABLE3_COL4 = "S1";
    private static final String TABLE3_COL5 = "S2";
    private static final String TABLE3_COL6 = "S3";
    private static final String TABLE3_COL7 = "S4";
    private static final String TABLE3_COL8 = "S5";
    private static final String TABLE3_COL9 = "S6";
    private static final String TABLE3_COL10 = "S7";
    private static final String TABLE3_COL11 = "S8";
    private static final String TABLE3_COL12 = "S9";

    /*Table4 fields*/
    private static final String TABLE4_COL1 = "idSensorsCalibration";
    private static final String TABLE4_COL2 = "main_id";
    private static final String TABLE4_COL3 = "timestamp";
    private static final String TABLE4_COL4 = "S1";

    /*CREATE TABLE queries*/
    private static final String CREATETABLE1 = "CREATE TABLE " + TABLE1_NAME + " (" + TABLE1_COL1 +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE1_COL2 + " TEXT, " + TABLE1_COL3 +
            " TEXT, " + TABLE1_COL4 + " TEXT, " + TABLE1_COL5 + " TEXT, " + TABLE1_COL6 + " TEXT, "
            + TABLE1_COL7 + " TEXT, " + TABLE1_COL8 + " TEXT, "+ TABLE1_COL9 + " TEXT, "
            + TABLE1_COL10 + " TEXT, " + TABLE1_COL11 + " TEXT, " + TABLE1_COL12 +" TEXT)";

    private static final String CREATETABLE2 = "CREATE TABLE " + TABLE2_NAME + " (" + TABLE2_COL1 +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE2_COL2 + " INTEGER, " +
            TABLE2_COL3 + " TEXT, " + TABLE2_COL4 + " TEXT, " + TABLE2_COL5 +
            " TEXT, " + TABLE2_COL6 + " TEXT, " + TABLE2_COL7 + " TEXT, " +
            "FOREIGN KEY ("+ TABLE2_COL2 + ") REFERENCES " + TABLE1_NAME + " (" + TABLE1_COL1 +
            "))";

    private static final String CREATETABLE3 = "CREATE TABLE " + TABLE3_NAME + " (" + TABLE3_COL1 +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE3_COL2 + " INTEGER, " + TABLE3_COL3 +
            " TEXT, " + TABLE3_COL4 + " TEXT, " + TABLE3_COL5 +
            " TEXT, " + TABLE3_COL6 + " TEXT, " + TABLE3_COL7 + " TEXT," + TABLE3_COL8 + " TEXT, " +
            TABLE3_COL9 + " TEXT, " + TABLE3_COL10 + " TEXT, " + TABLE3_COL11 + " TEXT, " +
            TABLE3_COL12 +", " + "FOREIGN KEY (" + TABLE3_COL2 + ") REFERENCES " + TABLE1_NAME +
            " (" + TABLE1_COL1 + "))";

    private static final String CREATETABLE4 = "CREATE TABLE " + TABLE4_NAME + " (" + TABLE4_COL1 +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE4_COL2 + " INTEGER, " + TABLE4_COL3 +
            " TEXT, " + TABLE4_COL4 + " TEXT, FOREIGN KEY (" + TABLE4_COL2 + ") REFERENCES " +
            TABLE1_NAME + " (" + TABLE1_COL1 + "))";


    /*DROP TABLE queries*/
    private static final String DROPTABLE1 = "DROP TABLE IF EXISTS " + TABLE1_NAME;
    private static final String DROPTABLE2 = "DROP TABLE IF EXISTS " + TABLE2_NAME;
    private static final String DROPTABLE3 = "DROP TABLE IF EXISTS " + TABLE3_NAME;
    private static final String DROPTABLE4 = "DROP TABLE IF EXISTS " + TABLE4_NAME;


    public DatabaseHelper(Context context){
        super(context, DB_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATETABLE1);
        db.execSQL(CREATETABLE2);
        db.execSQL(CREATETABLE3);
        db.execSQL(CREATETABLE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROPTABLE1);
        db.execSQL(DROPTABLE2);
        db.execSQL(DROPTABLE3);
        db.execSQL(DROPTABLE4);
        onCreate(db);
    }

    public boolean addDataTable1(String[] values){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COL2, values[0]);
        contentValues.put(TABLE1_COL3, values[1]);
        contentValues.put(TABLE1_COL4, values[2]);
        contentValues.put(TABLE1_COL5, values[3]);
        contentValues.put(TABLE1_COL6, values[4]);
        contentValues.put(TABLE1_COL7, values[5]);
        contentValues.put(TABLE1_COL8, values[6]);
        contentValues.put(TABLE1_COL9, values[7]);
        contentValues.put(TABLE1_COL10, values[8]);
        contentValues.put(TABLE1_COL11, values[9]);
        contentValues.put(TABLE1_COL12, values[10]);

        Log.d(TAG, "addData: Adding " + contentValues + " to " + TABLE1_NAME);
        long result = db.insert(TABLE1_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else
            return true;
    }

    public boolean addDataTableSensorCalibration(String[] values){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE4_COL2, values[0]);
        contentValues.put(TABLE4_COL3, values[1]);
        contentValues.put(TABLE4_COL4, values[2]);

        Log.d(TAG, "addDataTable2: Addding" + contentValues + " to " + TABLE4_NAME);
        long result = db.insert(TABLE4_NAME, null, contentValues);

        if (result == -1){
            return false;
        }
        else
            return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE1_NAME + " ORDER BY " + TABLE1_COL1 +
                " DESC LIMIT 10";
        return db.rawQuery(query, null);
    }

    public Integer getAutoIncrementMeasurements(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT seq FROM sqlite_sequence WHERE name=MainMeasurements";
        Cursor data = db.rawQuery(query, null);
        Integer AutoIncrement;
        if (data.moveToNext()){
            AutoIncrement = data.getInt(0);
        }
        else{
            AutoIncrement = 0;
        }
        data.close();
        Log.d(TAG, "AutoIncrement Value: " + Integer.toString(AutoIncrement));
        return AutoIncrement;
    }

    public void updateEndTimeTable1(Integer seq, String endtime){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE1_NAME + " set (" + TABLE1_COL5 + ") = ('" +
          //      endtime + "') where " + TABLE1_COL1 + " = " + seq;
        //db.execSQL(query);
        ContentValues newValue = new ContentValues();
        newValue.put("end_time", endtime);
        db.update(TABLE1_NAME, newValue, "main_id = "+seq, null);
        Log.d(TAG, "Update Succesfully");
    }

    public ArrayList<String> getMainMeasurementDetails(String main_id){
        ArrayList<String> values = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM main_measurements WHERE main_id = " + main_id;
        Cursor data = db.rawQuery(query, null);

        //Cursor should return only one row
        if (data.moveToNext()){
            values.add(data.getString(0));
            values.add(data.getString(1));
            values.add(data.getString(2));
            values.add(data.getString(3));
            values.add(data.getString(4));
            values.add(data.getString(5));
            values.add(data.getString(6));
            values.add(data.getString(7));
            values.add(data.getString(8) + " " + data.getString(9));
            values.add(data.getString(10) + " " + data.getString(11));
        }
        else {
            values = null;
        }

        return values;
    }

    public Cursor getSensorCalibrationDataDetails(String main_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM sensor_calibration WHERE main_id = " + main_id;
        return db.rawQuery(query, null);
    }

}
