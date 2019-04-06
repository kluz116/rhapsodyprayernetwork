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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.allan_musembya.prayer.Firebase.Post;
import org.allan_musembya.prayer.adapters.TestmonyCommentAdapter;
import org.allan_musembya.prayer.models.TestimonyComments;
import org.allan_musembya.prayer.utils.ParseDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestimonyComment extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    Bundle bundle;
    public String name,email,when_added,user_id;
    private EditText mInputMessageView;
    ImageView comment;

    private TestmonyCommentAdapter adapter;
    public List<TestimonyComments> ListOfTestimonyComments;
    private List<String> mCommentIds;
    //SwipeRefreshLayout mSwipeRefreshLayout;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference Mtestimonies,mUser_comment;
    SharedPreferences sp;

    public String timelog;
    RecyclerView recyclerView;
    RemoteImages rtImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testimonycomment_app_bar);
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
        comment.setOnClickListener(this);


        rtImage = new RemoteImages(this);
        //Disable comment button
        comment.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Mtestimonies = FirebaseDatabase.getInstance().getReference("testimonies");
        mUser_comment = FirebaseDatabase.getInstance().getReference("user_comment");



        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);




        ListOfTestimonyComments = new ArrayList<>();
        mCommentIds = new ArrayList<>();

        //get current device time
        timelog = ParseDate.getCurrentDate();

        Mtestimonies.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mCommentIds.add(dataSnapshot.getKey());
                getTestimonyComments(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getTestimonyUpdated(dataSnapshot);

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
                writeCommentTestimony(user_id,name,mInputMessageView.getText().toString(),timelog,rtImage.retrivesharedPreferences());
                obj.SubmitComment(mInputMessageView.getText().toString(),user_id,name,rtImage.retrivesharedPreferences(),mInputMessageView);
                break;
        }
    }


    private void getTestimonyComments(DataSnapshot dataSnapshot){

        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        TestimonyComments a = dataSnapshot.getValue(TestimonyComments.class);
        ListOfTestimonyComments.add(a);
        adapter = new TestmonyCommentAdapter(this, ListOfTestimonyComments);
        adapter.notifyItemInserted(ListOfTestimonyComments.size() - 1);
        recyclerView.setAdapter(adapter);

    }

    private void getTestimonyUpdated(DataSnapshot dataSnapshot){
        TestimonyComments a = dataSnapshot.getValue(TestimonyComments.class);
        ListOfTestimonyComments.add(a);
        String commentKey = dataSnapshot.getKey();

        // [START_EXCLUDE]
        int commentIndex = mCommentIds.indexOf(commentKey);
        if (commentIndex > -1) {
            // Replace with the new data
            ListOfTestimonyComments.set(commentIndex, a);

            // Update the RecyclerView
            adapter.notifyItemChanged(commentIndex);
        } else {
            Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
        }
        // [END_EXCLUDE]
    }

    private void writeCommentTestimony(String user_id, String name, String comment_text, String timelog,String img) {
        String key = mDatabase.child("testimonies").push().getKey();
        Post obj = new Post(user_id,name,comment_text,timelog,img);
        Map<String, Object> postValues = obj.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/testimonies/" + key, postValues);
        childUpdates.put("/user_comment/" + user_id + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TestimonyComment.this, "Comment Shared", Toast.LENGTH_SHORT).show();
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



}
