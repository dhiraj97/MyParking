package com.example.parkmania;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkmania.models.Parking;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Parking> {

    public CustomAdapter(Context context, List<Parking> dataItem) {
        super(context, 0, dataItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Parking dataItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvLocation = convertView.findViewById(R.id.tv_location);
        TextView tvDate = convertView.findViewById(R.id.tv_date);


        // Populate the data into the template view using the data object
        Resources resources = getContext().getResources();

        tvName.setText(dataItem.getCarLicensePlateNumber());
        tvDate.setText(dataItem.getDate());
        String nohrs = String.valueOf(dataItem.getNoOfHrs())+ " Hrs";
        tvLocation.setText(nohrs);


        // Return the completed view to render on screen
        return convertView;
    }
}
