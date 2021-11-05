package com.sminfotech.cloudvault.Adapters;

import static com.unity3d.services.core.properties.ClientProperties.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sminfotech.cloudvault.Model.UserDocuments;
import com.sminfotech.cloudvault.R;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class UserDocumentsAdapter extends RecyclerView.Adapter<UserDocumentsAdapter.UserDocumentsViewHolder> {

    List<UserDocuments> userDocumentsList;
    Activity activity;

    public UserDocumentsAdapter(List<UserDocuments> userDocumentsList, Activity activity) {
        this.userDocumentsList = userDocumentsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserDocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.documents_list_item, parent, false);
        return new UserDocumentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDocumentsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDocName.setText(userDocumentsList.get(position).getName());

        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File direct = new File(Environment.getExternalStorageDirectory() + "/CloudVault");

                if (!direct.exists()) {
                    direct.mkdirs();
                }
                DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(userDocumentsList.get(position).getDocLink());

                DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(userDocumentsList.get(position).getName())
                        .setDescription("Downloading")
                        .setDestinationInExternalPublicDir("/CloudVault/Documents", userDocumentsList.get(position).getName());
                mgr.enqueue(request);

                Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDocumentsList.size();
    }

    public class UserDocumentsViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocName;
        ImageView ivDownload;
        public UserDocumentsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocName = itemView.findViewById(R.id.tvDocName);
            ivDownload = itemView.findViewById(R.id.ivDownload);
        }
    }
}
