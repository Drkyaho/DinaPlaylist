package com.example.dinaplaylist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {
    private ListView lvSongs;
    private ArrayList<Song> songList;
    private ArrayAdapter<Song> adapter;
    private DatabaseReference databaseSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        lvSongs = findViewById(R.id.lvSongs);
        songList = new ArrayList<>();

        // Initialize ArrayAdapter with a custom layout for each song item
        adapter = new ArrayAdapter<Song>(this, R.layout.item_song, songList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_song, parent, false);
                }

                Song currentSong = getItem(position);
                TextView tvSongTitle = convertView.findViewById(R.id.tvSongTitle);
                TextView tvSongArtist = convertView.findViewById(R.id.tvSongArtist);
                Button btnCheckSong = convertView.findViewById(R.id.btnCheckSong);
                Button btnEditSong = convertView.findViewById(R.id.btnEditSong);
                Button btnDeleteSong = convertView.findViewById(R.id.btnDeleteSong);

                // Set song details
                if (currentSong != null) {
                    tvSongTitle.setText(currentSong.getTitle());
                    tvSongArtist.setText(currentSong.getArtist());

                    // Set button action for checking the song (open YouTube link)
                    btnCheckSong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String youtubeLink = currentSong.getYoutubeLink();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                            startActivity(browserIntent);
                        }
                    });

                    // Set button action for editing the song
                    btnEditSong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Intent to open EditSongActivity
                            Intent intent = new Intent(SongListActivity.this, EditSongActivity.class);
                            intent.putExtra("songId", currentSong.getId());  // Kirim songId
                            intent.putExtra("title", currentSong.getTitle());     // Kirim title
                            intent.putExtra("artist", currentSong.getArtist());   // Kirim artist
                            intent.putExtra("youtubeLink", currentSong.getYoutubeLink()); // Kirim youtubeLink
                            startActivity(intent);  // Mulai EditSongActivity
                        }
                    });
                }

                return convertView;
            }
        };

        lvSongs.setAdapter(adapter);

        // Firebase reference to "songs"
        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");
        databaseSongs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songList.clear();  // Clear previous data

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Song song = postSnapshot.getValue(Song.class);
                    if (song != null) {
                        songList.add(song);  // Add updated song data
                    }
                }
                adapter.notifyDataSetChanged();  // Notify adapter to update ListView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });


        // Back button functionality
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Go back to previous activity
            }
        });
    }
}
