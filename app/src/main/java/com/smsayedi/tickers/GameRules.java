package com.smsayedi.tickers;

public class GameRules {
    private Cell[][] cells;
    private Cell fromCell, medCell, toCell;

    private int i1, i2;   // Row indices of the fromCell and toCell
    private int j1, j2;   // Column indices of the fromCell and toCell

    public GameRules(Cell[][] boardCells, Cell fromCell, Cell toCell) {
        this.cells = boardCells;
        this.fromCell = fromCell;
        this.toCell = toCell;

        i1 = fromCell.getRow();
        i2 = toCell.getRow();
        j1 = fromCell.getCol();
        j2 = toCell.getCol();
    }

    public boolean isOneStepMove() {
        // Check if the ticker is moved just one step vertically
        boolean isOneStepVerticalMove = Math.abs(i1 - i2) == 1 && Math.abs(j1 - j2) == 0;
        // Check if the ticker is moved just one step horizontally
        boolean isOneStepHorizontalMove = Math.abs(j1 - j2) == 1 && Math.abs(i1 - i2) == 0;
        // Check if the ticker is moved just one step vertically or horizontally, as this is one of the game's rule.
        return isOneStepVerticalMove || isOneStepHorizontalMove;
    }

    public boolean isTwoStepMove() {
        int medI = (i1 + i2) / 2;   // Variable to store the row index of the medium cell between fromCell and toCell.
        // Check if the ticker is moved two steps vertically, and also there is another ticker between fromCell and toCell.
        boolean isTwoStepVerticalMove = Math.abs(i1 - i2) == 2 && Math.abs(j1 - j2) == 0 && cells[medI][j1].isOccupied();
        if (isTwoStepVerticalMove) {
            medCell = cells[medI][j1];
        }

        int medJ = (j1 + j2) / 2;   // Variable to store the column index of the medium cell between fromCell and toCell.
        // Check if the ticker is moved two steps horizontally, and also there is another ticker between fromCell and toCell.
        boolean isTwoStepHorizontalMove = Math.abs(j1 - j2) == 2 && Math.abs(i1 - i2) == 0 && cells[i1][medJ].isOccupied();
        if (isTwoStepHorizontalMove) {
            medCell = cells[i1][medJ];
        }

        // Check if the ticker is moved two steps horizontally or vertically, and that there is another ticker
        // between fromCell and toCell, as this is one of the game's rule.
        return isTwoStepVerticalMove || isTwoStepHorizontalMove;
    }

    public Cell getMediumCell() {
        return medCell;
    }

}
