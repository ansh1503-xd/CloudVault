package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.userNotesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sminfotech.cloudvault.Adapters.UserNotesAdapter;
import com.sminfotech.cloudvault.R;

public class NotesActivity extends AppCompatActivity {

    RecyclerView rvNotes;
    ImageView back;
    FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        rvNotes = findViewById(R.id.rvNotes);
        back = findViewById(R.id.back);
        fabAddNote = findViewById(R.id.fabAddNote);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NotesActivity.this, AddNoteActivity.class);
                startActivity(i);
            }
        });

        RecyclerView.Adapter adapter = new UserNotesAdapter(userNotesList, NotesActivity.this);
        rvNotes.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(NotesActivity.this,LinearLayoutManager.VERTICAL,false);
        rvNotes.setLayoutManager(manager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView.Adapter adapter = new UserNotesAdapter(userNotesList, NotesActivity.this);
        rvNotes.setAdapter(adapter);
    }
}