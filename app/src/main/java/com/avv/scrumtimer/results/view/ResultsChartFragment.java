package com.avv.scrumtimer.results.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.results.presenter.ResultsPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by angelvazquez on 17/5/16.
 */
public class ResultsChartFragment extends ChartFragment implements ResultsView {

    private Logger logger = Logger.getLogger(getClass().getName());
    private ResultsPresenter presenter;

    public ResultsChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.results_chart, container, false);
        presenter = new ResultsPresenter();
        presenter.setView(this);
        chart = (ColumnChartView) view.findViewById(R.id.chart);

        generateDefaultData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void restartAnimation() {
    }

    @Override
    public void onReset() {

    }

    private ColumnChartData data;
    private ColumnChartView chart;

    private void generateDefaultData() {
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();

        int i = 0;
        HashMap<String, Long> results = presenter.getResults();
        Iterator it = results.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            float value = (float) (new Float(String.valueOf(pair.getValue())) / ((float) 60 * 1000));
            logger.info(pair.getKey() + " = " + value);

            values = new ArrayList<SubcolumnValue>();
            SubcolumnValue subcolumnValue = new SubcolumnValue(value, ChartUtils.pickColor());
            subcolumnValue.setLabel((String)pair.getKey());
            AxisValue axisValue = new AxisValue(i);
            axisValue.setLabel((String)pair.getKey());
            axisXValues.add(axisValue);
            values.add(subcolumnValue);

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);

            i++;
        }

        data = new ColumnChartData(columns);


        Axis axisX = new Axis();
        axisX.setValues(axisXValues);

        List<AxisValue> axisYValues = new ArrayList<AxisValue>();

        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Participante");
        axisY.setName("Tiempo");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setColumnChartData(data);

    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
