package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.MainActivity.user;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sminfotech.cloudvault.Adapters.ImageAdapter;

public class UploadImageActivity extends AppCompatActivity {

    RecyclerView rvImage;
    FloatingActionButton fabUploadImage;
    ActivityResultLauncher<Intent> galleryResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        rvImage = findViewById(R.id.rvImage);
        fabUploadImage = findViewById(R.id.fabUploadImage);

        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                        }
                    }
                });

        RecyclerView.Adapter adapter = new ImageAdapter(user.getImageList(), UploadImageActivity.this);
        rvImage.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(UploadImageActivity.this,3);
        rvImage.setLayoutManager(manager);

        fabUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(UploadImageActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_media_popup);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LinearLayout gallerySelect = dialog.findViewById(R.id.gallerySelect);
                LinearLayout takePicture = dialog.findViewById(R.id.takePicture);
                gallerySelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageFromGallery(view);
                    }
                });
                takePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageFromCamera(view);
                    }
                });
                dialog.show();
            }
        });

    }

    private void uploadImageFromCamera(View view) {
    }

    private void uploadImageFromGallery(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        galleryResultLauncher.launch(i);
    }


}