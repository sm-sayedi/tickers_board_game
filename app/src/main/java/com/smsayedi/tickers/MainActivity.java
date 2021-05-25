package com.smsayedi.tickers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private CardView cdPlayWithFriend,
            cdPlayWithComputer,
            cdPlayOnline,
            cdGameRules,
            cdPreferences;

    enum GameType {WITH_FRIEND, WITH_COMPUTER, ONLINE}

    public static final String GAME_TYPE = "GameType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        /*MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();*/

        cdPlayWithFriend = findViewById(R.id.cdFriend);
        cdPlayWithComputer = findViewById(R.id.cdComputer);
        cdPlayOnline = findViewById(R.id.cdOnline);
        cdGameRules = findViewById(R.id.cdRules);
        cdPreferences = findViewById(R.id.cdPrefereneces);

        cdPlayWithFriend.setOnClickListener(new OptionClickListener());
        cdPlayWithComputer.setOnClickListener(new OptionClickListener());
        cdPlayOnline.setOnClickListener(new OptionClickListener());
        cdGameRules.setOnClickListener(new OptionClickListener());
        cdPreferences.setOnClickListener(new OptionClickListener());

    }

    private class OptionClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cdFriend:
                    Intent intent = new Intent(MainActivity.this, GameOptionsActivity.class);
                    intent.putExtra(GAME_TYPE, GameType.WITH_FRIEND);
                    startActivity(intent);
                    break;

                case R.id.cdComputer:
                    intent = new Intent(MainActivity.this, GameOptionsActivity.class);
                    intent.putExtra(GAME_TYPE, GameType.WITH_COMPUTER);
                    startActivity(intent);
                    break;

                case R.id.cdOnline:
                    Snackbar.make(v,"Under Development", Snackbar.LENGTH_SHORT).show();
                    break;

                case R.id.cdRules:
                    startActivity(new Intent(getApplicationContext(), GameRulesActivity.class));
                    break;

                case R.id.cdPrefereneces:
                    startActivity(new Intent(getApplicationContext(), PreferenceActivity.class));
                    break;
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
