package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {

    EditText etNoteTitle, etNoteBody;
    TextView tvCancel, tvSave;
    FirebaseFirestore firestore;
    List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteBody = findViewById(R.id.etNoteBody);
        tvCancel = findViewById(R.id.tvCancel);
        tvSave = findViewById(R.id.tvSave);
        firestore = FirebaseFirestore.getInstance();

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference ref = firestore.collection("userNotes").document();

                Map<String, Object> userNote = new HashMap<>();
                userNote.put("uid",loginuid);
                userNote.put("noteId",ref.getId());
                userNote.put("title", etNoteTitle.getText().toString());
                userNote.put("body", etNoteBody.getText().toString());
                userNote.put("createdAt", Double.valueOf(System.currentTimeMillis() / 1000.000));

                ref.set(userNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        stringList = user.getNotesList();
                        stringList.add(0,ref.getId());
                        firestore.collection("user").document(loginuid).update("notesList", stringList);
                        Snackbar.make(etNoteBody, "Saved successfully", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(AddNoteActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(etNoteBody, e.getLocalizedMessage()+"", Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}