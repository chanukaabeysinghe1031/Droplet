package com.example.llkkmmkmkllk.myapplication;


import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class YearBackgroundTask extends AsyncTask<Void, Void, ArrayList> {

    static String json = "";
    Map<String, Object> map = new HashMap<>();
    static ArrayList<Object> arrayOfMonths;
    int year;

    @Override
    protected ArrayList doInBackground(Void... voids) {

        Calendar calender=Calendar.getInstance();
        year=calender.get(Calendar.YEAR);

        Object obj;
        try {
            URL url = new URL("http://139.59.81.23/apis/droplet/api/v1/devices/1/usage/"+String.valueOf(year)+"/months");
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

            ObjectMapper mapper = new ObjectMapper();

            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            arrayOfMonths = (ArrayList<Object>) map.get("months");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayOfMonths;
    }
}
