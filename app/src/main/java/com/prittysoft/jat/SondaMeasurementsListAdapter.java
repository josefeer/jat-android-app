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

public class SondaMeasurementsListAdapter extends ArrayAdapter<SondaMeasurements> {

    private Context mContext;
    private int mResource;

    SondaMeasurementsListAdapter(@NonNull Context context, int resource, @NonNull List<SondaMeasurements> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String number = getItem(position).getNumber();
        String time = getItem(position).getTime();
        String temperature = getItem(position).getTemperature();

        SondaMeasurements values = new SondaMeasurements(number, time, temperature);

        ViewHolder holder;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();

            holder.number = convertView.findViewById(R.id.sonda_adapter_lbl_number);
            holder.time = convertView.findViewById(R.id.sonda_adapter_lbl_time);
            holder.temperature = convertView.findViewById(R.id.sonda_adapter_lbl_temperature);

            convertView.setTag(holder);

        }
        else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.number.setText(values.getNumber());
        holder.time.setText(values.getTime());
        holder.temperature.setText(values.getTemperature());

        return convertView;

    }

    public static class ViewHolder{
        TextView number;
        TextView time;
        TextView temperature;
    }

}
