package org.allan_musembya.prayer.prayernetwork;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

/**
 * Created by kluz on 4/19/18.
 */

public class PrayerRequest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "PrayerRequest";
    ProgressDialog progressDialog;

    TextView txtProfileName,txtProfileEmail;
    ImageView imgProfile;
    Button prayer_request;
    EditText input_prayer_request;
    Bundle bundle;
    String name,email,when_added,user_id;
    RemoteImages profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prayer_request_main_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Prayer Request");
        setSupportActionBar(toolbar);


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //Bind view elements
        prayer_request = findViewById(R.id.prayer_request);
        input_prayer_request = findViewById(R.id.input_prayer_request);


        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");;



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        txtProfileName = navigationView.getHeaderView(0).findViewById(R.id.user);
        txtProfileEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        imgProfile = navigationView.getHeaderView(0).findViewById(R.id.image_header);
        txtProfileName.setText(name);
        txtProfileEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);

        profileImage = new RemoteImages(this);
        profileImage.getLoggedInUserPic(user_id,imgProfile);

        imgProfile.setOnClickListener(this);
        prayer_request.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PrayerRequest.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.go_home) {

            Intent intent = new Intent(PrayerRequest.this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.daily_devotion) {
            Intent intent = new Intent(PrayerRequest.this, DailyDevotion.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.book_libray) {
            Intent intent = new Intent(PrayerRequest.this, BooksLibray.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_request) {

        } else if (id == R.id.prayer_of_salvation) {
            Intent intent = new Intent(PrayerRequest.this, SalvationPrayer.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.testfy_now){
            Intent intent = new Intent(PrayerRequest.this, Testfy.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.give_online){
            Intent intent = new Intent(PrayerRequest.this, GiveOnline.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.live_programs){
            Intent intent = new Intent(PrayerRequest.this, LivePrograms.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.log_out) {
            //Logs out logged in user and clears the sharedpreference
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.clear();
            e.commit();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Call back fired to add individual user request...
    private void SubmitPrayerRequest(final String userid,final String names, final  String email, final String request){
        String cancel_req_tag = "prayer";

        progressDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_PRAYER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Json"+ response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String errorMsg = jObj.getString("response_msg");
                        Toast.makeText(getApplicationContext(), ""+errorMsg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                        finish();

                    } else {
                        String error_crap = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), ""+error_crap, Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Prayer Error: " + e);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Prayer Error: " + error.getMessage());
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid",userid);
                params.put("names",names);
                params.put("email",email);
                params.put("request",request);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_header:
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("user_id", user_id);
                intent.putExtra("when_added", when_added);
                startActivity(intent);
                finish();
                break;
            case R.id.prayer_request:
                if(input_prayer_request.getText().toString().isEmpty()){
                    Toast.makeText(this, "Prayer Request can't be empty", Toast.LENGTH_SHORT).show();
                }else{
                    SubmitPrayerRequest(user_id,name,email,input_prayer_request.getText().toString());
                }

                break;
        }
    }
}
