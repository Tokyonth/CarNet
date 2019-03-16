package com.tokyonth.carnet.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.bean.CompositeIndexBean;
import com.tokyonth.carnet.bean.IncomeBean;
import com.tokyonth.carnet.bean.LineChartBean;
import com.tokyonth.carnet.ui.widget.LineChartManager;
import com.tokyonth.carnet.utils.LocalJsonAnalyzeUtil;

import java.util.List;

public class HealthDetails extends Fragment {

    private LineChartBean lineChartBean;
    private List<IncomeBean> pulse_rate;//脉搏
    private List<CompositeIndexBean> temperature;//体温
    private List<CompositeIndexBean> blood_pressure;//血压

    private LineChart lineChart1;

    private LinearLayout cl_shanghai;
    private View view_shanghai;

    private LinearLayout cl_gem;
    private View view_gem;

    private LineChartManager lineChartManager1;

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UrgentAdapter adapter = new UrgentAdapter(getContext(),R.layout.contacts_list,mFoodList);
        this.setListAdapter(adapter);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_healthdetails,container,false);
        lineChart1 = view.findViewById(R.id.lineChart);
        cl_shanghai = view.findViewById(R.id.cl_shanghai);
        view_shanghai = view.findViewById(R.id.view_shanghai);
        cl_gem = view.findViewById(R.id.cl_gem);
        view_gem = view.findViewById(R.id.view_gem);
        lineChart1 = view.findViewById(R.id.lineChart);

        initData();
        initView();
        return view;
    }


    private void initData() {
        //获取数据
        lineChartBean = LocalJsonAnalyzeUtil.JsonToObject(getContext(), "line_chart.json", LineChartBean.class);
        pulse_rate = lineChartBean.getGRID0().getResult().getClientAccumulativeRate();

        temperature = lineChartBean.getGRID0().getResult().getCompositeIndexShanghai();
        blood_pressure = lineChartBean.getGRID0().getResult().getCompositeIndexShenzhen();
    }

    private void initView() {

        lineChartManager1 = new LineChartManager(lineChart1);
        cl_shanghai.setOnClickListener(listener);
       // cl_shenzhen.setOnClickListener(listener);
        cl_gem.setOnClickListener(listener);

        //展示图表
        lineChartManager1.showLineChart(pulse_rate, "脉搏", getResources().getColor(R.color.blue));
        lineChartManager1.addLine(temperature, "体温", getResources().getColor(R.color.orange));
        lineChartManager1.addLine(blood_pressure, "血压", getResources().getColor(R.color.green));

        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
        lineChartManager1.setChartFillDrawable(drawable);
        lineChartManager1.setMarkerView(getContext());
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cl_shanghai:
                    view_shanghai.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));

                    view_gem.setBackground(getResources().getDrawable(R.drawable.shape_round_green));
                    //view_shenzhen.setBackground(getResources().getDrawable(R.drawable.shape_round_green));

                    lineChartManager1.resetLine(1, temperature, "体温", getResources().getColor(R.color.orange));
                    break;
                case R.id.cl_gem:
                    //view_shenzhen.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));

                    view_gem.setBackground(getResources().getDrawable(R.drawable.shape_round_green));
                    view_shanghai.setBackground(getResources().getDrawable(R.drawable.shape_round_green));

                    lineChartManager1.resetLine(1, blood_pressure, "血压", getResources().getColor(R.color.orange));
                    break;

            }
        }
    };


}
