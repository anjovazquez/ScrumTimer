package com.avv.scrumtimer.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.view.MemoryCache;

import java.util.ArrayList;
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
public class ResultsChartFragment extends ChartFragment {

    Logger logger = Logger.getLogger(getClass().getName());

    //private BarChart mBarChart;

    public ResultsChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.results_chart, container, false);
        /*mBarChart = (BarChart) view.findViewById(R.id.barchart);

        mBarChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                Log.d("BarChart", "Position: " + _Position);
            }
        });*/

        chart = (ColumnChartView) view.findViewById(R.id.chart);
        //chart.setOnValueTouchListener(new ValueTouchListener());

        generateDefaultData();

        loadData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //mBarChart.startAnimation();
    }

    @Override
    public void restartAnimation() {
        //mBarChart.startAnimation();
    }

    @Override
    public void onReset() {

    }

    private void loadData() {

        int[] colorList = {0xFF343456, 0xFF563456, 0xFF873F56, 0xFF56B7F1, 0xFF343456, 0xFF1FF4AC, 0xFF1BA4E6, 0xFF123456};

        int i = 0;
        Iterator it = MemoryCache.results.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            float value = (float) (new Float(String.valueOf(pair.getValue())) / ((float) 60 * 1000));
            logger.info(pair.getKey() + " = " + value);
            //mBarChart.addBar(new BarModel((String) pair.getKey(), value, colorList[i]));
            i++;
        }
    }

    private ColumnChartData data;
    private ColumnChartView chart;

    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = MemoryCache.results.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;



        List<AxisValue> axisXValues = new ArrayList<AxisValue>();


        int i = 0;
        Iterator it = MemoryCache.results.entrySet().iterator();
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
        /*float j = 1;
        while(j<=5){
            AxisValue axisValue = new AxisValue(j);
            axisValue.setLabel(String.valueOf(j));
            axisYValues.add(axisValue);
            j = j + 0.25f;
        }
        axisY.setValues(axisYValues);*/

        axisX.setName("Participante");
        axisY.setName("Tiempo");
        data.setAxisXBottom(axisX);
        //data.setAxisXBottom(null);
        data.setAxisYLeft(axisY);

        chart.setColumnChartData(data);

    }
}
