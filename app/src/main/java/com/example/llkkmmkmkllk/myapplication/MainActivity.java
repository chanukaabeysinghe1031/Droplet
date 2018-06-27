package com.example.llkkmmkmkllk.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String userID;
    private String deviceID;
    public static final String URL="http://139.59.81.23/apis/droplet/api/v1";

    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private TextView text11;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    public void setUserID(String userID) {this.userID = userID;}
    public void setDeviceID(String deviceID) {this.deviceID = deviceID;}
    public String getUserID() {return userID;}
    public String getDeviceID() {return deviceID;}
    public static String getURL() {return URL;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting firebase auth object

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        buttonSignUp  = (Button) findViewById(R.id.buttonSignUp);
        text11=(TextView)findViewById(R.id.text11);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Profile.class));
        }else{
            String[] arr=getData();
            if(arr!=null) {
                String email = arr[0];
                String password = arr[1];
                String userID=arr[2];
                if(email!=null||password!=null) {
                    userLogin(email, password);
                    text11.setText(userID);
                }
            }
        }

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);

    }

    //method for user login
    private void userLogin(final String email1,final String password1){
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email1)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Loging Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            storeData(email1,password1,firebaseAuth.getUid());
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            final String email = editTextEmail.getText().toString().trim();
            final String password  = editTextPassword.getText().toString().trim();
            userLogin(email,password);
        }

        if(view == buttonSignUp){
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
    }

    public void storeData(String email,String password,String user){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("My Pref",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.putString("userID",user);
        editor.apply();

    }

    public String[] getData(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("My Pref",MODE_PRIVATE);
        String email = sharedPref.getString("email",null);
        String password = sharedPref.getString("password",null);
        String user=sharedPref.getString("userID",null);
        String[] arr=new String[3];
        arr[0]=email;
        arr[1]=password;
        arr[2]=user;
        return arr;
    }

}
