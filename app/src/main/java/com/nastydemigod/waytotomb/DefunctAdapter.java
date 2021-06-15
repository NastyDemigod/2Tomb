package com.nastydemigod.waytotomb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        Log.d("post", "Размер листа: "+ defunctList.size());
        return defunctList.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("post", "getView");
        Context context = parent.getContext();

        Defunct defunct = defunctList.get(position);

        Holder holder = null;

        if(convertView==null){

            Log.d("post", "convertView == null");
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_defunct, parent, false);
            holder = new Holder();


            holder.FNO = convertView.findViewById(R.id.fno);
            holder.dates = convertView.findViewById(R.id.dates);
            holder.cemen = convertView.findViewById(R.id.cemen);
            holder.zah = convertView.findViewById(R.id.zah);
            holder.uchast = convertView.findViewById(R.id.uchast);
            holder.loc = convertView.findViewById(R.id.location);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }



        holder.FNO.setText(defunct.getFNO());
        holder.dates.setText(defunct.getDates());
        holder.cemen.setText(defunct.getCemen());
        holder.zah.setText(defunct.getZah());
        holder.uchast.setText(defunct.getUchast());
        holder.loc.setText(defunct.getLocahion());


        return convertView;
    }

    private static class Holder {
        TextView FNO;
        TextView dates;
        TextView cemen;
        TextView zah;
        TextView uchast;
        TextView loc;

    }
}
