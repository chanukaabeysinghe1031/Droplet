package com.example.llkkmmkmkllk.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class monthSummary extends AppCompatActivity implements View.OnClickListener{

    private LineChart lChart;
    private ArrayList arrayList;
    private Button btnBack;
    private TextView refresh;
//    public static TextView text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_summary);

        lChart = (LineChart) findViewById(R.id.summaryChart);
        btnBack=(Button) findViewById(R.id.btn1);
        refresh=(TextView)findViewById(R.id.swipe);

        btnBack.setOnClickListener(this);
        refresh.setOnClickListener(this);
        displayTable();

    }

    private void displayTable(){
        Calendar calender=Calendar.getInstance();

        MonthBackgroundTask hbt = new MonthBackgroundTask();
        try {
            arrayList = hbt.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        lChart.setDragEnabled(true);
        lChart.setScaleEnabled(true);
        lChart.animateXY(300,300);
        ArrayList yValues = new ArrayList<>();
        ArrayList<Integer> usages = new ArrayList<>();
        ArrayList<String > months = new ArrayList<>();
        Map<String, Object> childMap = new HashMap<>();


        for (int j = 0; j < arrayList.size(); j++) {
            childMap = (HashMap) arrayList.get(j);
            String monthNumber = (String) childMap.get("day");
            int waterflow=(int)childMap.get("usage");
            months.add(monthNumber);
            usages.add(waterflow);
        }

        for(int i=0;i<31;i++){
            boolean setValues=false;
            for(int k=0;k<months.size();k++){
                if(String.valueOf(i+1).equals(months.get(k))){
                    setValues=true;
                    yValues.add(new Entry(i, usages.get(k)));
                }
            }

            if(!setValues){
                yValues.add(new Entry(i, 0));
            }
        }

        LineDataSet dataSet = new LineDataSet(yValues, "Daily Usage");
        dataSet.setFillAlpha(110);
        dataSet.setColor(Color.BLACK);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(3f);
        dataSet.setValueTextSize(10f);

        LimitLine upperLimit = new LimitLine(4000f, "Too Much Usage");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f, 10f, 0);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setTextSize(15f);

        YAxis leftAsxis = lChart.getAxisLeft();
        leftAsxis.removeAllLimitLines();
        leftAsxis.addLimitLine(upperLimit);
        //leftAsxis.setAxisMaximum(5000f);
        leftAsxis.setAxisMinimum(0f);
        leftAsxis.setDrawGridLines(false);
        leftAsxis.enableGridDashedLine(10f, 10f, 0);
        leftAsxis.setDrawLimitLinesBehindData(true);

        XAxis bottomAxis=lChart.getXAxis();
        bottomAxis.setDrawGridLines(false);

        lChart.getAxisRight().setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);
        lChart.setData(data);

//        String[] values = new String[]{"1", "February", "March", "April"
//                , "May", "June", "July", "August", "September", "Octomber", "November", "December", "January"};

        XAxis xAxis = lChart.getXAxis();
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @Override
    public void onClick(View v) {
        if(v==btnBack){
            finish();
            startActivity(new Intent(this, Profile.class));
        }

        if(v==refresh){
            displayTable();
        }
    }
}
