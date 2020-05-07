package com.example.nischay.musicplayerapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "";
    RecyclerView recyclerView;
    private ArrayList<SongData> _songs = new ArrayList<SongData>();;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();

    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        songAdapter = new SongAdapter(_songs,this);
        recyclerView.setAdapter(songAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),layout.getOrientation()));

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button b, View view, final SongData obj, int position) {
                if(b.getText().toString().equals("Stop")){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    b.setText("Play");
                }else{
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(obj.getSongUrl());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        seekBar.setProgress(0);
                                        seekBar.setMax(mediaPlayer.getDuration());
                                        Log.d("Prog", "run: " + mediaPlayer.getDuration());
                                    }
                                });
                                b.setText("Stop");
                            }catch (Exception e){}
                        }

                    };
                    myHandler.postDelayed(runnable,100);
                }
            }
        });

        checkUserpermission();
        Thread t = new runThread();
        t.start();
    }

    private class runThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    });
                }
            }
        }
    }



    private void checkUserpermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }
        loadSongs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserpermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadSongs() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){

            Log.e("TAG:: -------------->  ","INSIDE");

            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            Cursor cur = getApplicationContext().getContentResolver().query(uri,null,
                    MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

            Log.e("TAG", "Query finished. " + (cur == null ? "Returned NULL." : "Returned a cursor."));

            if (cur == null) {
                // Query failed...
                Log.e("TAG", "Failed to retrieve music: cursor is null :-(");
                return;
            }
            if (!cur.moveToFirst()) {
                // Nothing to query. There is no music on the device. How boring.
                Log.e("TAG", "Failed to move cursor to first row (no query results).");
                return;
            }

            do{
                Log.e("this", "loadSongs: " );
                String _name = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String _artist = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String _uri = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));

                _songs.add(new SongData(_name,_artist,_uri));
                songAdapter.notifyItemChanged(_songs.size());
            }while (cur.moveToNext());
            cur.close();


        }else{
            Log.e("TAG:: -------------->  ","BHAAK");
        }


    }

}
