package com.tokyonth.carnet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokyonth.carnet.R;

import java.util.List;

public class MonitoringAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;

    /**
     * 绑定数据
     *
     * @param list
     */
    public void bindData(List<String> list) {
        this.list = list;
    }

    public MonitoringAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.monitoring_item, null);
        } else {
            view = convertView;
        }
        /* 从动态布局中获取控件 */
        TextView textView = (TextView) view.findViewById(R.id.textView1);
        /* 设置TextView的值 */
        textView.setText(list.get(position).toString());

        return view;
    }
}


