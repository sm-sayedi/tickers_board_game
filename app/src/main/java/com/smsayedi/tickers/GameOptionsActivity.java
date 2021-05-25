package com.smsayedi.tickers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;

public class GameOptionsActivity extends AppCompatActivity {

    public static final String BOARD_SIZE = "BoardSize";
    public static final String NAME_PLAYER1 = "Player1Name";
    public static final String NAME_PLAYER2 = "Player2Name";
    public static final String ID_PLAYER1 = "Player1Id";
    public static final String ID_PLAYER2 = "Player2Id";

    private EditText edPlayer1Name, edPlayer2Name;
    private Button btnStart;
    private ImageView imgClose;
    private ChipGroup chipGroup;
    private RadioGroup radioGroup1, radioGroup2;
    private int boardSize = 3;

    private String player1Id, player2Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        MainActivity.GameType gameType = (MainActivity.GameType) bundle.getSerializable(MainActivity.GAME_TYPE);

        if (gameType.equals(MainActivity.GameType.WITH_FRIEND)) {
            setContentView(R.layout.activity_friend_game_options);
        } else if (gameType.equals(MainActivity.GameType.WITH_COMPUTER)) {
            setContentView(R.layout.activity_computer_game_options);
        }

        edPlayer1Name = findViewById(R.id.edPlayer1Name);
        edPlayer2Name = findViewById(R.id.edPlayer2Name);
        btnStart = findViewById(R.id.btnStart);
        chipGroup = findViewById(R.id.chipGroup);

        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBoardSize();
                setTickerColor();

                Intent intent = null;
                if (gameType.equals(MainActivity.GameType.WITH_FRIEND)) {
                    intent = new Intent(GameOptionsActivity.this, PlayWithFriendActivity.class);
                    intent.putExtra(NAME_PLAYER2, edPlayer2Name.getText().toString());
                } else if (gameType.equals(MainActivity.GameType.WITH_COMPUTER)) {
                    intent = new Intent(GameOptionsActivity.this, PlayWithComputerActivity.class);
                }
                intent.putExtra(NAME_PLAYER1, edPlayer1Name.getText().toString());
                intent.putExtra(BOARD_SIZE, boardSize);
                intent.putExtra(ID_PLAYER1, player1Id);
                intent.putExtra(ID_PLAYER2, player2Id);

                startActivity(intent);
                finish();
            }
        });

        imgClose = findViewById(R.id.imgBack);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setBoardSize() {
        switch (chipGroup.getCheckedChipId()) {
            case R.id.chip1:
                boardSize = 3;
                break;

            case R.id.chip2:
                boardSize = 4;
                break;

            case R.id.chip3:
                boardSize = 5;
                break;
        }
    }

    private void setTickerColor() {
        switch (radioGroup1.getCheckedRadioButtonId()) {
            case R.id.rb11:
                player1Id = "ticker" + 0;
                break;

            case R.id.rb12:
                player1Id = "ticker" + 1;
                break;

            case R.id.rb13:
                player1Id = "ticker" + 2;
                break;

            case R.id.rb14:
                player1Id = "ticker" + 3;
                break;

            case R.id.rb15:
                player1Id = "ticker" + 4;
                break;
        }

        switch (radioGroup2.getCheckedRadioButtonId()) {
            case R.id.rb21:
                player2Id = "ticker" + 0;
                break;

            case R.id.rb22:
                player2Id = "ticker" + 1;
                break;

            case R.id.rb23:
                player2Id = "ticker" + 2;
                break;

            case R.id.rb24:
                player2Id = "ticker" + 3;
                break;

            case R.id.rb25:
                player2Id = "ticker" + 4;
                break;
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
