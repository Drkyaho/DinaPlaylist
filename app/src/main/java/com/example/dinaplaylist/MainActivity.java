package com.example.dinaplaylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etArtist, etYoutubeLink;
    private Button btnAddSong, btnViewSongs;
    private DatabaseReference databaseSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etArtist = findViewById(R.id.etArtist);
        etYoutubeLink = findViewById(R.id.etYoutubeLink);
        btnAddSong = findViewById(R.id.btnAddSong);
        btnViewSongs = findViewById(R.id.btnViewSongs);  // Perbaiki ID tombol

        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");

        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSong();
            }
        });

        btnViewSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SongListActivity.class));
            }
        });
    }

    private void addSong() {
        String title = etTitle.getText().toString().trim();
        String artist = etArtist.getText().toString().trim();
        String youtubeLink = etYoutubeLink.getText().toString().trim();


        if (!title.isEmpty() && !artist.isEmpty() && !youtubeLink.isEmpty()) {
            String id = databaseSongs.push().getKey();
            Song song = new Song(title, artist, youtubeLink);
            databaseSongs.child(id).setValue(song).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Lagu Ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Gagal Menambahkan Lagu", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show();
        }
    }

}
