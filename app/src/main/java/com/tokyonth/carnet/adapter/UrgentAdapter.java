package com.tokyonth.carnet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.bean.Contacts;

import java.util.List;

public class UrgentAdapter extends ArrayAdapter<Contacts> {

    private int resourceId;

    public UrgentAdapter(@NonNull Context context, int resource, @NonNull List<Contacts> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contacts food = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView textView = view.findViewById(R.id.list_name);
        ImageView imageView = view.findViewById(R.id.list_image);
        textView.setText(food.getName());
        imageView.setImageResource(food.getImage());
        return view;
    }
}

