package com.tokyonth.carnet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.bean.Project;

import java.util.List;

public class ProjectList extends ArrayAdapter<Project> {

    private int resourceId;
    public ProjectList(@NonNull Context context, int resource, @NonNull List<Project> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Project project = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView name = view.findViewById(R.id.project_name);
        TextView url = view.findViewById(R.id.project_url);
        name.setText(project.getName());
        url.setText(project.getUrl());
        return view;
    }

}
