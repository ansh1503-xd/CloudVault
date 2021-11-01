package com.sminfotech.cloudvault;

import static com.unity3d.services.core.properties.ClientProperties.getActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class ImageOrVideoActivity extends AppCompatActivity {

    public static String imageUrl;
    ImageView imageFromIntent, close;
    FloatingActionButton fabDownloadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_or_video);

        imageUrl = getIntent().getStringExtra("imageUrl");
        imageFromIntent = findViewById(R.id.imageFromIntent);
        fabDownloadImage = findViewById(R.id.fabDownloadImage);
        close = findViewById(R.id.close);

        Glide.with(ImageOrVideoActivity.this).load(imageUrl).into(imageFromIntent);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fabDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getImage(imageUrl);
                getImage(imageUrl);
            }
        });

    }

    private void getImage(String imageUrl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/CloudVault");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(imageUrl);

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/CloudVault", "fileName.jpg");
        mgr.enqueue(request);

        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_LONG).show();

    }

}