package com.smsayedi.tickers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * This class is for representing every single cell of the game board.
 */
public class Cell extends View {
    private static final String TAG = "Cell";

    private Ticker ticker;  // ticker which this cell will have

    private int row;    // row in which this cell is located
    private int col;    // column in which this cell is located
    private float x1;   // start x coordinate of this cell
    private float x2;   // end x coordinate of this cell
    private float y1;   // start y coordinate of this cell
    private float y2;   // end y coordinate of this cell
    private float centerX;  // center x coordinate of this cell
    private float centerY;  // center y coordinate of this cell

    // This constructor is needed, otherwise the program will crash (layout will not inflate the Cell).
    public Cell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *  Set the attributes for this cell, such as centerX, centerY,...
     *  We can't do this in constructor because the of the layout rendering problems.
     */
    public void setAttrs() {
        x1 = getX();
        x2 = x1 + getWidth();
        y1 = getY();
        y2 = y1 + getHeight();
        centerX = (x1 + x2) / 2;
        centerY = (y1 + y2) / 2;

        Log.d(TAG, "setAttrs: cell x1, y1 is " + x1 + ", " + y1);
        /*Log.d(TAG, "setAttrs: x,y: " + centerX + ", " + centerY);
        Log.d(TAG, "setAttrs: x,y: " + (x1 + getWidth() / 2.0) + ", " + (y1 + getHeight() / 2.0));*/
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

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public boolean contains(View view) {
        float viewCenterX = view.getX() + view.getWidth() / 2.0f;
        float viewCenterY = view.getY() + view.getHeight() / 2.0f;

        return contains(viewCenterX, viewCenterY);
    }

    public boolean contains(float x, float y) {
        return (x >= x1 && x <= x2) && (y >= y1 && y <= y2);
    }

    /**
     * Checks if this cell has a ticker in it.
     */
    public boolean isOccupied() {
        return ticker != null;
    }
}
