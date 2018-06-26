package com.example.llkkmmkmkllk.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextID;
    private EditText editTextCapcity;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private Button signin;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Profile.class));
        }


        //initializing views
        editTextEmail = (EditText) findViewById(R.id.txtEmail);
        editTextPassword = (EditText) findViewById(R.id.txtPassword);
        editTextID=(EditText)findViewById(R.id.txtModuleKey);
        editTextCapcity=(EditText)findViewById(R.id.txtTankCapacity);
        buttonSignup = (Button) findViewById(R.id.btnRegister);
        signin = (Button) findViewById(R.id.btnLogIn);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        int id= Integer.parseInt(editTextID.getText().toString());
        double tankCapacity= Double.parseDouble(editTextCapcity.getText().toString());

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(editTextID.getText())){
            Toast.makeText(this,"Please enter id here",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(editTextCapcity.getText())){
            Toast.makeText(this,"Please enter capacity of the water tank",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty

        //send data to the server
        //ParseData parseData=new ParseData();
        //parseData.sendData(email,id,tankCapacity);
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }else{
                            //display some message here
                            Toast.makeText(SignUp.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == signin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
