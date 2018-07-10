package com.prittysoft.jat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;



    public RecentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recents, container, false);
        mListView = v.findViewById(R.id.recent_listview);
        populateListView();

        return v;
    }

    private void populateListView(){
        Log.d(TAG, "displaying data in the listview");
        final List<MainMeasurements> listData = new ArrayList<>();
        Cursor data = mDatabaseHelper.getData();
        while (data.moveToNext()){
            MainMeasurements values = new MainMeasurements(data.getString(0) ,
                    data.getString(7), data.getString(5),
                    data.getString(6), data.getString(2));
            //listData.add(data.getString(0));
            listData.add(values);
        }
        //final ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listData);
        //mListView.setAdapter(adapter);

        final MeasurementsListAdapter adapter = new MeasurementsListAdapter(getContext(),
                R.layout.custom_adapter_listview, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String name = parent.getItemAtPosition(position).toString();
                String name2 = listData.get(position).getID();
                Log.d(TAG, "onItemClick: You clicked on " + name2);
                Intent DetailActivity = new Intent(getActivity(), DetailActivity.class);
                DetailActivity.putExtra("main_id", name2);
                startActivity(DetailActivity);
            }
        });

    }

    private void toastMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
