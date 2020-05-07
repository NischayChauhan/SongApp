package com.example.nischay.musicplayerapp;

/**
 * Created by Nischay on 5/7/2020.
 */

public class SongData {
    private String Songname;
    private String Artistname;
    private String SongUrl;

    public SongData(String songname, String artistname, String songUrl) {
        Songname = songname;
        Artistname = artistname;
        SongUrl = songUrl;
    }

    public String getSongname() {
        return Songname;
    }

    public void setSongname(String songname) {
        Songname = songname;
    }

    public String getArtistname() {
        return Artistname;
    }

    public void setArtistname(String artistname) {
        Artistname = artistname;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }
}
