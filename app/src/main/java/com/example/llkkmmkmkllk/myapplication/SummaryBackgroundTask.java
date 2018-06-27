package com.example.llkkmmkmkllk.myapplication;

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
import java.util.HashMap;
import java.util.Map;

public class SummaryBackgroundTask extends AsyncTask<Void,Void,Map> {


    String json="";
    int percentage;
    int waterPercentage;
    int viewUsage;
    double viewUsageToday;
    double viewUsageThisMonth;
    String parsed;

    @Override
    protected Map doInBackground(Void... voids) {

        Map<String,Object> map=new HashMap<>();
        Object obj;
        try {
            URL url=new URL("http://139.59.81.23/apis/droplet/api/v1/devices/1/summary");
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            InputStream inputStream=urlConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while(line!=null){
                line=bufferedReader.readLine();
                if(line!=null){
                    json=json+line;
                }
            }

            ObjectMapper mapper=new ObjectMapper();
            map=mapper.readValue(json, new TypeReference<Map<String,Object>>() {});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
