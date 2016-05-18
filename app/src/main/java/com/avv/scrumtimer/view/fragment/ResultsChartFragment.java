package com.avv.scrumtimer.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.view.MemoryCache;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by angelvazquez on 17/5/16.
 */
public class ResultsChartFragment extends ChartFragment {

    private BarChart mBarChart;

    public ResultsChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.results_chart, container, false);
        mBarChart = (BarChart) view.findViewById(R.id.barchart);

        mBarChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                Log.d("BarChart", "Position: " + _Position);
            }
        });

        loadData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBarChart.startAnimation();
    }

    @Override
    public void restartAnimation() {
        mBarChart.startAnimation();
    }

    @Override
    public void onReset() {

    }

    private void loadData() {
        Iterator it = MemoryCache.results.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            mBarChart.addBar(new BarModel((String)pair.getKey(), (float)(new Float(String.valueOf(pair.getValue()))/1000), 0xFF123456));
        }
    }
}
