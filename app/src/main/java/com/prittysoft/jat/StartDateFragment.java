package com.prittysoft.jat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class StartDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(activity, R.style.SigcsaDialogTheme, this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        String month, day;
        TextView startdate = activity.findViewById(R.id.fragment_register_startdate);
        mm++;

        if (mm < 10){
            month = "0" + mm;
        }
        else {
            month = Integer.toString(mm);
        }

        if (dd < 10){
            day = "0" + dd;
        }
        else {
            day = Integer.toString(dd);
        }

        String newdate = yy + "-" + month + "-" + day;
        startdate.setText(newdate);
    }

}

