package com.sminfotech.cloudvault.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sminfotech.cloudvault.R;

import java.io.IOException;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    Activity activity;
    List<String> audioList;

    public AudioAdapter(Activity activity, List<String> audioList) {
        this.activity = activity;
        this.audioList = audioList;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(activity, Uri.parse(audioList.get(position)));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mediaPlayer.start();
                    holder.btnPlay.setVisibility(View.GONE);
                    holder.btnPause.setVisibility(View.VISIBLE);
            }
        });

        holder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                holder.btnPlay.setVisibility(View.VISIBLE);
                holder.btnPause.setVisibility(View.GONE);
            }
        });
    }

    private String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {

        ImageView btnPlay, btnPause;
        SeekBar sbAudio;
        Chronometer tvTime;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnPause = itemView.findViewById(R.id.btnPause);
            sbAudio = itemView.findViewById(R.id.sbAudio);
            tvTime = itemView.findViewById(R.id.tvTime);

        }
    }
}
