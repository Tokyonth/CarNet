package com.tokyonth.carnet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tokyonth.carnet.R;
import com.tokyonth.carnet.adapter.DataListAdapter;
import com.tokyonth.carnet.bean.DataListBean;
import com.tokyonth.carnet.utils.DateUtil;

import java.util.ArrayList;

public class DataTableActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList arrayList;
    private TextView healthy_tag;
    private BarChart mChart;
    private String[] types = {"周一", "周二", "周三", "周四", "周五", "周六","周日"};
    private float[] changes = {27.91f, 15.9f, 25.4f, 17.79f, 21.85f, 39.58f,30.52f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listView = (ListView) findViewById(R.id.data_list);
        healthy_tag = (TextView) findViewById(R.id.tv_tag);
        mChart = findViewById(R.id.bar_chart);

        initBarChart(mChart);
        setBarChartData(types.length, mChart);

        arrayList = new ArrayList<>();
        initList();
        DataListAdapter adapter = new DataListAdapter(this,R.layout.data_list_item,arrayList);
        listView.setAdapter(adapter);
    }

    private void initList() {
        Intent intent = getIntent();
        String tag = intent.getStringExtra("TAG");
        switch (tag) {
            case "blood":
                int Min = 100;
                int Max = 110;
                int result;
                healthy_tag.setText("血压(BP)\tmmHg");
                for (int i = 0; i <= 6; i++) {
                    result = Min + (int)(Math.random() * ((Max - Min) + 1));
                    arrayList.add(new DataListBean(DateUtil.getOldDate(-i),String.valueOf(result)));
                }
                break;
            case "temperature":
                double tMin = 35.55;
                double tMax = 37.55;
                double tresult;
                healthy_tag.setText("体温(T)\t°C");
                for (int i = 0; i <= 6; i++) {
                    tresult = tMin + (double)(Math.random() * ((tMax - tMin) + 1));
                    String xsresult = String .format("%.1f",tresult);
                    arrayList.add(new DataListBean(DateUtil.getOldDate(-i),String.valueOf(xsresult)));
                }
                break;
            case "pulse":
                int pMin = 80;
                int pMax = 100;
                int presult;
                healthy_tag.setText("脉搏(P)\tbpm");
                for (int i = 0; i <= 6; i++) {
                    presult = pMin + (int)(Math.random() * ((pMax - pMin) + 1));
                    arrayList.add(new DataListBean(DateUtil.getOldDate(-i),String.valueOf(presult)));
                }
                break;
        }

    }

    private void initBarChart(BarChart mBarChart) {
        //mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setDrawGridBackground(false); //网格
        mBarChart.getDescription().setEnabled(false);//描述
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);
        //折线图例 标签 设置
        Legend l = mBarChart.getLegend();
        l.setEnabled(false);

        YAxis leftAxis = mBarChart.getAxisLeft();
        YAxis rightAxis = mBarChart.getAxisRight();
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        XAxis xAxis = mBarChart.getXAxis();

        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGranularity(1f);
        //xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setTextColor(0xff74828F);
        xAxis.setTextSize(10f);
        xAxis.setAxisLineColor(0xffe0e0e0);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int idx = (int) value;
                return types[idx];
            }
        });
    }

    private void setBarChartData(int count, BarChart mChart) {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        int[] colors = new int[count];

        for (int i = 0; i < count; i++) {
            float val = changes[i];
            if (val > 0) {
                colors[i] = 0xffF04933;
            }
            if (val < 22) {
                colors[i] = 0xff2BBE53;
            }
            yVals.add(new BarEntry(i, Math.abs(val)));
        }

        BarDataSet mBarDataSet = new BarDataSet(yVals, "数据");
        mBarDataSet.setDrawIcons(false);
        mBarDataSet.setColors(colors);
        mBarDataSet.setValueTextSize(12f);
        mBarDataSet.setValueTextColor(0xff74828F);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(mBarDataSet);

        BarData mBarData = new BarData(dataSets);
        mBarData.setBarWidth(0.6f);

        mBarData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int idx = (int) entry.getX();
                return String.valueOf(changes[idx]);
            }
        });
        mChart.setData(mBarData);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
