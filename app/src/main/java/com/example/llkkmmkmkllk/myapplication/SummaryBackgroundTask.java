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

public class SummaryBackgroundTask extends AsyncTask<Void,Void,Void> {


    String json="";
    int percentage;
    int waterPercentage;
    int viewUsage;
    double viewUsageToday;
    double viewUsageThisMonth;
    String parsed;

    @Override
    protected Void doInBackground(Void... voids) {

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
            Map<String,Object> map=new HashMap<>();
            map=mapper.readValue(json, new TypeReference<Map<String,Object>>() {});
            Map<String,Object> childMap1= (Map<String, Object>) map.get("usage");
            Map<String,Object> childMap2= (Map<String, Object>) map.get("level");

            waterPercentage= (int) childMap2.get("percentage");
            viewUsage= (int) childMap2.get("volume");

            viewUsageToday=(int)childMap1.get("day");
            viewUsageThisMonth=(int)childMap1.get("month");

            parsed=childMap1.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        percentage=waterPercentage;
        Profile.usage.setText(String.valueOf(viewUsage));
        Profile.waterLevel.setText(String.valueOf(waterPercentage)+"%");
        Profile.todayUsage.setText(String.valueOf(viewUsageToday));
        Profile.monthlyUsage.setText(String.valueOf(viewUsageThisMonth));

        if(percentage==100){
            Profile.waterPercentage.setImageResource(R.drawable.water13);
        } else if(percentage>=90){
            Profile.waterPercentage.setImageResource(R.drawable.water12);
        }else if(percentage>=80){
            Profile.waterPercentage.setImageResource(R.drawable.water11);
        }else if(percentage>=70){
            Profile.waterPercentage.setImageResource(R.drawable.water10);
        }else if(percentage>=60){
            Profile.waterPercentage.setImageResource(R.drawable.water9);
        }else if(percentage>=50){
            Profile.waterPercentage.setImageResource(R.drawable.water8);
        }else if(percentage>=50){
            Profile.waterPercentage.setImageResource(R.drawable.water7);
        }else if(percentage>=40){
            Profile.waterPercentage.setImageResource(R.drawable.water6);
        }else if(percentage>=30){
            Profile.waterPercentage.setImageResource(R.drawable.water5);
        }else if(percentage>=20){
            Profile.waterPercentage.setImageResource(R.drawable.water4);
        }else if(percentage>=10){
            Profile.waterPercentage.setImageResource(R.drawable.water3);
        }else if(percentage>0){
            Profile.waterPercentage.setImageResource(R.drawable.water2);
        }else {
            Profile.waterPercentage.setImageResource(R.drawable.water1);
        }
        super.onPostExecute(aVoid);
    }

}
