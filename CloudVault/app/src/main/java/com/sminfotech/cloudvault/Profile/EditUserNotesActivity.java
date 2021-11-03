package com.sminfotech.cloudvault.Profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sminfotech.cloudvault.R;

import java.util.HashMap;
import java.util.Map;

public class EditUserNotesActivity extends AppCompatActivity {

    TextView tvCancelEdit, tvEdit, tvSave, tvCancel;
    TextView tvNoteTitle, tvNoteBody;
    LinearLayout llEdit, llView;
    FirebaseFirestore firestore;
    String noteId;
    EditText etNoteBody, etNoteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_notes);

        tvCancelEdit = findViewById(R.id.tvCancelEdit);
        tvEdit = findViewById(R.id.tvEdit);
        llEdit = findViewById(R.id.llEdit);
        llView = findViewById(R.id.llView);
        tvSave = findViewById(R.id.tvSave);
        tvCancel = findViewById(R.id.tvCancel);
        tvNoteTitle = findViewById(R.id.tvNoteTitle);
        tvNoteBody = findViewById(R.id.tvNoteBody);
        etNoteBody = findViewById(R.id.etNoteBody);
        etNoteTitle = findViewById(R.id.etNoteTitle);
        noteId = getIntent().getStringExtra("noteId");
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("userNotes").document(noteId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    String title = (String) value.get("title");
                    String body = (String) value.get("body");
                    tvNoteTitle.setText(title);
                    tvNoteBody.setText(body);
                    etNoteBody.setText(body);
                    etNoteTitle.setText(title);
                }
            }
        });

        tvCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvNoteBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llEdit.setVisibility(View.VISIBLE);
                llView.setVisibility(View.GONE);
            }
        });

        tvNoteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llEdit.setVisibility(View.VISIBLE);
                llView.setVisibility(View.GONE);
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("userNotes").document(noteId).delete();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llEdit.setVisibility(View.GONE);
                llView.setVisibility(View.VISIBLE);
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> updateNote = new HashMap<>();
                updateNote.put("title", etNoteTitle.getText().toString());
                updateNote.put("body", etNoteBody.getText().toString());
                updateNote.put("createdAt",Double.valueOf(System.currentTimeMillis() / 1000.000));

                firestore.collection("userNotes").document(noteId).update(updateNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(tvSave,"Updated successfully",Snackbar.LENGTH_LONG).show();
                        llEdit.setVisibility(View.GONE);
                        llView.setVisibility(View.VISIBLE);
                    }
                });

            }
        });


    }
}