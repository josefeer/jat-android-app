package com.prittysoft.jat;

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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RecentsFragment extends Fragment {

    private static final String TAG = "ListDataActivity";
    private DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private Context context;
    MainMeasurements values;


    public RecentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabaseHelper = new DatabaseHelper(getActivity());
        View v;

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recents, container, false);
        mListView = v.findViewById(R.id.recent_listview);
        populateListView();

        return v;
    }

    private void populateListView(){
        final List<MainMeasurements> listData = new ArrayList<>();
        Cursor data = mDatabaseHelper.getRecentsData();

        while (data.moveToNext()){

            values = new MainMeasurements(data.getString(0) ,
                    data.getString(11), data.getString(8),
                    data.getString(10), data.getString(9));

            listData.add(values);

        }

        final MeasurementsListAdapter adapter = new MeasurementsListAdapter(this.context,
                R.layout.custom_adapter_listview, listData);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String positionID= listData.get(position).getID();
                Intent DetailActivity = new Intent(getActivity(), DetailActivity.class);
                DetailActivity.putExtra("id_main", positionID);
                startActivity(DetailActivity);

            }

        });

    }

    private void toastMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
