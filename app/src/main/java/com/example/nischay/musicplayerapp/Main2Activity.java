package com.example.nischay.musicplayerapp;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        tv = findViewById(R.id.music_text);


        findViewById(R.id.main_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });
    }

    public void startActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
// Pass data object in the bundle and populate details activity.
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        getWindow().setReenterTransition(new Explode());


        Transition ts = new ChangeClipBounds();         //Setting Element Animation
        ts.setDuration(5000);
        getWindow().setSharedElementExitTransition(ts);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)tv, "profile");
        startActivity(intent, options.toBundle());

//        startActivity(intent, options.toBundle());
    }
}
