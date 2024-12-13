package com.example.dinaplaylist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSongActivity extends AppCompatActivity {
    private EditText etTitle, etArtist, etYoutubeLink;
    private Button btnUpdateSong;
    private String songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_song);

        etTitle = findViewById(R.id.etTitle);
        etArtist = findViewById(R.id.etArtist);
        etYoutubeLink = findViewById(R.id.etYoutubeLink);
        btnUpdateSong = findViewById(R.id.btnUpdateSong);

        // Ambil data dari intent
        songId = getIntent().getStringExtra("songId");
        String title = getIntent().getStringExtra("title");
        String artist = getIntent().getStringExtra("artist");
        String youtubeLink = getIntent().getStringExtra("youtubeLink");

        // Set data ke EditText
        etTitle.setText(title);
        etArtist.setText(artist);
        etYoutubeLink.setText(youtubeLink);

        // Update data di Firebase
        btnUpdateSong.setOnClickListener(v -> {
            String newTitle = etTitle.getText().toString().trim();
            String newArtist = etArtist.getText().toString().trim();
            String newYoutubeLink = etYoutubeLink.getText().toString().trim();

            if (!newTitle.isEmpty() && !newArtist.isEmpty() && !newYoutubeLink.isEmpty()) {
                // Ambil referensi database untuk lagu yang ingin diperbarui
                DatabaseReference databaseSongs = FirebaseDatabase.getInstance().getReference("songs");

                // Memperbarui nilai-nilai yang ada di Firebase untuk lagu tersebut
                databaseSongs.child(songId).child("title").setValue(newTitle);
                databaseSongs.child(songId).child("artist").setValue(newArtist);
                databaseSongs.child(songId).child("youtubeLink").setValue(newYoutubeLink)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditSongActivity.this, "Song updated successfully", Toast.LENGTH_SHORT).show();
                                Log.d("EditSongActivity", "Update successful");

                                // Setelah pembaruan selesai, kembali ke SongListActivity dengan intent
                                Intent intent = new Intent(EditSongActivity.this, SongListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the stack to prevent going back to this activity
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditSongActivity.this, "Failed to update song", Toast.LENGTH_SHORT).show();
                                Log.e("EditSongActivity", "Update failed: " + task.getException());
                            }
                        });
            } else {
                Toast.makeText(EditSongActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
