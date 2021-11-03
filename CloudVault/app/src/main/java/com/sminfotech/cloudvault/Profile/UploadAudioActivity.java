package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sminfotech.cloudvault.Adapters.AudioAdapter;
import com.sminfotech.cloudvault.R;

public class UploadAudioActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView rvAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        back = findViewById(R.id.back);
        rvAudio = findViewById(R.id.rvAudio);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView.Adapter adapter = new AudioAdapter(UploadAudioActivity.this, user.getAudioList());
        rvAudio.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(UploadAudioActivity.this, LinearLayoutManager.VERTICAL, false);
        rvAudio.setLayoutManager(manager);

    }
}