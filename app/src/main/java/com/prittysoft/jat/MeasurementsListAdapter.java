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

    MeasurementsListAdapter(@NonNull Context context, int resource, @NonNull List<MainMeasurements> objects) {
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
        String equipment_model = getItem(position).getEquipment_model();
        String type = getItem(position).getType();

        MainMeasurements values = new MainMeasurements(ID, client, equipment, equipment_serial,
                equipment_model, type);

        ViewHolder holder;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();

            holder.ID = convertView.findViewById(R.id.recent_adapter_id);
            holder.equipment = convertView.findViewById(R.id.recent_adapter_equipment);
            holder.equipment_model = convertView.findViewById(R.id.recent_adapter_model);
            holder.equipment_serial = convertView.findViewById(R.id.recent_adapter_serial);
            holder.equipment_client = convertView.findViewById(R.id.recent_adapter_client);

            convertView.setTag(holder);

        }
        else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.ID.setText(values.getID());
        holder.equipment.setText(values.getEquipment());
        holder.equipment_model.setText(values.getEquipment_model());
        holder.equipment_serial.setText(values.getEquipment_serial());
        holder.equipment_client.setText(values.getClient());

        return convertView;

    }

    public static class ViewHolder{
        TextView ID;
        TextView equipment;
        TextView equipment_model;
        TextView equipment_serial;
        TextView equipment_client;
    }

}
