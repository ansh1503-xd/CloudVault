package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sminfotech.cloudvault.Adapters.ImageAdapter;
import com.sminfotech.cloudvault.R;

public class UploadVideoActivity extends AppCompatActivity {

    RecyclerView rvVideo;
    ImageView back;
    FloatingActionButton fabUploadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        rvVideo = findViewById(R.id.rvVideo);
        fabUploadVideo = findViewById(R.id.fabUploadVideo);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView.Adapter adapter = new ImageAdapter(user.getVideoList(), UploadVideoActivity.this);
        rvVideo.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(UploadVideoActivity.this,3);
        rvVideo.setLayoutManager(manager);

    }
}