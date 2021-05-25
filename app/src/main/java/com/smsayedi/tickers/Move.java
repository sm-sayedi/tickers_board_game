package com.smsayedi.tickers;

/**
 * This class is used for a single move of ticker from
 * one cell to another cell.
 */
public class Move {
    private Ticker ticker;
    private Cell fromCell, toCell, mediumCell;

    public Move(Ticker ticker, Cell fromCell, Cell toCell, Cell mediumCell) {
        this.ticker = ticker;
        this.fromCell = fromCell;
        this.toCell = toCell;
        this.mediumCell = mediumCell;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Cell getFromCell() {
        return fromCell;
    }

    public Cell getToCell() {
        return toCell;
    }

    public Cell getMediumCell() {
        return mediumCell;
    }
}
