package org.allan_musembya.prayer.prayernetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by kluz on 4/22/18.
 */

public class SubmitTestimony extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SubmitTestimony";
    ProgressDialog progressDialog;
    EditText input_testimony;
    Button submit_testimony;
    ImageView testimony_image;

    Bundle bundle;
    String name,email,when_added,user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testimony_submission_app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Submit Testimony");
        setSupportActionBar(toolbar);

        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");

       //Bind view elements
        testimony_image = findViewById(R.id.testimony_image);
        input_testimony = findViewById(R.id.input_testimony);
        submit_testimony = findViewById(R.id.submit_testimony);


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RemoteImages obj = new RemoteImages(this);
        obj.getITesfy(testimony_image);

        //On click event listerner
        submit_testimony.setOnClickListener(this);

    }


   //Call back fired to add individual user testimonies...
    private void SubmitTestimony(final String userid,final String names, final  String email, final String testimony){
        String cancel_req_tag = "testimony";

        progressDialog.setMessage("Sharing your testimony "+ names +". Please Wait...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_TESTIMONY, new Response.Listener<String>() {

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
                        Intent intent = new Intent(getApplicationContext(), Testfy.class);
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
                    Log.e(TAG, "Testimony Error: " + e);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Testimony Error: " + error.getMessage());
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
                params.put("testimony",testimony);
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
            case R.id.submit_testimony:
                if(input_testimony.getText().toString().isEmpty()){
                    Toast.makeText(this, "Testimony can't be empty", Toast.LENGTH_SHORT).show();
                }else{
                    SubmitTestimony(user_id,name,email,input_testimony.getText().toString());
                }

                break;
        }
    }
}
