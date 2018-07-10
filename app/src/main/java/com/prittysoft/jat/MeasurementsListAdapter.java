package com.prittysoft.jat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MeasurementsListAdapter extends ArrayAdapter<MainMeasurements> {

    private Context mContext;
    private int mResource;

    public MeasurementsListAdapter(@NonNull Context context, int resource, @NonNull List<MainMeasurements> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String ID = getItem(position).getID();
        String client = getItem(position).getClient();
        String equipment = getItem(position).getEquipment();
        String equipment_serial = getItem(position).getEquipment_serial();
        String date = getItem(position).getDate();


        MainMeasurements values = new MainMeasurements(ID, client, equipment, equipment_serial,date);
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();

            holder.client = convertView.findViewById(R.id.textView5);
            holder.equipment = convertView.findViewById(R.id.textView6);
            holder.equipment_serial = convertView.findViewById(R.id.textView7);
            holder.date = convertView.findViewById(R.id.textView8);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.client.setText(values.getClient());
        holder.equipment.setText(values.getEquipment());
        holder.equipment_serial.setText(values.getEquipment_serial());
        holder.date.setText(values.getDate());




        return convertView;

    }

    public static class ViewHolder{
       TextView client;
       TextView equipment;
       TextView equipment_serial;
       TextView date;
    }

}
