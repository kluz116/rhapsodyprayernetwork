package org.allan_musembya.prayer.prayernetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.Firebase.Post;
import org.allan_musembya.prayer.adapters.CommentAdapters;
import org.allan_musembya.prayer.adapters.TestmonyCommentAdapter;
import org.allan_musembya.prayer.endpoints.Endpoints;
import org.allan_musembya.prayer.models.Comments;
import org.allan_musembya.prayer.models.TestimonyComments;
import org.allan_musembya.prayer.utils.CheckConnection;
import org.allan_musembya.prayer.utils.GreetingMaker;
import org.allan_musembya.prayer.utils.ParseDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG ="" ;
    private BetterVideoPlayer bvp;
    String timelog;
    TextView count_testimony,txtProfileName,txtProfileEmail,timerTextView,comment_on_prayer,session_logged,testimony_heading,testimony_details,testimony_date,dateOfPrayerPoint,prayer_heading,prayer_details;
    ImageView monthly_image,image_prayer_pont,rppn_news,give_image,devotion_image,prayer_request_image,itestify,imgProfile,imageView6,logedUser,send_comment,send_testimony;
    Button  watch_more_videos,mButtonStartPause,navigate_prayer_request,testimony_navigation,click_to_give,see_more_books,read_todays_devotion;
    EditText mInputMessageView,comment_on_testmony;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Bundle bundle;
    String name,email,when_added,user_id;
    MediaPlayer player;
    LinearLayout prayer_layout,live_video,watch_online;
    private RecyclerView recyclerView,recyclerView2;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
    private CommentAdapters recyclerViewAdapter;
    private TestmonyCommentAdapter adapter;
    public List<Comments> ListOfComments;
    public List<TestimonyComments> ListOfTestimonyComments;
    private List<String> mCommentIds;
    private static final long START_TIME_IN_MILLIS = 300000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    String getDate;
    Toolbar toolbar;
    private Drawable drawable,drawable2;
    RemoteImages profileImage,rtImage;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mPrayer_point,Mtestimonies;
    // [END declare_database_ref]





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = new Locale("fr");
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timerTextView =findViewById(R.id.textView);
       // tabDigit1 = findViewById(R.id.textView);
        session_logged = findViewById(R.id.session_logged);
        comment_on_prayer = findViewById(R.id.comment_on_prayer);
        mInputMessageView = findViewById(R.id.mInputMessageView);
        comment_on_testmony = findViewById(R.id.comment_on_testmony);
        testimony_heading = findViewById(R.id.testimony_heading);
        testimony_details = findViewById(R.id.testimony_details);
        testimony_date = findViewById(R.id.testimony_date);
        dateOfPrayerPoint = findViewById(R.id.dateOfPrayerPoint);
        prayer_heading = findViewById(R.id.prayer_heading);
        prayer_details = findViewById(R.id.prayer_details);
        imageView6 = findViewById(R.id.imageView6);
        send_comment = findViewById(R.id.send_comment);
        send_testimony = findViewById(R.id.send_testimony);
        logedUser = findViewById(R.id.logedUser);
        mButtonStartPause = findViewById(R.id.button2);
        navigate_prayer_request = findViewById(R.id.navigate_prayer_request);
        testimony_navigation = findViewById(R.id.testimony_navigation);
        click_to_give = findViewById(R.id.click_to_give);
        see_more_books = findViewById(R.id.see_more_books);
        read_todays_devotion = findViewById(R.id.read_todays_devotion);
        watch_more_videos = findViewById(R.id.watch_more_videos);
       count_testimony = findViewById(R.id.count_testimony);
        image_prayer_pont = findViewById(R.id.image_prayer_pont);
        rppn_news = findViewById(R.id.rppn_news);
        give_image = findViewById(R.id.give_image);
        devotion_image = findViewById(R.id.devotion_image);
        prayer_request_image = findViewById(R.id.prayer_request_image);
        itestify = findViewById(R.id.itestify);
        monthly_image = findViewById(R.id.monthly_image);
        mSwipeRefreshLayout = findViewById(R.id.swifeRefresh);
        prayer_layout = findViewById(R.id.prayer);
        live_video = findViewById(R.id.live_video);
        watch_online = findViewById(R.id.watch_online);


        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");
        player = MediaPlayer.create(getApplicationContext(),R.raw.kluz);


        //Initialise Remoteimage and Bind data on the view
        RemoteImages obj = new RemoteImages(this);
        obj.getPrayerPointImages(image_prayer_pont);
        obj.getRppnImages(rppn_news);
        obj.getGiveImages(give_image);
        obj.getDevotion(devotion_image);
        obj.getPrayerRequest(prayer_request_image);
        obj.getITesfy(itestify);
        obj.getMonthly(monthly_image);
        rtImage = new RemoteImages(this);
        //initialize_database_ref
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPrayer_point = FirebaseDatabase.getInstance().getReference("prayer_point");
        Mtestimonies = FirebaseDatabase.getInstance().getReference("testimonies");



        //checking if connection is available
        if (CheckConnection.isInternetAvailable(getApplicationContext())) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getAllTestimonies();
                    getPrayerPoint();
                }
            });
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    getAllTestimonies();
                    getPrayerPoint();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection..", Toast.LENGTH_LONG).show();
        }

        //Set recyclerView for the prayer point comments
        recyclerView = findViewById(R.id.recycler_view_kluz);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Set recyclerView for the prayer point comments
        recyclerView2 = findViewById(R.id.recycler_view_testimony);
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        ListOfComments = new ArrayList<Comments>();
        ListOfTestimonyComments = new ArrayList<>();
        mCommentIds    = new ArrayList<>();


        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);



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


        profileImage = new RemoteImages(this);
        profileImage.getLoggedInUserPic(user_id,imgProfile);
        profileImage = new RemoteImages(this);
        profileImage.getLoggedInUserPic(user_id,logedUser);


        session_logged.setText(""+GreetingMaker.getMessage()+", "+name);


        //Click listeners for events
        imgProfile.setOnClickListener(this);
        mButtonStartPause.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        logedUser.setOnClickListener(this);
        comment_on_prayer.setOnClickListener(this);
        send_comment.setOnClickListener(this);
        send_testimony.setOnClickListener(this);
        navigate_prayer_request.setOnClickListener(this);
        testimony_navigation.setOnClickListener(this);
        click_to_give.setOnClickListener(this);
        see_more_books.setOnClickListener(this);
        read_todays_devotion.setOnClickListener(this);
        watch_more_videos.setOnClickListener(this);
        count_testimony.setOnClickListener(this);
        mInputMessageView.setOnClickListener(this);
        comment_on_testmony.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        //set send comment button invisible first
        send_comment.setVisibility(View.GONE);
        send_testimony.setVisibility(View.GONE);
        prayer_layout.setVisibility(View.GONE);
        live_video.setVisibility(View.GONE);
        watch_online.setVisibility(View.GONE);

        if (ParseDate.getDayOfMonth()){
            live_video.setVisibility(View.VISIBLE);
        }else{
            prayer_layout.setVisibility(View.VISIBLE);
            watch_online.setVisibility(View.VISIBLE);
        }

        //Call the collapsing metthod
        initCollapsingToolbar();
        updateCountDownText();
        //String videoUri = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        timelog = ParseDate.getCurrentDate();


        bvp = findViewById(R.id.online_programs);

        if(savedInstanceState == null) {
            bvp.setAutoPlay(false);
            bvp.setSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
            //bvp.setCaptions(R.raw.sub, CaptionsView.CMime.SUBRIP);
        }

        bvp.setHideControlsOnPlay(true);


        mPrayer_point.limitToLast(3).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getPrayerPointComments(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getPrayerPointCommentsUpdated(dataSnapshot);
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
        Mtestimonies.limitToLast(3).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getTestimonyComments(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //getPrayerPointCommentsUpdated(dataSnapshot);
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

    mDatabase.child("prayer_point").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            getCommentsCount(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    mDatabase.child("testimonies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getTestimonyCommentsCount(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        //text change listerner on the comment button
        comment_on_testmony.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    send_testimony.setVisibility(View.GONE);
                }else{
                    send_testimony.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    send_testimony.setVisibility(View.GONE);
                }else{
                    send_testimony.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });//End of text watcher..


    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                player.start();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Click to Pray");
                updateCountDownText();
                player.stop();

            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("Pause Prayer");
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Resume Prayer");
        player.pause();

    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }
    public void checkMediaPlayerRunning(){
        if (player.isPlaying()){
            player.stop();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                        checkMediaPlayerRunning();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.message, menu);
        getMenuInflater().inflate(R.menu.main, menu);


        //drawable = menu.getItem(1).getIcon(); // set 0 if you have only one item in menu
        //drawable = menu.getItem(1).getIcon(); // set 0 if you have only one item in menu
        //this also will work
        drawable2 = menu.findItem(R.id.message).getIcon();

        //

        if(drawable2!=null) {

            drawable2.mutate();
            drawable2.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();
        if (id == R.id.message) {
            Intent intent = new Intent(MainActivity.this, Message.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.go_home) {

        } else if (id == R.id.daily_devotion) {
            Intent intent = new Intent(MainActivity.this, DailyDevotion.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.book_libray) {
            Intent intent = new Intent(MainActivity.this, BooksLibray.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_request) {
            Intent intent = new Intent(MainActivity.this, PrayerRequest.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        } else if (id == R.id.prayer_of_salvation) {
            Intent intent = new Intent(MainActivity.this, SalvationPrayer.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.testfy_now){
            Intent intent = new Intent(MainActivity.this, Testfy.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.give_online){
            Intent intent = new Intent(MainActivity.this, GiveOnline.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
            finish();
        }else if(id == R.id.live_programs){
            Intent intent = new Intent(MainActivity.this, LivePrograms.class);
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

    private void initCollapsingToolbar() {

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Home");
                    drawable2.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                   // drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                    drawable2.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    //drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    private void getAllTestimonies() {
        mSwipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_TESTIMONY_LATEAST,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                testimony_heading.setText(""+obj.getString("names")+"'s Testimony");
                                testimony_details.setText(obj.getString("testimony"));
                                testimony_date.setText(ParseDate.getDateParsed(obj.getString("timelog")));
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


    private void getPrayerPoint() {
        mSwipeRefreshLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_PRAYER_POINT,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                prayer_heading.setText(obj.getString("prayer_title"));
                                prayer_details.setText(obj.getString("prayer_content"));
                                dateOfPrayerPoint.setText(obj.getString("prayer_date"));
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




    @Override
    public void onClick(View view) {
        NetworkCallBacks obj = new NetworkCallBacks(this);
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
            case R.id.logedUser:
                Intent logedUser_intent = new Intent(getApplicationContext(), Profile.class);
                logedUser_intent.putExtra("name", name);
                logedUser_intent.putExtra("email", email);
                logedUser_intent.putExtra("user_id", user_id);
                logedUser_intent.putExtra("when_added", when_added);
                startActivity(logedUser_intent);
                finish();
                break;
            case R.id.button2:
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
                break;

            case R.id.send_comment:
                writeComment(user_id,name,mInputMessageView.getText().toString(),timelog,rtImage.retrivesharedPreferences());
                obj.SubmitCommentPrayer(user_id,name,mInputMessageView.getText().toString(),rtImage.retrivesharedPreferences(),mInputMessageView);
                break;
            case R.id.send_testimony:
                writeCommentTestimony(user_id,name,comment_on_testmony.getText().toString(),timelog,rtImage.retrivesharedPreferences());
                obj.SubmitComment(mInputMessageView.getText().toString(),user_id,name,rtImage.retrivesharedPreferences(),mInputMessageView);
                break;
            case R.id.comment_on_prayer:
                Intent cmt = new Intent(getApplicationContext(), CommentActivity.class);
                cmt.putExtra("name", name);
                cmt.putExtra("email", email);
                cmt.putExtra("user_id", user_id);
                cmt.putExtra("when_added", when_added);
                startActivity(cmt);
                break;
            case R.id.count_testimony:
                Intent count_testimony_itent = new Intent(getApplicationContext(), TestimonyComment.class);
                count_testimony_itent.putExtra("name", name);
                count_testimony_itent.putExtra("email", email);
                count_testimony_itent.putExtra("user_id", user_id);
                count_testimony_itent.putExtra("when_added", when_added);
                startActivity(count_testimony_itent);
                break;
            case  R.id.navigate_prayer_request:
                Intent intent_prayer_request = new Intent(getApplicationContext(), PrayerRequest.class);
                intent_prayer_request.putExtra("name", name);
                intent_prayer_request.putExtra("email", email);
                intent_prayer_request.putExtra("user_id", user_id);
                intent_prayer_request.putExtra("when_added", when_added);
                startActivity(intent_prayer_request);
                //finish();
                break;
            case R.id.testimony_navigation:
                Intent intent_testimony = new Intent(getApplicationContext(), Testfy.class);
                intent_testimony.putExtra("name", name);
                intent_testimony.putExtra("email", email);
                intent_testimony.putExtra("user_id", user_id);
                intent_testimony.putExtra("when_added", when_added);
                startActivity(intent_testimony);
                //finish();
                break;
            case R.id.click_to_give:
                Intent in = new Intent(getApplicationContext(), GiveOnline.class);
                in.putExtra("name", name);
                in.putExtra("email", email);
                in.putExtra("user_id", user_id);
                in.putExtra("when_added", when_added);
                startActivity(in);
                finish();
                break;
            case R.id.see_more_books:
                Intent see_more_intent = new Intent(getApplicationContext(), BooksLibray.class);
                see_more_intent.putExtra("name", name);
                see_more_intent.putExtra("email", email);
                see_more_intent.putExtra("user_id", user_id);
                see_more_intent.putExtra("when_added", when_added);
                startActivity(see_more_intent);
                finish();
                break;
            case R.id.read_todays_devotion:
                Intent devotion_intent = new Intent(getApplicationContext(), DailyDevotion.class);
                devotion_intent.putExtra("name", name);
                devotion_intent.putExtra("email", email);
                devotion_intent.putExtra("user_id", user_id);
                devotion_intent.putExtra("when_added", when_added);
                startActivity(devotion_intent);
                finish();
                break;
            case R.id.watch_more_videos:
                Intent watch_more_videos_intent = new Intent(getApplicationContext(), LivePrograms.class);
                watch_more_videos_intent.putExtra("name", name);
                watch_more_videos_intent.putExtra("email", email);
                watch_more_videos_intent.putExtra("user_id", user_id);
                watch_more_videos_intent.putExtra("when_added", when_added);
                startActivity(watch_more_videos_intent);
                finish();
                break;
            case R.id.mInputMessageView:

                Intent cmti = new Intent(getApplicationContext(), CommentActivity.class);
                cmti.putExtra("name", name);
                cmti.putExtra("email", email);
                cmti.putExtra("user_id", user_id);
                cmti.putExtra("when_added", when_added);
                startActivity(cmti);

                break;
            case R.id.comment_on_testmony:
                Intent testimony = new Intent(getApplicationContext(), TestimonyComment.class);
                testimony.putExtra("name", name);
                testimony.putExtra("email", email);
                testimony.putExtra("user_id", user_id);
                testimony.putExtra("when_added", when_added);
                startActivity(testimony);
                break;
        }
    }

    //Read all the availble comments from the firebase and map them on the recycle view
    private void getPrayerPointComments(DataSnapshot dataSnapshot){
        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        Comments a = dataSnapshot.getValue(Comments.class);
        mCommentIds.add(dataSnapshot.getKey());
        ListOfComments.add(a);
        recyclerViewAdapter = new CommentAdapters(this, ListOfComments);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyItemInserted(ListOfComments.size() - 1);



    }
    //Read all the availble comments from the firebase and map them on the recycle view
    private void getTestimonyComments(DataSnapshot dataSnapshot){
        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        TestimonyComments a = dataSnapshot.getValue(TestimonyComments.class);
        mCommentIds.add(dataSnapshot.getKey());
        ListOfTestimonyComments.add(a);

        adapter = new TestmonyCommentAdapter(this, ListOfTestimonyComments);
        recyclerView2.setAdapter(adapter);
        adapter.notifyItemInserted(ListOfTestimonyComments.size() - 1);


    }
    private void getPrayerPointCommentsUpdated(DataSnapshot dataSnapshot){
        Comments a = dataSnapshot.getValue(Comments.class);
        ListOfComments.add(a);
        String commentKey = dataSnapshot.getKey();

        // [START_EXCLUDE]
        int commentIndex = mCommentIds.indexOf(commentKey);
        if (commentIndex > -1) {
            // Replace with the new data
            ListOfComments.set(commentIndex, a);

            // Update the RecyclerView
            recyclerViewAdapter.notifyItemChanged(commentIndex);
        } else {
            Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
        }
        // [END_EXCLUDE]
    }


    //count comments on the prayer point and set them on the text view
    private void getCommentsCount(DataSnapshot dataSnapshot){
        int count = (int) dataSnapshot.getChildrenCount();
        comment_on_prayer.setText(""+String.valueOf(count)+" Comments");
    }
    private void getTestimonyCommentsCount(DataSnapshot dataSnapshot){
        int count = (int) dataSnapshot.getChildrenCount();
        count_testimony.setText(""+String.valueOf(count)+" Comments");
    }
    private void writeComment(String user_id, String name, String comment_text, String timelog,String img) {
        String key = mDatabase.child("prayer_point").push().getKey();
        Post obj = new Post(user_id,name,comment_text,timelog,img);
        Map<String, Object> postValues = obj.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/prayer_point/" + key, postValues);
        //childUpdates.put("/prayer_point/", postValues);
        childUpdates.put("/user_comment/" + user_id + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Comment Shared", Toast.LENGTH_SHORT).show();
                        mInputMessageView.getText().clear();
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

    private void writeCommentTestimony(String user_id, String name, String comment_text, String timelog,String img) {
        String key = mDatabase.child("testimonies").push().getKey();
        Post obj = new Post(user_id,name,comment_text,timelog,img);
        Map<String, Object> postValues = obj.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/testimonies/" + key, postValues);
        childUpdates.put("/user_comment/" + user_id + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        comment_on_testmony.getText().clear();
               /* .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Comment Shared", Toast.LENGTH_SHORT).show();
                        //comment_on_testmony.getText().clear();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });*/
    }

}
