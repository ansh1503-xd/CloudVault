package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;
import static com.sminfotech.cloudvault.MainActivity.userDocumentsList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sminfotech.cloudvault.Adapters.ImageAdapter;
import com.sminfotech.cloudvault.Adapters.UserDocumentsAdapter;
import com.sminfotech.cloudvault.Model.UserDocuments;
import com.sminfotech.cloudvault.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UploadDocumentsActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView rvDocs;
    FloatingActionButton fabUploadDocs;
    ActivityResultLauncher<String> externalStoragePermission;
    ActivityResultLauncher<Intent> documentResultLauncher;
    String[] mimeTypes =
            {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                    "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                    "text/plain",
                    "application/pdf",
                    "application/zip"};

    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageReference;
//    List<UserDocuments> userDocumentsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);

        back = findViewById(R.id.back);
        rvDocs = findViewById(R.id.rvDocs);
        fabUploadDocs = findViewById(R.id.fabUploadDocument);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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

        documentResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @SuppressLint("Range")
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data.getClipData()!=null&&data.getClipData().getItemCount()>1) {
                        ClipData selectedImageUri = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<>();

                        for (int i = 0; i < selectedImageUri.getItemCount(); i++) {
                            ClipData.Item item = selectedImageUri.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            uploadDocToFirebase(uri);
                        }
                    }else{
                        uploadDocToFirebase(data.getData());
                    }
                }
            }
        });

        fabUploadDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadDocumentsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    uploadDocsFromFile(view);
                } else {
                    externalStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView.Adapter adapter = new UserDocumentsAdapter(userDocumentsList, UploadDocumentsActivity.this);
        rvDocs.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(UploadDocumentsActivity.this, 2  );
        rvDocs.setLayoutManager(manager);
    }

    @SuppressLint("Range")
    private void uploadDocToFirebase(Uri uri) {
        File myFile = new File(uri.toString());
        String displayName = null;
        Cursor cursor = null;
        if (uri.toString().startsWith("content://")) {

            try {
                cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = myFile.getName();
        }
        StorageReference ref = storageReference.child("Documents" + "/" + UUID.randomUUID().toString());
        Dialog dialog = new Dialog(UploadDocumentsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvText = dialog.findViewById(R.id.tvText);
        tvText.setText("Uploading...");
        String finalDisplayName = displayName;
        ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String docUrl = uri.toString();
                            DocumentReference reference = firestore.collection("userDocs").document();

                            Map<String, Object> docMap = new HashMap<>();
                            docMap.put("uid", loginuid);
                            docMap.put("docId", reference.getId());
                            docMap.put("docLink", docUrl);
                            docMap.put("name", finalDisplayName);

                            reference.set(docMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Snackbar.make(fabUploadDocs, "Upload success", Snackbar.LENGTH_LONG).show();
                                    RecyclerView.Adapter adapter = new UserDocumentsAdapter(userDocumentsList, UploadDocumentsActivity.this);
                                    rvDocs.setAdapter(adapter);
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
                Snackbar.make(fabUploadDocs, "Failed : " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
        dialog.show();
    }

    private void uploadDocsFromFile(View view) {
        Intent i = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            i.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            i.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        documentResultLauncher.launch(i);
    }
}