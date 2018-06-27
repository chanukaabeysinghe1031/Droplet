package com.example.llkkmmkmkllk.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

public class Profile extends AppCompatActivity  implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    public static TextView waterLevel;
    public static TextView usage;
    public static TextView todayUsage;
    public static TextView monthlyUsage;
    public static ImageView waterPercentage;
    public Button history;
    public Button monthHistory;
    private Button buttonLogout;
    private SwipeRefreshLayout swipe;

    private int percentage;
    private int viewUsage;
    private double viewUsageToday;
    private double viewUsageThisMonth;

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Map map;
        swipe=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);

        if (!isConnected(Profile.this)) {
            Map<String,Object> storedMap=getData();
            percentage= (int) storedMap.get("percentage");
            viewUsage= (int) storedMap.get("remaining");
            viewUsageToday=(int)storedMap.get("today");
            viewUsageThisMonth=(int)storedMap.get("month");
        } else {
           SummaryBackgroundTask sbt = new SummaryBackgroundTask();
            try {
               map=sbt.execute().get();
                Map<String,Object> childMap1= (Map<String, Object>) map.get("usage");
                Map<String,Object> childMap2= (Map<String, Object>) map.get("level");

                percentage= (int) childMap2.get("percentage");
                viewUsage= (int) childMap2.get("volume");
                viewUsageToday=(int)childMap1.get("day");
                viewUsageThisMonth=(int)childMap1.get("month");
                storeData();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
            doTask();

    }

    private void doTask(){
        //swipe.setOnRefreshListener(this);
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views

        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //displaying logged in user name

        //adding listener to button
        buttonLogout.setOnClickListener(this);


        //====================Connect to the server to get the summary information==================
        waterLevel=(TextView)findViewById(R.id.txtWaterLevel);
        usage=(TextView)findViewById(R.id.txtRemaining);
        todayUsage=(TextView)findViewById(R.id.txtUsageToday);
        monthlyUsage=(TextView)findViewById(R.id.txtMonthlyUsage);
        waterPercentage=(ImageView)findViewById(R.id.waterPercentage);
        history=(Button) findViewById(R.id.buttonDisplayYearSummary);
        monthHistory=(Button)findViewById(R.id.buttonDisplayMonthSummary);

        display();


        history.setOnClickListener(this);
        monthHistory.setOnClickListener(this );
    }

    private void display(){
        Profile.usage.setText(String.valueOf(viewUsage));
        Profile.waterLevel.setText(String.valueOf(percentage)+"%");
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
    }
    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            SharedPreferences sp=getApplicationContext().getSharedPreferences("My Pref",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.remove("email");
            editor.remove("password");
            editor.apply();
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        if(view==history){

            finish();
            startActivity(new Intent(this, yearSummary.class));
        }

        if(view==monthHistory){
            finish();
            startActivity(new Intent(this, monthSummary.class));
        }
    }

    @Override
    public void onRefresh() {
        //i++;
        //text.setText(String.valueOf(i));
        usage.setText("");
        todayUsage.setText("");
        monthlyUsage.setText("");
        waterPercentage.setImageResource(R.drawable.water1);
        doTask();
        android.os.Handler handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        });

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


    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Profile.this, Profile.class));
            }

        });
        return builder;
    }

    public void storeData(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("Profile",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("percentage",percentage);
        editor.putInt("remaining",viewUsage);
        editor.putInt("todayUsage", (int) viewUsageToday);
        editor.putInt("monthlyUsage", (int) viewUsageThisMonth);
        editor.apply();

    }

    public Map getData(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Profile",MODE_PRIVATE);
        int storedPercentage=sharedPref.getInt("percentage",percentage);
        int storedRemaining=sharedPref.getInt("remaining",viewUsage);
        int storedTodayUsage=sharedPref.getInt("todayUsage", (int) viewUsageToday);
        int storedMonthyUsage=sharedPref.getInt("monthlyUsage", (int) viewUsageThisMonth);
        Map<String,Object> map=new HashMap<>();
        map.put("percentage",storedPercentage);
        map.put("remaining",storedRemaining);
        map.put("today",storedTodayUsage);
        map.put("month",storedMonthyUsage);
        return map;
    }
}