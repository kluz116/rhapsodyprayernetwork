package org.allan_musembya.prayer.prayernetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.endpoints.Endpoints;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity  {

    private static final String TAG = "RegisterActivity";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword;
    private Button btnSignUp;
    private Button btnLinkLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = findViewById(R.id.signup_first_name);
        signupInputPassword = findViewById(R.id.signup_input_password);
        signupInputEmail = findViewById(R.id.signup_email);



        btnSignUp = findViewById(R.id.btn_signup);
        btnLinkLogin = findViewById(R.id.btn_link_login);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });


    }

   private void submitForm() {

       if (signupInputName.getText().toString().isEmpty()){
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
        }else if(signupInputEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT).show();

        }else if(signupInputPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();

        }else{
           registerUser(signupInputName.getText().toString(), signupInputEmail.getText().toString(), signupInputPassword.getText().toString());
        }

    }


    private void registerUser(final String name, final  String email, final String password){
        String cancel_req_tag = "register";

        progressDialog.setMessage("Signing Up "+ name +". Please Wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Json"+ response);

                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), ""+errorMsg, Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String error_crap = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), ""+error_crap, Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error Message: "+e, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Registration Error: " + e);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("name",name);
                params.put("email",email);
                params.put("password",password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


}