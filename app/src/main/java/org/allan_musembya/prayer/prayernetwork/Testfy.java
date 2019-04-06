package org.allan_musembya.prayer.prayernetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.adapters.TestimonyAdapter;
import org.allan_musembya.prayer.models.Testimony;
import org.allan_musembya.prayer.utils.CheckConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kluz on 4/19/18.
 */

public class Testfy extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TextView txtProfileName,txtProfileEmail;
    ImageView imgProfile;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1;
    Bundle bundle;
    String name,email,when_added,user_id;

    RemoteImages profileImage;

    private TestimonyAdapter adapter;
    private List<Testimony> testimonyList;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testfy_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Testimonies");
        setSupportActionBar(toolbar);


        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1);
        mSwipeRefreshLayout = findViewById(R.id.swifeRefresh);

        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");

       //checking if connection is available
        if (CheckConnection.isInternetAvailable(getApplicationContext())) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getAllTestimonies();
                }
            });
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.clear();
                    getAllTestimonies();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection..", Toast.LENGTH_LONG).show();
        }




        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        testimonyList = new ArrayList<>();
        adapter = new TestimonyAdapter(this, testimonyList);
        recyclerView.setAdapter(adapter);




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
        floatingActionButton1.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        imgProfile.setOnClickListener(this);

        profileImage = new RemoteImages(this);
        profileImage.getLoggedInUserPic(user_id,imgProfile);


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Testfy.this.finish();
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

            Intent intent = new Intent(Testfy.this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.daily_devotion) {
            Intent intent = new Intent(Testfy.this, DailyDevotion.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.book_libray) {
            Intent intent = new Intent(Testfy.this, BooksLibray.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_request) {
            Intent intent = new Intent(Testfy.this, PrayerRequest.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_of_salvation) {
            Intent intent = new Intent(Testfy.this, SalvationPrayer.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.testfy_now){

        }else if(id == R.id.give_online){
            Intent intent = new Intent(Testfy.this, GiveOnline.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.live_programs){
            Intent intent = new Intent(Testfy.this, LivePrograms.class);
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
            case R.id.material_design_floating_action_menu_item1:
                Intent i = new Intent(getApplicationContext(), SubmitTestimony.class);
                i.putExtra("name", name);
                i.putExtra("email", email);
                i.putExtra("user_id", user_id);
                i.putExtra("when_added", when_added);
                startActivity(i);
                break;
        }
    }

    private void getAllTestimonies() {
        mSwipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://159.203.97.53/rpnetworkapi/api/get_testimonies";
        StringRequest arrayreq = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                Testimony a = new Testimony(obj.getString("userid"),obj.getString("names"),obj.getString("email"),obj.getString("testimony"),obj.getString("timelog"));
                                testimonyList.add(a);
                                adapter.notifyDataSetChanged();

                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                })

        {


        };

        queue.add(arrayreq);


    }
}
