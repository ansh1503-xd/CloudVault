package com.sminfotech.cloudvault.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sminfotech.cloudvault.Model.UserNotes;
import com.sminfotech.cloudvault.Profile.EditUserNotesActivity;
import com.sminfotech.cloudvault.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserNotesAdapter extends RecyclerView.Adapter<UserNotesAdapter.UserNotesViewHolder> {

    List<UserNotes> userNotesList;
    Activity activity;

    public UserNotesAdapter(List<UserNotes> userNotesList, Activity activity) {
        this.userNotesList = userNotesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_notes_list_item, parent, false);
        return new UserNotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNotesViewHolder holder, int position) {
        holder.tvTitle.setText(userNotesList.get(position).getTitle());
        holder.tvBody.setText(userNotesList.get(position).getBody());
        Date date = new Date((long) (userNotesList.get(position).getCreatedAt() * 1000L)); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm aa");
        String date1 = sdf.format(date);
        holder.tvLastEdited.setText("Last edited : "+ date1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, EditUserNotesActivity.class);
                i.putExtra("noteId", userNotesList.get(position).getNoteId());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userNotesList.size();
    }

    public class UserNotesViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvBody;
        TextView tvLastEdited;

        public UserNotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvLastEdited = itemView.findViewById(R.id.tvLastEdited);
        }
    }
}
