package com.example.usama.dsms_fb;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WaterRequestDataAdapter extends ArrayAdapter<WaterRequestData> {
    public WaterRequestDataAdapter(@NonNull Context context, ArrayList<WaterRequestData> list) {
        super(context, 0 , list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
        WaterRequestData currentData = getItem(position);

        TextView requestNumber =(TextView) listItem.findViewById(R.id.requestNumber);
        requestNumber.setText("Request # "+Integer.toString(position+1));

        TextView dateDuration = (TextView) listItem.findViewById(R.id.dateduration);
        dateDuration.setText("Date Duration : "+ currentData.getStartDate() + " - "+ currentData.getEndDate());

        TextView timeduration = (TextView) listItem.findViewById(R.id.timeduration);
        timeduration.setText("Time Duration : " + currentData.getStartTime() + " - "+ currentData.getEndTime());

        TextView powerConsumption = (TextView) listItem.findViewById(R.id.powerConsumption);
        powerConsumption.setText("Power Consumption (Watts) :" + Double.toString(currentData.getEstimatedPowerConsumption()));

        TextView waterConsumption = (TextView) listItem.findViewById(R.id.waterConsumption);
        waterConsumption.setText("Water Consumption (Kiosik) :" + Double.toString(currentData.getEstimatedwaterExtraction()));

        TextView estimatedBill = (TextView) listItem.findViewById(R.id.expectedbill);
        estimatedBill.setText("Expected Bill (Rupees) :"  + Double.toString(currentData.getExpectedBill()));

        TextView peakBill = (TextView) listItem.findViewById(R.id.peakBill);
        peakBill.setText("Peak Bill  : "+Double.toString(currentData.getPeakBill()));

        TextView nonpeakBill = (TextView) listItem.findViewById(R.id.nonpeakBill);
        nonpeakBill.setText("Non Peak Bill  : "+Double.toString(currentData.getNonpeakBill()));




        return listItem;
    }
}

