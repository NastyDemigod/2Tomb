package com.nastydemigod.waytotomb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DefunctAdapter extends ArrayAdapter<Defunct> {

    private List<Defunct> defunctList;

    public DefunctAdapter(@NonNull Context context, int resource, List<Defunct> defunctList) {
        super(context, resource);
        this.defunctList = defunctList;
    }

    @Override
    public int getCount() {
        return defunctList.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Context context = parent.getContext();

        Defunct defunct = defunctList.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_defunct, parent, false);


        TextView FNO = convertView.findViewById(R.id.fno);
        TextView dates = convertView.findViewById(R.id.dates);
        TextView cemen = convertView.findViewById(R.id.cemen);
        TextView zah = convertView.findViewById(R.id.zah);
        TextView uchast = convertView.findViewById(R.id.uchast);

        FNO.setText(defunct.getFNO());
        dates.setText(defunct.getDates());
        cemen.setText(defunct.getCemen());
        zah.setText(defunct.getZah());
        uchast.setText(defunct.getUchast());



        return convertView;
    }
}
