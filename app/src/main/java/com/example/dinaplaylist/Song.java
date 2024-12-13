package com.example.dinaplaylist;

public class Song {
    private String id; // Tambahkan ID
    private String title;
    private String artist;
    private String youtubeLink;

    // Constructor dan getter/setter

    public Song() { }

    public Song(String title, String artist, String youtubeLink) {
        this.title = title;
        this.artist = artist;
        this.youtubeLink = youtubeLink;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }
}
