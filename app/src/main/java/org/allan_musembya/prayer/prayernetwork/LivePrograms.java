package org.allan_musembya.prayer.prayernetwork;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.allan_musembya.prayer.adapters.VideoAdapter;
import org.allan_musembya.prayer.models.Videos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kluz on 4/19/18.
 */

public class LivePrograms extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TextView txtProfileName,txtProfileEmail;
    ImageView imgProfile;

    Bundle bundle;
    String name,email,when_added,user_id;
    RemoteImages profileImage;

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Videos> ListOfVideos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_programs_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Live Programs");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);



        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");

        ListOfVideos = new ArrayList<>();
        adapter = new VideoAdapter(this, ListOfVideos);

        prepareVideos();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
        navigationView.setNavigationItemSelectedListener(this);

        profileImage = new RemoteImages(this);
        profileImage.getLoggedInUserPic(user_id,imgProfile);

        imgProfile.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LivePrograms.this.finish();
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

            Intent intent = new Intent(LivePrograms.this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.daily_devotion) {
            Intent intent = new Intent(LivePrograms.this, DailyDevotion.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.book_libray) {
            Intent intent = new Intent(LivePrograms.this, BooksLibray.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.prayer_request) {
            Intent intent = new Intent(LivePrograms.this, PrayerRequest.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.prayer_of_salvation) {
            Intent intent = new Intent(LivePrograms.this, SalvationPrayer.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.testfy_now){
            Intent intent = new Intent(LivePrograms.this, Testfy.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.give_online){
            Intent intent = new Intent(LivePrograms.this, GiveOnline.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.live_programs){

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
        }
    }

    private void prepareVideos() {

        Videos a = new Videos("Watch online Events","watch videos","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","watch videos","watch videos");
        ListOfVideos.add(a);
        a = new Videos("Watch online Events","watch videos","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","watch videos","watch videos");
        ListOfVideos.add(a);
        a = new Videos("Watch online Events","watch videos","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4","watch videos","watch videos");
        ListOfVideos.add(a);
        adapter.notifyDataSetChanged();
    }
}
