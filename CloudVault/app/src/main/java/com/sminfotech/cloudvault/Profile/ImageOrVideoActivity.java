package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.unity3d.services.core.properties.ClientProperties.getActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.R;

import java.io.File;
import java.util.UUID;

public class ImageOrVideoActivity extends AppCompatActivity {

    String imageUrl;
    ImageView imageFromIntent, close;
    FloatingActionButton fabDownloadImage;
    FirebaseFirestore firestore;
    TextView deleteImage;
    String type;
    VideoView videoFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_or_video);

        imageUrl = getIntent().getStringExtra("imageUrl");
        type = getIntent().getStringExtra("type");
        imageFromIntent = findViewById(R.id.imageFromIntent);
        fabDownloadImage = findViewById(R.id.fabDownloadImage);
        close = findViewById(R.id.close);
        deleteImage = findViewById(R.id.deleteImage);
        videoFromIntent = findViewById(R.id.videoFromIntent);
        firestore = FirebaseFirestore.getInstance();

        if (type.equals("image")){
            Glide.with(ImageOrVideoActivity.this).load(imageUrl).into(imageFromIntent);
            imageFromIntent.setVisibility(View.VISIBLE);
        }else if(type.equals("video")){
            videoFromIntent.setVisibility(View.VISIBLE);
            videoFromIntent.setVideoURI(Uri.parse(imageUrl));
            videoFromIntent.requestFocus();
            videoFromIntent.start();
        }

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("image")){
                    Dialog dialog = new Dialog(ImageOrVideoActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.confirmation_popup);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    TextView confirmDelete = dialog.findViewById(R.id.confirmDelete);
                    TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
                    TextView popupHeading = dialog.findViewById(R.id.popupHeading);
                    TextView popupBody = dialog.findViewById(R.id.popupBody);
                    popupHeading.setText("Delete Image");
                    popupBody.setText("Are you sure?\nYou want to delete this image?");
                    confirmDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            firestore.collection("user").document(loginuid).update("imageList", FieldValue.arrayRemove(imageUrl))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Snackbar.make(deleteImage, "Deleted successfully...", Snackbar.LENGTH_LONG).show();
                                            dialog.dismiss();
                                            onBackPressed();
                                        }
                                    });
                        }
                    });
                    cancelDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else if (type.equals("video")){
                    Dialog dialog = new Dialog(ImageOrVideoActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.confirmation_popup);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    TextView confirmDelete = dialog.findViewById(R.id.confirmDelete);
                    TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
                    TextView popupHeading = dialog.findViewById(R.id.popupHeading);
                    TextView popupBody = dialog.findViewById(R.id.popupBody);
                    popupHeading.setText("Delete Video");
                    popupBody.setText("Are you sure?\nYou want to delete this video?");
                    confirmDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            firestore.collection("user").document(loginuid).update("videoList", FieldValue.arrayRemove(imageUrl))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Snackbar.make(deleteImage, "Deleted successfully...", Snackbar.LENGTH_LONG).show();
                                            dialog.dismiss();
                                            onBackPressed();
                                        }
                                    });
                        }
                    });
                    cancelDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fabDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage(imageUrl);
            }
        });

    }

    private void getImage(String imageUrl) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/CloudVault");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(imageUrl);

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        String random =UUID.randomUUID().toString();

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle(random)
                .setDescription("Downloading")
                .setDestinationInExternalPublicDir("/CloudVault/Media", random);
        mgr.enqueue(request);

        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_LONG).show();

    }

}