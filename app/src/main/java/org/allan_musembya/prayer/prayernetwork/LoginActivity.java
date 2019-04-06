package org.allan_musembya.prayer.prayernetwork;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.endpoints.Endpoints;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://159.203.97.53/rpnetworkapi/api/login_user";
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    private Button btnlogin;
    private Button btnLinkSignup;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    SharedPreferences sp;

    RemoteImages profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInputEmail = findViewById(R.id.login_input_email);
        loginInputPassword = findViewById(R.id.login_input_password);
        btnlogin = findViewById(R.id.btn_login);
        btnLinkSignup = findViewById(R.id.btn_link_signup);

        sp=getSharedPreferences("login",MODE_PRIVATE);

        if (sp.contains("loginInputEmail") && sp.contains("loginInputPassword")) {
            String name = sp.getString("name",null);
            String when_added = sp.getString("when_added",null);
            String user_id = sp.getString("user_id",null);
            String email =  sp.getString("loginInputEmail",null);
            String password =  sp.getString("loginInputPassword",null);

            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            intent.putExtra("user_id", user_id);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("when_added", when_added);
            startActivity(intent);
            finish();

        }

        // Initialize Firebase Auth
       mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]




        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(loginInputEmail.getText().toString(),loginInputPassword.getText().toString());
                //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(i);
            }
        });

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });






    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    // [START eon_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }




    private void loginUser( final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in.... ");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user_id = jObj.getJSONObject("row").getString("id");
                        String name = jObj.getJSONObject("row").getString("name");
                        String email = jObj.getJSONObject("row").getString("email");
                        String password = jObj.getJSONObject("row").getString("password");
                        String when_added = jObj.getJSONObject("row").getString("when_added");

                        //Launch User activity
                       Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        intent.putExtra("when_added", when_added);

                        SharedPreferences.Editor e=sp.edit();
                        e.putString("loginInputEmail",email);
                        e.putString("loginInputPassword",password);
                        e.putString("name",name);
                        e.putString("user_id",user_id);

                        e.commit();
                        startActivity(intent);
                        finish();

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
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


