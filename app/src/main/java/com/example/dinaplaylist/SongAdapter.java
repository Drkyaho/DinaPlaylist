package com.example.dinaplaylist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Song> songs;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        }

        Song currentSong = songs.get(position);

        // Referensi elemen
        TextView tvSongTitle = convertView.findViewById(R.id.tvSongTitle);
        TextView tvSongArtist = convertView.findViewById(R.id.tvSongArtist);
        Button btnCheckSong = convertView.findViewById(R.id.btnCheckSong);
        Button btnEditSong = convertView.findViewById(R.id.btnEditSong);
        Button btnDeleteSong = convertView.findViewById(R.id.btnDeleteSong); // Tambahkan button delete

        // Set data
        tvSongTitle.setText(currentSong.getTitle());
        tvSongArtist.setText(currentSong.getArtist());

        // Listener tombol Check
        btnCheckSong.setOnClickListener(v -> {
            String youtubeLink = currentSong.getYoutubeLink();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
            context.startActivity(browserIntent);
        });

        // Listener tombol Edit
        btnEditSong.setOnClickListener(v -> {
            if (currentSong.getId() != null) {
                Intent editIntent = new Intent(context, EditSongActivity.class);
                editIntent.putExtra("songId", currentSong.getId());
                editIntent.putExtra("title", currentSong.getTitle());
                editIntent.putExtra("artist", currentSong.getArtist());
                editIntent.putExtra("youtubeLink", currentSong.getYoutubeLink());
                context.startActivity(editIntent);
                Log.d("SongAdapter", "Song ID: " + currentSong.getId());
            } else {
                Toast.makeText(context, "Song ID not found", Toast.LENGTH_SHORT).show();
            }
        });


        // Listener tombol Delete
        btnDeleteSong.setOnClickListener(v -> {
            DatabaseReference databaseSongs = FirebaseDatabase.getInstance().getReference("songs");
            databaseSongs.child(currentSong.getId()).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Song deleted successfully", Toast.LENGTH_SHORT).show();
                    songs.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to delete song", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return convertView;
    }



}
