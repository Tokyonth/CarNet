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
import com.tokyonth.carnet.bean.DataListBean;

import java.util.List;

public class DataListAdapter extends ArrayAdapter<DataListBean> {

    private int resourceId;

    public DataListAdapter(@NonNull Context context, int resource, @NonNull List<DataListBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataListBean dataListBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView time = view.findViewById(R.id.tv_time);
        TextView data = view.findViewById(R.id.tv_data);
        time.setText(dataListBean.getTime());
        data.setText(dataListBean.getData() + "\t\t");
        return view;
    }


}
