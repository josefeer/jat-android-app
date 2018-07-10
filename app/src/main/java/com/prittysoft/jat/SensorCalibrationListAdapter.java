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

public class SensorCalibrationListAdapter extends ArrayAdapter<SensorCalibration> {

    private Context mContext;
    private int mResource;

    public SensorCalibrationListAdapter(@NonNull Context context, int resource, @NonNull List<SensorCalibration> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String main_id = getItem(position).getMain_id();
        String timestamp = getItem(position).getTimestamp();
        String s1 = getItem(position).getS1();

        SensorCalibration values = new SensorCalibration(main_id, timestamp, s1);
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();

            holder.timestamp = convertView.findViewById(R.id.adapter_timestamp);
            holder.s1 = convertView.findViewById(R.id.adapter_s1);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.timestamp.setText(values.getTimestamp());
        holder.s1.setText(values.getS1());

        return convertView;

    }

    public static class ViewHolder{
        TextView timestamp;
        TextView s1;
    }
}
