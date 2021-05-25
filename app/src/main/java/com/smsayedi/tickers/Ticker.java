package com.smsayedi.tickers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * This class is for representing the discs (called tickers).
 */
public class Ticker extends ImageView {

    private char player = '1';  // Specifies which player this disc belongs to.
    private int row, col;

    public Ticker(Context context) {
        super(context);
    }

    public char getPlayer() {
        return player;
    }

    public void setPlayer(char player) {
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
