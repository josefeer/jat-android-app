package com.prittysoft.jat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private TextView txt_startdate, txt_enddate, cal_startdate, cal_enddate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        txt_startdate = v.findViewById(R.id.register_txt_startdate);
        txt_enddate = v.findViewById(R.id.register_txt_enddate);
        cal_startdate = v.findViewById(R.id.register_cal_startdate);
        cal_enddate = v.findViewById(R.id.register_cal_enddate);

        cal_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal0 = Calendar.getInstance();
                int year0 = cal0.get(Calendar.YEAR);
                int month0 = cal0.get(Calendar.MONTH);
                int day0 = cal0.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog0 = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme,
                        mDateSetListener,
                        year0,month0,day0
                );

                dialog0.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //year month and datofmonth contains values from user
            }
        };

        return v;
    }

}
