package com.example.llkkmmkmkllk.myapplication;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Transaction;

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
    public static int percentage;
    private Button buttonLogout;
    private SwipeRefreshLayout swipe;
    //private TextView text;

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        swipe=(SwipeRefreshLayout)findViewById(R.id.swipe);
        //text=(TextView)findViewById(R.id.text1);
        //text.setText(String.valueOf(i));
        swipe.setOnRefreshListener(this);
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


        history.setOnClickListener(this);
        monthHistory.setOnClickListener(this );
        SummaryBackgroundTask bt=new SummaryBackgroundTask();
        bt.execute();
    }
    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
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
        doTask();
        android.os.Handler handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        });

    }

//    @Override
//    public void onRefresh() {
//        swipe.setRefreshing(true);
//    }
}