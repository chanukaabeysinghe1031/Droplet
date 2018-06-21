package com.example.llkkmmkmkllk.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity  implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
}