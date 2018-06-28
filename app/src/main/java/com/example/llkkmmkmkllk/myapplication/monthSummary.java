package com.example.llkkmmkmkllk.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.graphics.Color.RED;

public class monthSummary extends AppCompatActivity implements View.OnClickListener {

    private LineChart lChart;
    private ArrayList arrayList;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_summary);
        lChart = (LineChart) findViewById(R.id.summaryChart);
        btnBack = (Button) findViewById(R.id.btn1);
        btnBack.setOnClickListener(this);

        if (!isConnected(monthSummary.this)) {
            doTask(getData());
        } else {
            MonthBackgroundTask hbt = new MonthBackgroundTask();
            try {
                arrayList = hbt.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            doTask(arrayList);
        }

    }

    private void doTask(ArrayList arrayList) {

        lChart.setDragEnabled(true);
        lChart.animateXY(300, 300);
        lChart.setScaleEnabled(true);
        ArrayList yValues = new ArrayList<>();
        ArrayList<Integer> usages = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>();
        Map<String, Object> childMap = new HashMap<>();

        for (int j = 0; j < arrayList.size(); j++) {
            childMap = (HashMap) arrayList.get(j);
            String monthNumber = (String) childMap.get("day");
            int waterflow = (int) childMap.get("usage");
            months.add(monthNumber);
            usages.add(waterflow);
        }

        storeData(months, usages);

        for (int i = 0; i < 31; i++) {
            boolean setValues = false;
            for (int k = 0; k < months.size(); k++) {
                if (String.valueOf(i + 1).equals(months.get(k))) {
                    setValues = true;
                    yValues.add(new Entry(i, usages.get(k)));
                }
            }

            if (!setValues) {
                yValues.add(new Entry(i, 0));
            }
        }

        LineDataSet dataSet = new LineDataSet(yValues, "Daily Usage");
        dataSet.setFillAlpha(110);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.BLACK);
        dataSet.setLineWidth(3f);
        //dataSet.setValueTextSize(10f);

        LimitLine upperLimit = new LimitLine(4000f, "Too Much Usage");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f, 10f, 0);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setTextSize(15f);

        YAxis leftAsxis = lChart.getAxisLeft();
        leftAsxis.removeAllLimitLines();
        leftAsxis.addLimitLine(upperLimit);
        leftAsxis.setAxisMinimum(0f);
        leftAsxis.enableGridDashedLine(10f, 10f, 0);
        leftAsxis.setDrawGridLines(false);
        leftAsxis.setDrawLimitLinesBehindData(true);

        XAxis bottomAxis = lChart.getXAxis();
        bottomAxis.setDrawGridLines(false);

        lChart.getAxisRight().setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);
        lChart.setData(data);

        XAxis xAxis = lChart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
            startActivity(new Intent(this, Profile.class));
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }


    public Builder buildDialog(Context c) {

        Builder builder = new Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(monthSummary.this, Profile.class));
            }

        });
        return builder;
    }

    public void storeData(ArrayList days, ArrayList usages) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DailyUsage", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set setOfDays = new HashSet<>(days);
        Set setOfUsage = new HashSet<>(usages);

        editor.putStringSet("Days", setOfDays);
        editor.putStringSet("Usage", setOfUsage);
        editor.apply();
    }

    public ArrayList getData() {
        ArrayList getArrayList = new ArrayList();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DailyUsage", MODE_PRIVATE);

        Set setOfDays = new HashSet<>();
        Set setOfUsage = new HashSet<>();

        setOfDays = sp.getStringSet("Days", null);
        setOfUsage = sp.getStringSet("Usage", null);
        ArrayList days = new ArrayList(setOfDays);
        ArrayList usages = new ArrayList(setOfUsage);
        for (int i = 0; i < setOfDays.size(); i++) {
            Map<String, Object> childMap = new HashMap();
            childMap.put("day", days.get(i));
            childMap.put("usage",usages.get(i));
            if (childMap != null && getArrayList != null) {
                getArrayList.add(childMap);
            }

        }
        return getArrayList;
    }
}
