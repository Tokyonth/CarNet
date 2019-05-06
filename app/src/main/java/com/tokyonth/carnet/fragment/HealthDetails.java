package com.tokyonth.carnet.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.activity.DataTableActivity;
import com.tokyonth.carnet.chart.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.List;

public class HealthDetails extends Fragment {

    private LineChart lineChart;
    private DynamicLineChartManager dynamicLineChartManager;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    private Button btn1;
    private Button btn2;
    private Button btn3;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_healthdetails,container,false);
        lineChart = root.findViewById(R.id.lineChart);
        btn1 = root.findViewById(R.id.btn_blood_more);
        btn2 = root.findViewById(R.id.btn_temperature_more);
        btn3 = root.findViewById(R.id.btn_pulse_more);
        tv1 = root.findViewById(R.id.tv_pulse_msg);
        tv2 = root.findViewById(R.id.tv_blood_msg);
        tv3 = root.findViewById(R.id.tv_temperature_msg);
        initData();
        initView();
        return root;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){ }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                list.add((95 + (int)(Math.random() * ((115 - 95) + 1))));
                list.add((85 + (int)(Math.random() * ((100 - 85) + 1))));
                list.add((35 + (int)(Math.random() * ((40 - 35) + 1))));
                dynamicLineChartManager.addEntry(list);
                list.clear();
            }
        }
    };

    private void initView() {
        //折线名字
        names.add("血压");
        names.add("脉搏");
        names.add("体温");
        //折线颜色
        colour.add(Color.parseColor("#2A8EE3"));
        colour.add(Color.parseColor("#109D58"));
        colour.add(Color.parseColor("#E8A140"));

        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour);
        dynamicLineChartManager.setYAxis(120, 30, 10);
        dynamicLineChartManager.setDescription("");
        dynamicLineChartManager.setMarkerView(getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //设置曲线填充色 以及 MarkerView
                Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
                dynamicLineChartManager.setChartFillDrawable(drawable);
            }
        }, 1000);

        btn3.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn1.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.btn_blood_more:
                    intent.setClass(getContext(),DataTableActivity.class);
                    intent.putExtra("TAG","blood");
                    startActivity(intent);
                    break;
                case R.id.btn_temperature_more:
                    intent.setClass(getContext(),DataTableActivity.class);
                    intent.putExtra("TAG","temperature");
                    startActivity(intent);
                    break;
                case R.id.btn_pulse_more:
                    intent.setClass(getContext(),DataTableActivity.class);
                    intent.putExtra("TAG","pulse");
                    startActivity(intent);
                    break;
            }
        }
    };

}
