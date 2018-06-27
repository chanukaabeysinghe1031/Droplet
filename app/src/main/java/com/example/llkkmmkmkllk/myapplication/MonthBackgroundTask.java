package com.example.llkkmmkmkllk.myapplication;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import static android.content.Context.MODE_PRIVATE;

public class MonthBackgroundTask extends AsyncTask<Void,Void,ArrayList> {

    static String json = "";
    Map<String, Object> map = new HashMap<>();
    static ArrayList<Object> arrayOfDays;
    int year;
    int month;
    int id=1;
    @Override
    protected ArrayList<Object> doInBackground(Void... voids) {

        Calendar calender=Calendar.getInstance();
        year=calender.get(Calendar.YEAR);
        month=calender.get(Calendar.MONTH);

        try {
            URL url = new URL(MainActivity.getURL()+"/devices/"+id+"/usage/"+year+"/"+month+"/days");
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

            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            arrayOfDays = (ArrayList<Object>) map.get("days");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayOfDays;
    }


}
