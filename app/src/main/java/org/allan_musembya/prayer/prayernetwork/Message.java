package org.allan_musembya.prayer.prayernetwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Message extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Message");
        setSupportActionBar(toolbar);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }
}
