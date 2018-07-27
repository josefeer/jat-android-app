package com.prittysoft.jat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RegisterFragment extends Fragment{

    private android.support.v4.app.FragmentManager fragmentManager;
    public TextView startdate, enddate;
    private ListView listView;
    private Button buttonserch;
    private DatabaseHelper mDatabaseHelper;
    private Context context;
    private MainMeasurements values;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getFragmentManager();
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        startdate = v.findViewById(R.id.fragment_register_startdate);
        enddate = v.findViewById(R.id.fragment_register_enddate);
        listView = v.findViewById(R.id.fragment_register_listview);
        buttonserch = v.findViewById(R.id.fragment_register_searchbutton);
        mDatabaseHelper = new DatabaseHelper(getActivity());

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDateFragment startDateFragment = new StartDateFragment();
                startDateFragment.show(fragmentManager, "DatePicker");
            }
        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndDateFragment endDateFragment = new EndDateFragment();
                endDateFragment.show(fragmentManager, "DatePicker");
            }
        });

        buttonserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sdate = startdate.getText().toString();
                String edate = enddate.getText().toString();

                populateListView(sdate, edate);

            }
        });

        return v;
    }

    private void populateListView(String start, String end){
        final List<MainMeasurements> listData = new ArrayList<>();
        Cursor data = mDatabaseHelper.getDataBetweenDates(start, end);

        while (data.moveToNext()){

            values = new MainMeasurements(data.getString(0) ,
                    data.getString(11), data.getString(8),
                    data.getString(10), data.getString(9),
                    data.getString(4));

            listData.add(values);

        }

        data.close();

        listView.setAdapter(null);

        final MeasurementsListAdapter adapter = new MeasurementsListAdapter(this.context,
                R.layout.custom_adapter_listview, listData);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String positionID= listData.get(position).getID();
                String typevalue = listData.get(position).getType();
                Intent DetailActivity = new Intent(getActivity(), DetailActivity.class);
                DetailActivity.putExtra("id_main", positionID);
                DetailActivity.putExtra("type", typevalue);
                startActivity(DetailActivity);

            }

        });

    }

}
