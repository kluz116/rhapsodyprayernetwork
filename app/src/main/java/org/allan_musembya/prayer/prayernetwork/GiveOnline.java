package org.allan_musembya.prayer.prayernetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kluz on 4/19/18.
 */

public class GiveOnline extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TextView txtProfileName, txtProfileEmail;
    ImageView imgProfile,give_image;
    Button visa_card_layout;

    Bundle bundle;
    String name, email, when_added, user_id;

    RemoteImages profileImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give Online");
        setSupportActionBar(toolbar);


        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");

        visa_card_layout = findViewById(R.id.visa_card_layout);
        give_image = findViewById(R.id.give_image);

        RemoteImages obj= new RemoteImages(this);
        obj.getGiveImages(give_image);


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
        profileImage.getLoggedInUserPic(user_id, imgProfile);


        imgProfile.setOnClickListener(this);
        visa_card_layout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GiveOnline.this.finish();
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

            Intent intent = new Intent(GiveOnline.this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.daily_devotion) {
            Intent intent = new Intent(GiveOnline.this, DailyDevotion.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.book_libray) {
            Intent intent = new Intent(GiveOnline.this, BooksLibray.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_request) {
            Intent intent = new Intent(GiveOnline.this, PrayerRequest.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_of_salvation) {
            Intent intent = new Intent(GiveOnline.this, SalvationPrayer.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.testfy_now) {
            Intent intent = new Intent(GiveOnline.this, Testfy.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.give_online) {

        } else if (id == R.id.live_programs) {
            Intent intent = new Intent(GiveOnline.this, LivePrograms.class);
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
        switch (view.getId()) {
            case R.id.image_header:
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("user_id", user_id);
                intent.putExtra("when_added", when_added);
                startActivity(intent);
                finish();
                break;
            case R.id.visa_card_layout:
                Intent purchase = new Intent(getApplicationContext(), Purchase.class);
                purchase.putExtra("name", name);
                purchase.putExtra("email", email);
                purchase.putExtra("user_id", user_id);
                purchase.putExtra("when_added", when_added);
                startActivity(purchase);

                break;
        }
    }
}