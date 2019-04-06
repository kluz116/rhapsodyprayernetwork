package org.allan_musembya.prayer.prayernetwork;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.Firebase.Post;
import org.allan_musembya.prayer.adapters.CommentAdapter;
import org.allan_musembya.prayer.endpoints.Endpoints;
import org.allan_musembya.prayer.models.Comments;
import org.allan_musembya.prayer.utils.ParseDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kluz on 4/20/18.
 */

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    Bundle bundle;
    public String name,email,when_added,user_id;
    private EditText mInputMessageView;
    ImageView comment;

    private CommentAdapter adapter;
    private List<Comments> ListOfComments;
    private List<String> mCommentIds;
    //SwipeRefreshLayout mSwipeRefreshLayout;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mPrayer_point,mUser_comment;
    SharedPreferences sp;

    public String timelog,user_image;
    RecyclerView recyclerView;
    RemoteImages rtImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Comment");
        setSupportActionBar(toolbar);

        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");
        mInputMessageView = findViewById(R.id.mInputMessageView);
        comment = findViewById(R.id.comment_btn);
        //mSwipeRefreshLayout = findViewById(R.id.swifeRefresh);

        getLoggedInUserPic (user_id);
        rtImage = new RemoteImages(this);
        comment.setOnClickListener(this);

        //Disable comment button
        comment.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPrayer_point = FirebaseDatabase.getInstance().getReference("prayer_point");
        mUser_comment = FirebaseDatabase.getInstance().getReference("user_comment");



        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);




        ListOfComments = new ArrayList<>();
        mCommentIds = new ArrayList<>();

        //get current device time
        timelog = ParseDate.getCurrentDate();

        mPrayer_point.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mCommentIds.add(dataSnapshot.getKey());
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

        //text change listerner on the comment button
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    comment.setVisibility(View.GONE);
                }else{
                    comment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    comment.setVisibility(View.GONE);
                }else{
                    comment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });//End of text watcher..

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_btn:
                NetworkCallBacks obj = new NetworkCallBacks(this);
                writeComment(user_id,name,mInputMessageView.getText().toString(),timelog,rtImage.retrivesharedPreferences());
                obj.SubmitCommentPrayer(user_id,name,mInputMessageView.getText().toString(),rtImage.retrivesharedPreferences(),mInputMessageView);
                break;
        }
    }

    private void getPrayerPointComments(DataSnapshot dataSnapshot){

        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        Comments a = dataSnapshot.getValue(Comments.class);
        ListOfComments.add(a);
        adapter = new CommentAdapter(this, ListOfComments);
        adapter.notifyItemInserted(ListOfComments.size() - 1);
        recyclerView.setAdapter(adapter);

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
            adapter.notifyItemChanged(commentIndex);
        } else {
            Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
        }
        // [END_EXCLUDE]
    }

    private void writeComment(String user_id, String name, String comment_text,String timelog,String img) {

        String key = mDatabase.child("prayer_point").push().getKey();
        Post obj = new Post(user_id,name,comment_text,timelog,img);
        Map<String, Object> postValues = obj.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/prayer_point/" + key, postValues);
        //childUpdates.put("/prayer_point/", postValues);
        //childUpdates.put("/user_comment/" + user_id + "/" + key, postValues);

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

    public void getLoggedInUserPic (final String user_id){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.PROFILE_IMG,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                //Glide.with(getApplicationContext()).load(obj.getString("image")).placeholder(R.drawable.profile).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);
                                user_image = obj.getString("image");
                            }
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

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }

        };

        queue.add(arrayreq);


    }

}
