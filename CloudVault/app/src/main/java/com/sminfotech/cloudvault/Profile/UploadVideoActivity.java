package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sminfotech.cloudvault.Adapters.ImageAdapter;
import com.sminfotech.cloudvault.Adapters.VideoAdapter;
import com.sminfotech.cloudvault.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UploadVideoActivity extends AppCompatActivity {

    RecyclerView rvVideo;
    ImageView back;
    FloatingActionButton fabUploadVideo;
    ActivityResultLauncher<Intent> galleryResultLauncher;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    String currentVideoPath;
    List<String> videoList;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    ActivityResultLauncher<String> externalStoragePermission;
    ActivityResultLauncher<String> cameraPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        rvVideo = findViewById(R.id.rvVideo);
        fabUploadVideo = findViewById(R.id.fabUploadVideo);
        back = findViewById(R.id.back);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firestore = FirebaseFirestore.getInstance();

        videoList = user.getVideoList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        externalStoragePermission = registerForActivityResult(
                                    new ActivityResultContracts.RequestPermission(),
                                    new ActivityResultCallback<Boolean>() {
                                        @Override
                                        public void onActivityResult(Boolean result) {
                                            if (result) {
                                                Log.e("TAG", "onActivityResult: PERMISSION GRANTED");
                                            } else {
                                            }
                                        }
                                    });

        cameraPermission = registerForActivityResult(
                                    new ActivityResultContracts.RequestPermission(),
                                    new ActivityResultCallback<Boolean>() {
                                        @Override
                                        public void onActivityResult(Boolean result) {
                                            if (result) {
                                                Log.e("TAG", "onActivityResult: PERMISSION GRANTED");
                                            } else {
                                            }
                                        }
                                    });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    File f = new File(currentVideoPath);
                    Uri contentUri = Uri.fromFile(f);
                    StorageReference ref = storageReference.child("Videos" + "/" + UUID.randomUUID().toString());
                    Dialog dialog = new Dialog(UploadVideoActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.loading_dialog);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    TextView tvText = dialog.findViewById(R.id.tvText);
                    tvText.setText("Uploading...");
                    ref.putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String videourl = uri.toString();

                                        videoList.add(0,videourl);
                                        firestore.collection("user").document(loginuid).update("videoList",videoList)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Snackbar.make(fabUploadVideo, "Upload success", Snackbar.LENGTH_LONG).show();
                                                        RecyclerView.Adapter adapter = new VideoAdapter(user.getVideoList(), UploadVideoActivity.this);
                                                        rvVideo.setAdapter(adapter);
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Snackbar.make(fabUploadVideo, "Failed : " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });

                    dialog.show();
                }
            }
        });

        galleryResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Uri selectedVideoUri = data.getData();
                            StorageReference ref = storageReference.child("Videos" + "/" + UUID.randomUUID().toString());
                            Dialog dialog = new Dialog(UploadVideoActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.loading_dialog);
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            TextView tvText = dialog.findViewById(R.id.tvText);
                            tvText.setText("Uploading...");
                            ref.putFile(selectedVideoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                   ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           String videourl = uri.toString();
                                           videoList.add(0,videourl);
                                           firestore.collection("user").document(loginuid).update("videoList", videoList)
                                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           dialog.dismiss();
                                                           Snackbar.make(rvVideo,"Upload successfully", Snackbar.LENGTH_LONG).show();
                                                           RecyclerView.Adapter adapter = new VideoAdapter(user.getVideoList(), UploadVideoActivity.this);
                                                           rvVideo.setAdapter(adapter);
                                                       }
                                                   });
                                       }
                                   });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Snackbar.make(rvVideo, "Failed : " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                            dialog.show();
                        }
                    }
                });

        fabUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(UploadVideoActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.select_media_popup);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LinearLayout gallerySelect = dialog.findViewById(R.id.gallerySelect);
                LinearLayout takePicture = dialog.findViewById(R.id.takePicture);
                TextView tvPicture = dialog.findViewById(R.id.tvPicture);
                tvPicture.setText("Take a Video");
                gallerySelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(UploadVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            uploadVideoFromGallery(view);
                        } else {
                            externalStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        dialog.dismiss();
                    }
                });
                takePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(UploadVideoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            uploadVideoFromCamera(view);
                        } else {
                            cameraPermission.launch(Manifest.permission.CAMERA);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.Adapter adapter = new VideoAdapter(user.getVideoList(), UploadVideoActivity.this);
        rvVideo.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(UploadVideoActivity.this, 3);
        rvVideo.setLayoutManager(manager);

    }

    private void uploadVideoFromCamera(View view) {
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (i.resolveActivity(UploadVideoActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(UploadVideoActivity.this,
                        "com.example.android.fileprovider",
                        photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraResultLauncher.launch(i);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentVideoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadVideoFromGallery(View view) {
        Intent i = new Intent();
        i.setType("video/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        i.setAction(Intent.ACTION_GET_CONTENT);
        galleryResultLauncher.launch(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView.Adapter adapter = new VideoAdapter(user.getVideoList(), UploadVideoActivity.this);
        rvVideo.setAdapter(adapter);
    }
}