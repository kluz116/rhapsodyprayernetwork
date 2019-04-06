package org.allan_musembya.prayer.prayernetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.clients.ImageClient;
import org.allan_musembya.prayer.endpoints.Endpoints;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ImageClient imageClient;
    String name,email,when_added,user_id;
    Bundle bundle;

    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final String TAG = "Upload";

    private String KEY_IMAGE = "image";
    private String USER_ID = "user_id";

    ProgressDialog progressDialog;


    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Set Profile Picture");
        setSupportActionBar(toolbar);

        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        imageClient = new ImageClient(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                photo = imageClient.getBitmap();
                //Toast.makeText(ResultActivity.this, ""+photo, Toast.LENGTH_SHORT).show();
                //Toast.makeText(ResultActivity.this, "photo:"+photo, Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.image)).setImageBitmap(photo);
                    }
                });
            }
        }).start();
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();
        if (id == R.id.set_profile) {

            uploadImage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadImage() {
        String cancel_req_tag = "upload_pic";
        progressDialog.setMessage("Please wait...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.UPLOAD_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String errorMsg = jObj.getString("response_msg");
                        Toast.makeText(getApplicationContext(),""+errorMsg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("when_added", when_added);
                        startActivity(intent);

                    } else {
                        String error_crap = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), ""+error_crap, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Response: " + error.toString());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                //Converting Bitmap to String
                String image = getStringImage(photo);
                //Creating parameters
                    //Adding parameters
                    params.put(KEY_IMAGE, image);
                    params.put(USER_ID, user_id);

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
