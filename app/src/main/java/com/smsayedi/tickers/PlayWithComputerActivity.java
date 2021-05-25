package com.smsayedi.tickers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class PlayWithComputerActivity extends AppCompatActivity {

    private static final String TAG = "PlayWithComputerActivit";

    private ConstraintLayout mainLayout;

    private Cell[][] cells;
    private int boardSize = 3;
    private int tickersCount = 0;
    private int totalTickersNumber = boardSize * boardSize / 3 * 2;
    private float originX, originY;
    private int p1Count = 3, p2Count = 3;

    private char player = '1';

    private TextView tvP1Count, tvP2Count;

    long startTime;

    private TextView tvMins1, tvSecs1, tvCenti1,
            tvMins2, tvSecs2, tvCenti2;

    private TextView tvPlayer1, tvPlayer2;

    int elapsedTime1, elapsedTime2;

    String player1;
    String player1Id, player2Id;

    ArrayList<Ticker> computerTickers = new ArrayList<>();

    public void updateTimer(int time) {
        time /= 10;
        int centiSecs = time % 100;
        int secs = (time / 100) % 60;
        int mins = (time / 100) / 60;


        String minsString = (mins < 10) ? ("0" + mins) : ("" + mins);
        String secsString = (secs < 10) ? ("0" + secs) : ("" + secs);
        String centiSecsString = (centiSecs < 10) ? ("0" + centiSecs) : ("" + centiSecs);

        if (player == '1') {
            tvMins1.setText(minsString);
            tvSecs1.setText(secsString);
            tvCenti1.setText(centiSecsString);
        } else {
            tvMins2.setText(minsString);
            tvSecs2.setText(secsString);
            tvCenti2.setText(centiSecsString);
        }
    }

    public void setTimers() {
        tvMins1 = findViewById(R.id.tvMins1);
        tvSecs1 = findViewById(R.id.tvSecs1);
        tvCenti1 = findViewById(R.id.tvCenti1);

        tvMins2 = findViewById(R.id.tvMins2);
        tvSecs2 = findViewById(R.id.tvSecs2);
        tvCenti2 = findViewById(R.id.tvCenti2);

        startTime = System.currentTimeMillis();

        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                int total = (int) (System.currentTimeMillis() - startTime);
                updateTimer(total + (player == '1' ? elapsedTime1 : elapsedTime2));

                handler.postDelayed(this, 10);
            }
        };

        handler.post(run);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        boardSize = bundle.getInt(GameOptionsActivity.BOARD_SIZE);
        totalTickersNumber = boardSize * boardSize / 3 * 2;
        player1 = bundle.getString(GameOptionsActivity.NAME_PLAYER1);

        player1Id = bundle.getString(GameOptionsActivity.ID_PLAYER1);
        player2Id = bundle.getString(GameOptionsActivity.ID_PLAYER2);
        Log.d(TAG, "onCreate: player1 id is " + player1Id + "\nplayer2 id is " + player2Id);

        p1Count = totalTickersNumber / 2;
        p2Count = totalTickersNumber / 2;

        String layoutIdString = "board_" + boardSize;
        int layoutId = getResources().getIdentifier(layoutIdString, "layout", getPackageName());
        setContentView(layoutId);

        String idString = player1Id;
        int resId = getResources().getIdentifier(idString, "drawable", getPackageName());

        // Set the appropriate image
        ImageView imgIndicator = findViewById(R.id.indicator0);
        imgIndicator.setImageResource(resId);
        imgIndicator = findViewById(R.id.indicator1);
        idString = player2Id;
        resId = getResources().getIdentifier(idString, "drawable", getPackageName());
        imgIndicator.setImageResource(resId);

        tvP1Count = findViewById(R.id.tvP1Count);
        tvP2Count = findViewById(R.id.tvP2Count);
        tvP1Count.setText(String.valueOf(p1Count));
        tvP2Count.setText(String.valueOf(p2Count));
        tvPlayer1 = findViewById(R.id.tvPlayer1);
        tvPlayer2 = findViewById(R.id.tvPlayer2);

        tvPlayer1.setText(player1);
        tvPlayer2.setText("Computer");

        setTimers();

        mainLayout = findViewById(R.id.boardLayout);
        createCells();

    }

    /**
     * Creates every single cell of the board, from the xml file
     */
    public void createCells() {
        cells = new Cell[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                // Create the String of cell id, e.g., cell02, as specified in the xml file
                String idString = "cell" + i + j;

                // Create the resource id from idString
                int resId = getResources().getIdentifier(idString, "id", getPackageName());

                // Initialize the cell by using resId
                cells[i][j] = findViewById(resId);

                // Specify the row and column of this cell
                cells[i][j].setRow(i);
                cells[i][j].setCol(j);

                // Set listeners for this cell, when it is tapped
                cells[i][j].setOnClickListener(new CellOnClickListener());
            }
        }
    }

    private class CellOnClickListener implements View.OnClickListener {
        /**
         * This method is called when a cell is tapped,
         * so we create a ticker image and place it in the center of the cell.
         *
         * @param cell The cell which is tapped.
         */
        @Override
        public void onClick(View cell) {
            // Just add specified number of tickers
            if (tickersCount < totalTickersNumber) {
                putATicker((Cell) cell);


                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        doItComputer();
                        Log.d(TAG, "onFinish: computer ticker player is " + computerTickers.get(computerTickers.size() - 1).getPlayer());
                    }
                }.start();
            }
        }
    }

    // Put a ticker in this cell
    public void putATicker(Cell cell) {
        // Create a ticker, by using an ImageView class
        Ticker ticker = new Ticker(PlayWithComputerActivity.this);

        // Create id string of the drawable of this ticker, for two different players
        String idString = (player == '1') ? player1Id : player2Id;
        int resId = getResources().getIdentifier(idString, "drawable", getPackageName());

        // Set the appropriate image
        ticker.setImageResource(resId);

        int tickerWidth = (int) (cell.getWidth() * 0.7);    // Width of the ticker is 70% of the cell width.
        int tickerHeight = (int) (cell.getHeight() * 0.7);  // Height of the ticker is 70% of the cell height.
        ticker.setMinimumWidth(tickerWidth);
        ticker.setMinimumHeight(tickerHeight);
        Log.d(TAG, "onClick: Ticker width and height is " + tickerWidth + ", " + tickerHeight);

        // x and y coordinates of the the ticker image
        float x = ((Cell) cell).getCenterX() - tickerWidth / 2.0f;
        float y = ((Cell) cell).getCenterY() - tickerHeight / 2.0f;

        // Set the x and y coordinates of ticker image, to place it in the center of the cell.
        ticker.setX(x);
        ticker.setY(y);

        // Set listener for the ticker when it is touched.
        ticker.setOnTouchListener(new TickerOnTouchListener());

        // Add this ticker image to the mainLayout
        mainLayout.addView(ticker, mainLayout.getChildCount());

        // Set this cell the ticker that is created by tapping the cell.
        cell.setTicker(ticker);

        // Specify which player this ticker belongs to
        ticker.setPlayer(player);

        ticker.setRow(cell.getRow());
        ticker.setCol(cell.getCol());

        if (player == '1') {
            tvP1Count.setText(String.valueOf(--p1Count));
            if (p1Count == 0) {
                tvP1Count.setVisibility(View.GONE);
                p1Count = totalTickersNumber / 2;
            }
        } else {
            tvP2Count.setText(String.valueOf(--p2Count));
            if (p2Count == 0) {
                tvP2Count.setVisibility(View.GONE);
                p2Count = totalTickersNumber / 2;
            }
        }

        if (player == '2') {
            computerTickers.add(ticker);
        }

        changePlayer();

        tickersCount++;
    }

    public void doItComputer() {
        if (tickersCount < totalTickersNumber) {
            Cell cell = findAValidCell();

            putATicker(cell);
        }
    }

    public Cell findAValidCell() {
        int i, j;
        do {
            i = new Random().nextInt(boardSize);
            j = new Random().nextInt(boardSize);
        } while (cells[i][j].getTicker() != null);

        return cells[i][j];
    }

    private class TickerOnTouchListener implements View.OnTouchListener {
        // Variables to store the x and y coordinates of the touch, when it's first implied.
        private float xDown, yDown;

        @Override
        public boolean onTouch(View ticker, MotionEvent event) {
            /*
                Move the ticker,
                    1. after all the tickers are placed on the board
                    2. when a player moves HIS ticker, not the other player's ticker
             */
            if (tickersCount == totalTickersNumber && ((Ticker) ticker).getPlayer() == player) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xDown = event.getX();
                        yDown = event.getY();

                        originX = ticker.getX();
                        originY = ticker.getY();

                        // Set the z-dimension value for this ticker, to make it appear above other tickers,
                        // without this code the tickers that are placed after other tickers,
                        // will be shown under them when moving, which is not a good UI appearance.
                        ticker.setTranslationZ(1);

                        // Scale the ticker, so it resembles the real picking action.
                        ticker.setScaleX(1.5f);
                        ticker.setScaleY(1.5f);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Store the x and y coordinates when the ticker is moved.
                        float movedX = event.getX();
                        float movedY = event.getY();

                        // Find the difference of the first touch coordinates, and the
                        // coordinates when the ticker is moved.
                        float dx = movedX - xDown;
                        float dy = movedY - yDown;

                        // Update the ticker's location
                        ticker.setX(ticker.getX() + dx);
                        ticker.setY(ticker.getY() + dy);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Try if the ticker can be moved to the new cell.
                        tryToMove(ticker);

                        // Re-scale the ticker to it's original size.
                        ticker.setScaleX(1);
                        ticker.setScaleY(1);

                        // Set the z-dimension value back to 0,
                        // to make other tickers look above, when they're moved.
                        ticker.setTranslationZ(0);
                        break;
                }
            }
            return true;
        }
    }

    /**
     * This method see if a ticker can be moved to a new cell,
     * or should be replaced back in it's previous cell.
     *
     * @param ticker
     */
    private void tryToMove(View ticker) {
        // These two variables are used because by calling findCell(originX, originY),
        // some problems are created, that's why we find the fromCell by the center coordinates of the ticker.
        float originCenterX = originX + ticker.getWidth() / 2.0f;
        float originCenterY = originY + ticker.getHeight() / 2.0f;
        Cell fromCell = findCell(originCenterX, originCenterY); // The cell from which the ticker is dragged.
        Cell toCell = findCell(ticker); // The cell to which the ticker will be moved.

        // If there is no toCell found, means the ticker is moved and dropped outside the board,
        // so the ticker should be placed back in it's old cell.
        if (toCell == null) {
            replaceTicker(ticker);
            return;
        }

        // The remaining chunk of code is executed when we move the ticker to a cell inside the board.

        GameRules gameRules = new GameRules(cells, fromCell, toCell);

        // Check if there is not already a ticker in toCell, and that a ticker is moved one or two steps.
        if (!toCell.isOccupied() && (gameRules.isOneStepMove() || gameRules.isTwoStepMove())) {
            if (gameRules.isTwoStepMove()) {
                Cell medCell = gameRules.getMediumCell();
                // If the medium cell ticker is the opposite player's one, remove it,
                // as this is one of the game's rules.
                if (medCell.getTicker().getPlayer() != player) {
                    mainLayout.removeView(medCell.getTicker());
                    computerTickers.remove(medCell.getTicker());

                    medCell.setTicker(null);

                    checkTheWinner((Ticker) ticker);
                }
            }
            moveTicker(ticker, fromCell, toCell);
            if (p2Count != 0) {
                moveItComputer();
            }
        } else {    // Place the ticker back in it's old cell, because the ticker is not moved to a
            // valid cell based on the game's rules.
            replaceTicker(ticker);
        }

    }

    public void checkTheWinner(Ticker ticker) {
        if (player == '1') {
            p2Count--;
            if (p2Count == 0) {
                Snackbar.make(ticker, player1 + " won the game!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            p1Count--;
            if (p1Count == 0) {
                Snackbar.make(ticker, "Computer won the game!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Moves the ticker from fromCell to toCell.
     *
     * @param ticker   The ticker to be moved.
     * @param fromCell The cell from which the ticker is dragged.
     * @param toCell   The cell in which the ticker is dropped.
     */
    private void moveTicker(View ticker, Cell fromCell, Cell toCell) {
        ticker.setX(toCell.getCenterX() - ticker.getWidth() / 2.0f);
        ticker.setY(toCell.getCenterY() - ticker.getHeight() / 2.0f);

        // Remove the ticker from fromCell, by setting the ticker to null.
        fromCell.setTicker(null);
        Log.d(TAG, "moveTicker: from cell [" + fromCell.getRow() + "][" + fromCell.getCol() + "]");

        // Add the ticker to toCell
        toCell.setTicker((Ticker) ticker);
        ((Ticker) ticker).setRow(toCell.getRow());
        ((Ticker) ticker).setCol(toCell.getCol());
        Log.d(TAG, "moveTicker: to cell [" + toCell.getRow() + "][" + toCell.getCol() + "]");

        // Change player
        changePlayer();
    }

    public void moveItComputer() {
        Move move = findBestMove();
        Ticker ticker = move.getTicker();
        Cell fromCell = move.getFromCell();
        Cell toCell = move.getToCell();
        Cell medCell = move.getMediumCell();


        ObjectAnimator animator = ObjectAnimator.ofFloat(ticker, "x", toCell.getCenterX() - ticker.getWidth() / 2.0f);
        ObjectAnimator.ofFloat(ticker, "y", toCell.getCenterY() - ticker.getHeight() / 2.0f).setDuration(500).start();
        animator.setDuration(500).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ticker.setScaleX(1);
                ticker.setScaleY(1);
                // Means the computer move is a two step move, and it will eat the opposite player ticker.
                if (medCell != null) {
                    mainLayout.removeView(medCell.getTicker());
//                    computerTickers.remove(medCell.getTicker());

                    medCell.setTicker(null);

                    checkTheWinner((Ticker) ticker);
                }
                moveTicker(ticker, fromCell, toCell);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        ticker.setScaleX(1.5f);
        ticker.setScaleY(1.5f);
    }

    public Move findBestMove() {
        Ticker myTicker = null;
        Cell fromCell = null, toCell = null;
        ArrayList<Move> validMoves = new ArrayList<>();

        for (Ticker ticker : computerTickers) {
            myTicker = ticker;
            fromCell = cells[ticker.getRow()][ticker.getCol()];
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells.length; j++) {
                    toCell = cells[i][j];
                    GameRules gameRules = new GameRules(cells, fromCell, toCell);

                    // Check if there is not already a ticker in cell, and that a ticker is moved one or two steps.
                    if (!toCell.isOccupied() && (gameRules.isOneStepMove() || gameRules.isTwoStepMove())) {
                        if (gameRules.isTwoStepMove()) {
                            Cell medCell = gameRules.getMediumCell();
                            // If the medium cell ticker is the opposite player's one, remove it,
                            // as this is one of the game's rules.
                            if (medCell.getTicker().getPlayer() != player) {
                                return new Move(ticker, fromCell, toCell, medCell);
                            }
                        }
                        validMoves.add(new Move(myTicker, fromCell, toCell, null));
                    }
                }
            }
        }
        int random = new Random().nextInt(validMoves.size());
        Log.d(TAG, "findBestMove: total valid moves are " + validMoves.size() +
                "\nrandom is " + random);

        return validMoves.get(random);
    }

    /*public Object[] findComputerTicker() {
        for (int i = 0; i < computerTickers.size(); i++) {
            Ticker ticker = computerTickers.get(i);
            int row = ticker.getRow();
            int col = ticker.getCol();

            try {
                if (!cells[row - 1][col].isOccupied()) {
                    Log.d(TAG, "ticker " + i + " moved to top");
                    ticker.setRow(row - 1);
                    return new Object[]{computerTickers.get(i), cells[row][col], cells[row - 1][col]};
                }
            } catch (Exception ex) {
                Log.d(TAG, "ticker " + i + " exception: top");
            }

            try {
                if (!cells[row][col - 1].isOccupied()) {
                    Log.d(TAG, "ticker " + i + " moved to left");
                    ticker.setCol(col - 1);
                    return new Object[]{computerTickers.get(i), cells[row][col], cells[row][col - 1]};
                }
            } catch (Exception ex) {
                Log.d(TAG, "ticker " + i + " exception: left");
            }

            try {
                if (!cells[row + 1][col].isOccupied()) {
                    Log.d(TAG, "ticker " + i + " moved to bottom");
                    ticker.setRow(row + 1);
                    return new Object[]{computerTickers.get(i), cells[row][col], cells[row + 1][col]};
                }
            } catch (Exception ex) {
                Log.d(TAG, "ticker " + i + " exception: bottom");
            }

            try {
                if (!cells[row][col + 1].isOccupied()) {
                    Log.d(TAG, "ticker " + i + " moved to right");
                    ticker.setCol(col + 1);
                    return new Object[]{computerTickers.get(i), cells[row][col], cells[row][col + 1]};
                }
            } catch (Exception ex) {
                Log.d(TAG, "ticker " + i + " exception: right");
            }
        }
        return null;
    }*/

    /*public Object[] findComputerTicker() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].getTicker() != null && cells[i][j].getTicker().getPlayer() == '2') {
                    return new Object[]{cells[i][j], cells[i][j].getTicker()};
                }
            }
        }
        return null;
    }*/

    /**
     * Place the ticker back in the cell from which it was dragged.
     *
     * @param ticker The ticker which was moved, but will be re-placed in it's old cell.
     */
    private void replaceTicker(View ticker) {
        ticker.setX(originX);
        ticker.setY(originY);
    }

    private void changePlayer() {
        if (player == '1') {
            elapsedTime1 += (int) (System.currentTimeMillis() - startTime);
        } else {
            elapsedTime2 += (int) (System.currentTimeMillis() - startTime);
        }
        startTime = System.currentTimeMillis();

        player = (player == '1') ? '2' : '1';
    }

    /**
     * Find the cell inside which the dragged ticker is located.
     *
     * @param ticker The ticker from whose position, the cell is located.
     * @return The cell if it is found, or null if it is not found.
     */
    private Cell findCell(View ticker) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].contains(ticker)) {
                    return cells[i][j];
                }
            }
        }
        return null;
    }

    /**
     * Find the cell inside which x and y is located.
     *
     * @param x
     * @param y
     * @return The cell if it is found, or null if it is not found.
     */
    private Cell findCell(float x, float y) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].contains(x, y)) {
                    return cells[i][j];
                }
            }
        }
        return null;
    }

    /**
     * Set the attributes of every single cell, by calling it's setAttrs() method
     */
    private void setCellAttrs() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].setAttrs();
            }
        }
    }

    /*
        We override this method because, this callback is called whenever the layout is laid out,
        so we can call the methods on it's children, such as cell.getX().
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // If the window is visible to the user, set the attributes for all he cells
        if (hasFocus) {
            // Call this method, otherwise the the ticker will not be vertically centered in the cells.
//            hideSystemUI();
            setCellAttrs();
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
