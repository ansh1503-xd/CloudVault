package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sminfotech.cloudvault.Adapters.ImageAdapter;
import com.sminfotech.cloudvault.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UploadImageActivity extends AppCompatActivity {

    RecyclerView rvImage;
    FloatingActionButton fabUploadImage;
    ActivityResultLauncher<Intent> galleryResultLauncher;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    StorageReference storageReference;
    FirebaseStorage storage;
    ImageView back;
    FirebaseFirestore firestore;
    List<String> imageList = new ArrayList<>();
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        rvImage = findViewById(R.id.rvImage);
        fabUploadImage = findViewById(R.id.fabUploadImage);
        back = findViewById(R.id.back);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    File f = new File(currentPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    StorageReference ref = storageReference.child("Images" + "/" + UUID.randomUUID().toString());
                    Dialog dialog = new Dialog(UploadImageActivity.this);
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
                                        String imageurl = uri.toString();

                                        imageList = user.getImageList();
                                        imageList.add(0, imageurl);

                                        firestore.collection("user").document(loginuid).update("imageList", imageList)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Snackbar.make(fabUploadImage, "Upload success", Snackbar.LENGTH_LONG).show();
                                                        RecyclerView.Adapter adapter = new ImageAdapter(user.getImageList(), UploadImageActivity.this);
                                                        rvImage.setAdapter(adapter);
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
                            Snackbar.make(fabUploadImage, "Failed : " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });

                    dialog.show();
                }
            }
        });

        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri selectedImageUri = data.getData();
                            StorageReference ref = storageReference.child("Images" + "/" + UUID.randomUUID().toString());
                            Dialog dialog = new Dialog(UploadImageActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.loading_dialog);
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            TextView tvText = dialog.findViewById(R.id.tvText);
                            tvText.setText("Uploading...");
                            ref.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageurl = uri.toString();

                                                imageList = user.getImageList();
                                                imageList.add(0, imageurl);

                                                firestore.collection("user").document(loginuid).update("imageList", imageList)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                dialog.dismiss();
                                                                Snackbar.make(fabUploadImage, "Upload success", Snackbar.LENGTH_LONG).show();
                                                                RecyclerView.Adapter adapter = new ImageAdapter(user.getImageList(), UploadImageActivity.this);
                                                                rvImage.setAdapter(adapter);
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
                                    Snackbar.make(fabUploadImage, "Failed : " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

                                }
                            });
                            dialog.show();
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
                        dialog.dismiss();
                    }
                });
                takePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageFromCamera(view);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private void uploadImageFromCamera(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(UploadImageActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(UploadImageActivity.this,
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

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadImageFromGallery(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        galleryResultLauncher.launch(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView.Adapter adapter = new ImageAdapter(user.getImageList(), UploadImageActivity.this);
        rvImage.setAdapter(adapter);
    }
}