package com.example.llkkmmkmkllk.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryBackgroundTask extends AsyncTask<Void, Void, ArrayList> {

    static String json = "";
    String parsed;
    JSONObject Jaa;
    Map<String,Object> map=new HashMap<>();

    Map<String,Object>  mapp=new HashMap<>();
    static ArrayList<Object> arrayOfMonths;
    int waterflow;

    JSONArray JA2;
    @Override
    protected ArrayList doInBackground(Void... voids) {

        Object obj;
        try {
            URL url = new URL("http://139.59.81.23/apis/droplet/api/v1/devices/1/usage/2018/months");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                if (line != null) {
                    json = json + line;
                }
            }

            ObjectMapper mapper=new ObjectMapper();

            map=mapper.readValue(json, new TypeReference<Map<String,Object>>() {});
            arrayOfMonths=(ArrayList<Object>) map.get("months");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayOfMonths;
    }
}
