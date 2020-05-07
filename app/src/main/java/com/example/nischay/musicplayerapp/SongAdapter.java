package com.example.nischay.musicplayerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nischay on 5/7/2020.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{

    private ArrayList<SongData> _songs = new ArrayList<SongData>();
    private Context context;
    private OnItemClickListener monItemClickListener;

    public SongAdapter(ArrayList<SongData> _songs, Context context) {
        this._songs = _songs;
        this.context = context;
    }


    public interface OnItemClickListener{
        void onItemClick(Button b ,View view, SongData obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.act_main_row,parent,false);
        return new SongHolder(myView);
    }
    @Override
    public void onBindViewHolder(final SongHolder holder, final int position) {
        final SongData s = _songs.get(position);

        holder.tvSongName.setText(s.getSongname());
        holder.tvSongArtist.setText(s.getArtistname());
        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In order to access this listener from main thread
                // i am passing it the interface which is linked to on item click listener of recycler view
                // Defined as the function above by name of setOnItemClickListener

                if(monItemClickListener!=null)
                    monItemClickListener.onItemClick(holder.btnAction, v, s, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{
        TextView tvSongName,tvSongArtist;
        Button btnAction;
        public SongHolder(View myView) {
            super(myView);
            tvSongName = (TextView) itemView.findViewById(R.id.song_name);
            tvSongArtist = (TextView) itemView.findViewById(R.id.artist_name);
            btnAction = (Button) itemView.findViewById(R.id.btn_play);
        }
    }
}
