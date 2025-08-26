package assignment;

import java.awt.*;
import java.sql.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

import assignment.Piece.PieceType;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
    // Variables
    int boardWidth;
    int maxHeight;
    int boardHeight;
    TetrisPiece piece;
    int rowsCleared;
    Action lastAction;
    Result lastResult;
    int[] columnHeight;
    int[] rowWidth;
    Point wallKick;
    int bodyX;
    int bodyY;
    PieceType[][] grid;
    

    // JTetris will use this constructor.
    public TetrisBoard(int width, int height) {
        boardHeight = height;
        boardWidth = width;
        rowsCleared = 0;
        grid = new PieceType[boardHeight][boardWidth];
        lastAction = null;
        lastResult = null;
        columnHeight = getAllColumnHeights();
        rowWidth = getAllRowWidths();
    }

    //This constructor is used for testMove.
    public TetrisBoard(int width, int height, int mh, TetrisPiece p, int rc, Action action, Result result, int[]cl, int rw[], int bX, int bY, PieceType[][] theGrid) {
        this.boardWidth = width;
        this.boardHeight = height;
        this.maxHeight = mh;
        this.bodyX = bX;
        this.bodyY = bY;

        this.piece = new TetrisPiece(p.getType(), p.getRotationIndex());
        piece.clockWise = (TetrisPiece) p.clockwisePiece();
        piece.counterClockWise = (TetrisPiece) p.counterclockwisePiece();


        this.rowsCleared = rc;
        this.lastAction = action;
        this.lastResult = result;


        this.columnHeight = new int[cl.length];
        for (int i =0;i<columnHeight.length;i++){
            columnHeight[i] = cl[i];
        }

        this.rowWidth = new int[rw.length];
        for (int i =0;i<rowWidth.length;i++){
            rowWidth[i] = rw[i];
        }

        this.grid = new PieceType[theGrid.length][theGrid[0].length];
        for (int i =0; i<grid.length;i++){
            for (int j=0;j<grid[0].length;j++){
                grid[i][j] = theGrid[i][j];
            }
        }
    }

    //This method checks to make sure that the given piece at a given potential position does not collide with anything.
    public boolean checkBounds(Piece p, int bodyX, int bodyY) {
        for (int i = 0; i < p.getBody().length; i++) {
            if (bodyX + p.getBody()[i].x < 0 || bodyY + p.getBody()[i].y < 0) {
                return false;
            } else if (bodyX + p.getBody()[i].x >= boardWidth || bodyY + p.getBody()[i].y >= boardHeight) {
                return false;
            } else if (grid[bodyY + p.getBody()[i].y][bodyX + p.getBody()[i].x] != null) {
                return false;
            }
        }
        return true;
    }
    //This method checks to make sure that the given piece at a given potential position does not collide with anything after trying the appropriate wall kicks.
    public boolean checkCollide(Piece p, int bodyX, int bodyY, Point [] wall){
        boolean move = true;
        boolean curmove;

            for (int k=0; k<wall.length; k++)
            {
                    curmove = true;
                    for(int i=0; i<p.getBody().length; i++)
                    {
                        if(bodyX+wall[k].x+p.getBody()[i].x<0 || bodyY+wall[k].y+p.getBody()[i].y<0 ){
                            move = false;
                            curmove = false;
                        }
                        else if(bodyX+wall[k].x+p.getBody()[i].x>=boardWidth || bodyY+wall[k].y+p.getBody()[i].y>=boardHeight ){
                            move = false;
                            curmove = false;
                        }
                        else if (grid[bodyY + wall[k].y + p.getBody()[i].y][bodyX + wall[k].x + p.getBody()[i].x] != null) {
                            move = false;
                            curmove = false;
                        }
                    }
                    if(curmove)
                    {
                        wallKick = wall[k];
                        return true;
                    }
            }
        return move;
    }

    //Returns max height of all columns.
    public int[] getAllColumnHeights(){
        int[] colHeights = new int[boardWidth];
        for (int j =0;j<boardWidth;j++){
            int height = 0;
            for (int i =boardHeight-1;i>=0;i--){
                if (grid[i][j]!=null){
                    height=i+1;
                    break;
                }
            }
            colHeights[j]=height;
        }
        return colHeights;
    }

    //Returns the maxHeight of all columns.
    public int setMaxHeight(){
        int height = 0;
        for (int i=0; i<rowWidth.length; i++){
            if (rowWidth[i]>0)
            {
                height = i+1;
            }
        }
        return height;
    }

    //Returns number of blocks for each row
    public int[] getAllRowWidths(){
        int[] rowWidths = new int[boardHeight];
        for (int i =0;i<boardHeight;i++){
            int height = 0;
            for (int j =boardWidth-1;j>=0;j--){
                if (grid[i][j]!=null){
                    height++;
                }
            }
            rowWidths[i]=height;
        }
        return rowWidths;
    }

    //After a piece is placed, this method updates the grid to include the piece types at the appropriate positions.
     public PieceType[][] updateGrid(Piece p, int bodyX, int bodyY) {
        PieceType[][] newGrid = grid;
         for (int a = 0; a < 4; a++)
         {
             newGrid[bodyY+p.getBody()[a].y][bodyX+p.getBody()[a].x] = p.getType();
         }
         return newGrid;
     }

     //This method clears the rows if they are filled.
     public void clearRows(){
        int amount = 0;
         for (int i = 0; i<boardHeight;i++){
             amount = 0;
             for (int j = 0; j<boardWidth;j++){
                 if (grid[i][j]!=null)
                     amount++;
             }
             if (amount==boardWidth){
                 removeRow(i);
                 rowsCleared++;
             }
         }
     }

     //This method removes a row from grid.
     public PieceType[][] removeRow(int y){
         PieceType[][] newGrid = grid;
         for (int i = 0;i<boardWidth;i++){
             for (int j =y;j<boardHeight-1;j++){
                 if (newGrid[j][i]!=null){
                     newGrid[j][i]=newGrid[j+1][i];
                 }
             }
         }
         return newGrid;
     }
    /**
     * Applies the given action to the board, mutating it's state.
     */
    @Override
    public Result move(Action act) {
        Result result = Result.SUCCESS;
        lastAction = act;
        if (piece==null){
            result = Result.NO_PIECE;
            return result;
        }

        switch(act){
            case CLOCKWISE:
                //if x coord of bounding box + relative x coord of most left of clockwise piece more than 0
                //and x coord of bounding box + relative x coord of most right of clockwise piece less than boardWidth
                // rotate piece and return success
                //need wall kicks
                Piece p = piece;
                Piece temp = piece.clockwisePiece();
                if(piece.getType()==PieceType.STICK){
                    if(checkCollide(temp, bodyX, bodyY, Piece.I_CLOCKWISE_WALL_KICKS[p.getRotationIndex()])) {
                        piece = (TetrisPiece) temp;
                        bodyY += wallKick.y;
                        bodyX += wallKick.x;
                    }
                }
                else if (checkCollide(temp, bodyX, bodyY, Piece.NORMAL_CLOCKWISE_WALL_KICKS[p.getRotationIndex()])){
                    piece = (TetrisPiece) temp;
                    bodyY+= wallKick.y;
                    bodyX+= wallKick.x;
                }
                else {
                    result = Result.OUT_BOUNDS;
                }

                break;
            case COUNTERCLOCKWISE:
                //if x coord of bounding box + relative x coord of most left of counterclockwise piece more than 0
                //and x coord of bounding box + relative x coord of most right of counterclockwise piece less than boardWidth
                // rotate piece and return success
                // need wall kicks
                Piece p1 = piece;
                Piece temp1 = piece.counterclockwisePiece();
                if(piece.getType()==PieceType.STICK){
                    if(checkCollide(temp1, bodyX, bodyY, Piece.I_COUNTERCLOCKWISE_WALL_KICKS[p1.getRotationIndex()])) {
                        piece = (TetrisPiece) temp1;
                        bodyY += wallKick.y;
                        bodyX += wallKick.x;
                    }
                }
                else if (checkCollide(temp1, bodyX, bodyY, Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[p1.getRotationIndex()])){
                    piece = (TetrisPiece) temp1;
                    bodyY+= wallKick.y;
                    bodyX+= wallKick.x;
                }
                else {
                    result = Result.OUT_BOUNDS;
                }
                break;
            case DOWN:
                //if move down touches smth and need place
                //places
                if (checkBounds(piece, bodyX,bodyY-1)){
                    bodyY--;
                } else {
                    result = Result.PLACE;
                }
                break;
            case DROP:
                bodyY = dropHeight(piece, bodyX);
                result = Result.PLACE;
                break;
            case HOLD:
                break;
            case LEFT:
                if (checkBounds(piece, bodyX-1,bodyY))
                    bodyX--;
                else
                    result = Result.OUT_BOUNDS;
                break;
            case NOTHING:
                break;
            case RIGHT:
                if (checkBounds(piece, bodyX+1,bodyY))
                    bodyX++;
                else
                    result = Result.OUT_BOUNDS;
                break;
        }
        lastResult = result;
        if (result == Result.PLACE){
            grid = updateGrid(piece, bodyX, bodyY);
        }
        clearRows();
        rowWidth = getAllRowWidths();
        columnHeight = getAllColumnHeights();
        maxHeight = setMaxHeight();
        return result;
    }
    /**
     * Returns a new board whose state is equal to what the state of this
     * board would be after the input Action. This operation does not mutate
     * the current board, making it useful for speculatively testing actions
     * in an AI implementation.
     */
    @Override
    public Board testMove(Action act) {
        Board testBoard = new TetrisBoard(boardWidth,boardHeight, maxHeight, piece, rowsCleared, lastAction, lastResult, columnHeight, rowWidth, bodyX, bodyY, grid);
        testBoard.move(act);
        return testBoard;
    }
    /**
     * Return the current piece on the board, or null if there is no current piece.
     */
    @Override
    public Piece getCurrentPiece() {
        return piece;
    }
    /**
     * Return the position of the lower-left hand corner of the current piece's
     * bounding box, or null if there is no current piece.
     *
     * Note that, in the board coordinate system, (0, 0) is the lower left hand corner
     * of the board, where Y increases upwards and X increases to the left. Due to SRS
     * bounding boxes being bigger than the piece, the "current piece position" may technically
     * be outside the bounds of the grid even if the piece itself is in bounds.
     */
    @Override
    public Point getCurrentPiecePosition() {
        if (piece==null){
            return null;
        }
        return new Point(bodyX, bodyY);
    }
    /**
     * Give a piece to the board to use as its next piece, placing the lower left hand corner
     * of the piece's bounding box at the given position.
     *
     * If the piece would be placed in a position that would cause it to intersect with existing pieces
     * or go out of the bounds of the board, an {@link IllegalArgumentException} should be thrown.
     */
    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        if (checkBounds(p,(int) spawnPosition.getX(), (int) spawnPosition.getY()))
        {
            bodyY = (int) spawnPosition.getY();
            bodyX = (int) spawnPosition.getX();
            piece = (TetrisPiece) p;
        }
        else {
            System.err.println("The piece does  not have a valid spawn point.");
        }
    }
    /**
     * Return true if the given object is equal to this object. You are free to assume that the
     * other object is another board; for safety, you should probably verify before casting
     * with an instanceof, though!
     *
     * This should measure the "semantic equality" of two boards - they should be considered equal
     * if they have the same current piece in the same position and have the same grid, but should
     * be oblivious to other internal state like the last action taken/last result returned.
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Board)){
            return false;
        }
        TetrisBoard otherBoard = (TetrisBoard) other;
        if (otherBoard.getHeight() == this.getHeight() && otherBoard.getWidth() == this.getWidth() && otherBoard.getCurrentPiece().equals(this.getCurrentPiece()) && this.getCurrentPiecePosition().equals(otherBoard.getCurrentPiecePosition())){
            for (int i=0; i<otherBoard.getHeight(); i++)
            {
                for (int j=0; j<otherBoard.getWidth(); j++)
                {
                    if(otherBoard.getGrid(j,i)!=this.getGrid(j,i))
                    {
                        return false;
                    }
                }
            }
        }
        else return false;
        return true;
    }
    /**
     * Returns the result of the last action given to the board.
     */
    @Override
    public Result getLastResult() {
        return lastResult;
    }
    /**
     * Returns the last action given to the board.
     */
    @Override
    public Action getLastAction() {
        return lastAction;
    }
    /**
     * Returns the number of rows cleared by the last action; should return 0 if no rows
     * were cleared.
     */
    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }
    /**
     * Returns the width of the board in blocks.
     */
    @Override
    public int getWidth() {
        return boardWidth;
    }
    /**
     * Returns the height of the board in blocks.
     */
    @Override
    public int getHeight() {
        return boardHeight;
    }
    /**
     * Returns the max column height present in the board (as computed by {@link getColumnHeight(int)}).
     * For an empty board this is 0. This should not take the current piece into consideration.
     */
    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Given a piece and an x, returns the y value where the piece would come to
     * rest if it were dropped straight down at that x.
     */
    @Override
    public int dropHeight(Piece piece, int x) {
        int y = bodyY;
        while(checkBounds(piece, x,y-1)){
            y--;
        }
        return y;
    }
    /**
     * Returns the height of the given column -- i.e. the y value of the highest
     * block + 1.  The height is 0 if the column contains no blocks; this should
     * not take the current piece into consideration.
     */
    @Override
    public int getColumnHeight(int x) {
        return columnHeight[x];
    }
    /**
     * Returns the number of filled blocks in the given row; this should not take
     * the current piece into consideration.
     */
    @Override
    public int getRowWidth(int y) {
        return rowWidth[y];
    }

    /**
     * Returns the type of piece that exists at the given position (due to being placed there)
     * - if no placed piece exists at the given position or the given position is out of
     * bounds, return null.
     *
     * This method should not take the current piece into consideration - i.e., calling
     * `getGrid` on a position where the current piece it should simply return null.
     *
     * Note that, in the board coordinate system, (0, 0) is the lower left hand corner
     * of the board, where Y increases upwards and X increases to the left.
     */
    @Override
    public Piece.PieceType getGrid(int x, int y) {
        return grid[y][x];
    }
}