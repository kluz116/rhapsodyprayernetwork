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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.allan_musembya.prayer.Firebase.Post;
import org.allan_musembya.prayer.adapters.DevotionsAdapter;
import org.allan_musembya.prayer.models.Devotions;
import org.allan_musembya.prayer.utils.ParseDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kluz on 4/19/18.
 */

public class DailyDevotion extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "reader";
    TextView txtProfileName,txtProfileEmail;
    ImageView imgProfile,send_comment,daily_dev;
    LinearLayout root;

    Bundle bundle;
    String name,email,when_added,user_id;
    private EditText mInputMessageView;
    RemoteImages profileImage,rtImage;
    public String timelog;

    private DevotionsAdapter adapter;
    private List<Devotions> ListOfComments;
    private List<String> mCommentIds;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mdevotion;
    SharedPreferences sp;
    RecyclerView recyclerView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_devotion_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Daily Devotion");
        setSupportActionBar(toolbar);

        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");


        mInputMessageView = findViewById(R.id.mInputMessageView);
        send_comment = findViewById(R.id.send_comment);
        daily_dev = findViewById(R.id.daily_dev);
        rtImage = new RemoteImages(this);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mdevotion = FirebaseDatabase.getInstance().getReference("people_comments_rhapsody");

        //get current device time
        timelog = ParseDate.getCurrentDate();


        recyclerView = findViewById(R.id.recycler_view_devotion);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ListOfComments = new ArrayList<>();
        mCommentIds = new ArrayList<>();

        RemoteImages obj = new RemoteImages(this);
        obj.getDailyDevotionEng(daily_dev);
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

        //Disable comment button
        send_comment.setVisibility(View.GONE);

        //text change listerner on the comment button
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    send_comment.setVisibility(View.GONE);
                }else{
                    send_comment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    send_comment.setVisibility(View.GONE);
                }else{
                    send_comment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });//End of text watcher..

        imgProfile.setOnClickListener(this);
        send_comment.setOnClickListener(this);

        mdevotion.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getDevotionsComments(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getDevotionUpdated(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DailyDevotion.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_translate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        RemoteImages obj = new RemoteImages(this);
        obj.getDailyDevotionEng(daily_dev);
        int id = item.getItemId();
        if (id == R.id.en) {
            obj.getDailyDevotionEng(daily_dev);
        }else if(id==R.id.fr){
            obj.getDailyDevotionFR(daily_dev);
        }

        return super.onOptionsItemSelected(item);
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
            case R.id.send_comment:
                NetworkCallBacks obj = new NetworkCallBacks(this);
                writeComment(user_id,name,mInputMessageView.getText().toString(),timelog,rtImage.retrivesharedPreferences());
                obj.SubmitCommentDevotion(mInputMessageView.getText().toString(),user_id,name,rtImage.retrivesharedPreferences(),mInputMessageView);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.go_home) {

            Intent intent = new Intent(DailyDevotion.this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        } else if (id == R.id.daily_devotion) {

        } else if (id == R.id.book_libray) {
            Intent intent = new Intent(DailyDevotion.this, BooksLibray.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_request) {
            Intent intent = new Intent(DailyDevotion.this, PrayerRequest.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_of_salvation) {
            Intent intent = new Intent(DailyDevotion.this, SalvationPrayer.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.testfy_now){
            Intent intent = new Intent(DailyDevotion.this, Testfy.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.give_online){
            Intent intent = new Intent(DailyDevotion.this, GiveOnline.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();

        }else if(id == R.id.live_programs){
            Intent intent = new Intent(DailyDevotion.this, LivePrograms.class);
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

    private void writeComment(String user_id, String name, String comment_text,String timelog,String img) {

        String key = mDatabase.child("people_comments_rhapsody").push().getKey();
        Post obj = new Post(user_id,name,comment_text,timelog,img);
        Map<String, Object> postValues = obj.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/people_comments_rhapsody/" + key, postValues);
        childUpdates.put("/user_comment/" + user_id + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Toast.makeText(CommentActivity.this, "Comment shared..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
    }
    private void getDevotionsComments(DataSnapshot dataSnapshot){

        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        Devotions a = dataSnapshot.getValue(Devotions.class);
        ListOfComments.add(a);
        adapter = new DevotionsAdapter(this, ListOfComments);
        adapter.notifyItemInserted(ListOfComments.size() - 1);
        recyclerView.setAdapter(adapter);

    }

    private void getDevotionUpdated(DataSnapshot dataSnapshot){
        Devotions a = dataSnapshot.getValue(Devotions.class);
        ListOfComments.add(a);
        String commentKey = dataSnapshot.getKey();

        // [START_EXCLUDE]
        int commentIndex = mCommentIds.indexOf(commentKey);
        if (commentIndex > -1) {
            // Replace with the new data
            ListOfComments.set(commentIndex, a);

            // Update the RecyclerView
            adapter.notifyItemChanged(commentIndex);
        } else {
            Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
        }
        // [END_EXCLUDE]
    }

}
