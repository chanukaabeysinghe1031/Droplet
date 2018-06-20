package com.example.llkkmmkmkllk.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    public static EditText email;
    public static EditText password;
    public static EditText confirmPassword;
    public static EditText moduleKey;
    public static EditText tankCapacity;

    Button register;
    Button logInPage;


    RequestQueue requestQueue;
    String insertURL="http:// 192.168.8.100/Droplet/insertCustomers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email=(EditText)findViewById(R.id.txtEmail);
        password=(EditText)findViewById(R.id.txtPassword);
        confirmPassword=(EditText)findViewById(R.id.txtConfirmPassword);
        moduleKey=(EditText)findViewById(R.id.txtModuleKey);
        tankCapacity=(EditText)findViewById(R.id.txtTankCapacity);
        register=(Button)findViewById(R.id.btnRegister);
        logInPage=(Button)findViewById(R.id.btnLogIn);

        requestQueue= Volley.newRequestQueue(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        if(v==register){
            registerCustomer();
        }
        if(v==logInPage){

        }
    }

    private void registerCustomer(){
        StringRequest request=new StringRequest(Request.Method.POST, insertURL, new Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> parameters=new HashMap<>();
                parameters.put("id",moduleKey.getText().toString());
                parameters.put("email",email.getText().toString());
                parameters.put("password",password.getText().toString());
                parameters.put("tankcapacity",tankCapacity.getText().toString());

                return parameters;
            }
        };

        requestQueue.add(request);
    }
}
