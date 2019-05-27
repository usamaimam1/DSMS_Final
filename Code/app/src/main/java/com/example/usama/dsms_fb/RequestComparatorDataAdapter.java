package com.example.usama.dsms_fb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestComparatorDataAdapter extends ArrayAdapter<WaterRequestData> {
    public RequestComparatorDataAdapter(@NonNull Context context, ArrayList<WaterRequestData> list) {
        super(context, 0 , list);
    }
    public View getView(int position, @Nullable android.view.View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.comparator_view,parent,false);
        WaterRequestData currentData = getItem(position);

        TextView request = (TextView) listItem.findViewById(R.id.Request);
        TextView dateDuration =(TextView) listItem.findViewById(R.id.DateDuration);
        TextView timeDuration =(TextView) listItem.findViewById(R.id.TimeDuration);
        TextView PowerConsumption =(TextView) listItem.findViewById(R.id.Power);
        TextView waterConsumption =(TextView) listItem.findViewById(R.id.WaterConsumption);
        TextView expectedBill =(TextView) listItem.findViewById(R.id.ExpectedBill);
        TextView peakHourBill =(TextView) listItem.findViewById(R.id.peakHourBill);
        TextView nonpeakHourBill =(TextView) listItem.findViewById(R.id.nonpeakHourBill);
        /*Button submitButton  =(Button) listItem.findViewById(R.id.submitButton);*/

        request.setText(currentData.getRequestID());
        dateDuration.setText("Date Duration : "+ currentData.getStartDate() + " - "+ currentData.getEndDate());
        timeDuration.setText("Time Duration : " + currentData.getStartTime() + " - "+ currentData.getEndTime());
        PowerConsumption.setText("Power Consumption (Watts) :" + Double.toString(currentData.getEstimatedPowerConsumption()));
        waterConsumption.setText("Water Consumption (Kiosik) :" + Double.toString(currentData.getEstimatedwaterExtraction()));
        expectedBill.setText("Expected Bill (Rupees) :"  + Double.toString(currentData.getExpectedBill()));
        peakHourBill.setText("Peak Hour Bill (Rupees) :"  + Double.toString(currentData.getPeakBill()));
        nonpeakHourBill.setText("Non peak Hour Bill (Rupees) :"  + Double.toString(currentData.getNonpeakBill()));


        return listItem;
    }
}

