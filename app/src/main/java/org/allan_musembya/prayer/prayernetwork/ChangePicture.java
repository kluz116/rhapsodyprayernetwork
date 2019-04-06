package org.allan_musembya.prayer.prayernetwork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.takusemba.cropme.CropView;
import com.takusemba.cropme.OnCropListener;

import org.allan_musembya.prayer.adapters.AlbumAdapter;
import org.allan_musembya.prayer.clients.AlbumClient;
import org.allan_musembya.prayer.clients.ImageClient;
import org.allan_musembya.prayer.models.Album;
import org.allan_musembya.prayer.models.Photo;
import org.allan_musembya.prayer.utils.OnPhotoClickListener;

import java.util.ArrayList;
import java.util.List;

public class ChangePicture extends AppCompatActivity {
    private AlbumClient albumClient;
    private ImageClient imageClient;
    private AlbumAdapter adapter;


    private RecyclerView recyclerView;
    private RelativeLayout parent;
    private CropView cropView;
    private ProgressBar progressBar;

    private static final int REQUEST_CODE_PERMISSION = 100;

    Bundle bundle;
    String name,email,when_added,user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profile_app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Crop");
        setSupportActionBar(toolbar);
        findViewsByIds();


        //Get logged in passed data
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        when_added = bundle.getString("when_added");
        user_id = bundle.getString("user_id");

        albumClient = new AlbumClient(this);
        imageClient = new ImageClient(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        OnPhotoClickListener listener = new OnPhotoClickListener() {
            @Override
            public void onPhotoClicked(Photo photo) {
                cropView.setUri(photo.uri);
            }
        };
        adapter = new AlbumAdapter(ChangePicture.this, new ArrayList<Album>(), listener);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChangePicture.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(parent, R.string.error_permission_is_off, Snackbar.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        } else {
            loadAlbums();
        }*/

        loadAlbums();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_picture, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();
        if (id == R.id.crop_picture) {

            cropView.crop(new OnCropListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    saveBitmapAndStartActivity(bitmap);
                }

                @Override
                public void onFailure() {

                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViewsByIds() {
        recyclerView = findViewById(R.id.recycler_view);
        cropView = findViewById(R.id.crop_view);
        parent = findViewById(R.id.container);
        progressBar = findViewById(R.id.progress);
    }

    private void saveBitmapAndStartActivity(final Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        cropView.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageClient.saveBitmap(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        cropView.setEnabled(true);
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("when_added", when_added);
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    private void loadAlbums() {
        adapter.clear();
        final List<Album> result = albumClient.getAlbums();
        for (final Album album : result) {
            new Thread(new Runnable() {
                public void run() {
                    albumClient.getResizedBitmap(ChangePicture.this, album);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!album.photos.isEmpty()) {
                                if (adapter.getItemCount() == 0) {
                                    Photo photo = album.photos.get(0);
                                    cropView.setUri(photo.uri);
                                }
                                adapter.addItem(album);
                            }
                        }
                    });
                }
            }).start();
        }
    }

}
