package com.tokyonth.carnet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.activity.ScrollingActivity;
import com.tokyonth.carnet.adapter.MonitoringAdapter;
import com.tokyonth.carnet.dial.SemicircleProgressView;

import java.util.ArrayList;

public class Monitoring extends ListFragment implements View.OnClickListener {

    private SemicircleProgressView semicircleProgressView;
    private MonitoringAdapter adapter;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_monitoring,container,false);
        this.setListAdapter(adapter);
        semicircleProgressView = (SemicircleProgressView) root.findViewById(R.id.semicircleProgressView);
        semicircleProgressView.setSesameValues(98, 100);

        btn = (Button) root.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MonitoringAdapter(getContext());

        ArrayList list = new ArrayList();
        list.add("心率正常");
        list.add("体温正常");
        adapter.bindData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                Intent intent = new Intent();
                intent.setClass(getContext(),ScrollingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
