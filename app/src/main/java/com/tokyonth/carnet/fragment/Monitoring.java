package com.tokyonth.carnet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.dashboardview.view.DashboardView;

public class Monitoring extends Fragment implements View.OnClickListener { ;

    private DashboardView dashboardView;

    private Button btn_temp;
    private Button btn_pressure;
    private SeekBar seekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_monitoring,container,false);
        dashboardView = (DashboardView) view.findViewById(R.id.dashboardView);
        btn_temp = (Button) view.findViewById(R.id.btn_temp);
        btn_pressure = (Button) view.findViewById(R.id.btn_pressure);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);

        btn_pressure.setOnClickListener(this);
        btn_temp.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dashboardView.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_temp:
                dashboardView.setTikeStrArray(new String[]{"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"});
                dashboardView.setText("当前温度");
                dashboardView.setUnit("0℃");
                dashboardView.setMaxNum(100);
                break;
            case R.id.btn_pressure:
                dashboardView.setTikeStrArray(new String[]{"0k", "10k", "20k", "30k", "40k", "50k", "60k", "70k", "80k", "90k", "100k"});
                dashboardView.setText("当前压强");
                dashboardView.setUnit("kPa");
                dashboardView.setMaxNum(100);
                break;
        }
    }
}
