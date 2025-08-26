package assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/*
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

public class TetrisTest {

    // This will run ONCE before all other tests. It can be useful to setup up
    // global variables and anything needed for all of the tests.

    @BeforeAll
    static void setupAll() {

    }

    // This will run before EACH test.
    @BeforeEach
    void setupEach() {
    }
    public static void main (String args[]){
        testTetrisPiece();
        testTetrisBoard();
        testBrain();
    }

    // You can test execute critter here. You may want to make additional tests and
    // your own testing harness. See spec section 2.5 for more details.
    public static void testTetrisPiece() {
        TetrisPiece stick = new TetrisPiece(Piece.PieceType.STICK);
        //Test if all methods work as intended with STICK type
        pieceMethods(stick);
        TetrisPiece ll = new TetrisPiece(Piece.PieceType.LEFT_L);
        //Test if all methods work as intended with LEFT_L shape
        pieceMethods(ll);
        TetrisPiece rl = new TetrisPiece(Piece.PieceType.RIGHT_L);
        //Test if all methods work as intended with RIGHT_L shape
        pieceMethods(rl);
        TetrisPiece s = new TetrisPiece(Piece.PieceType.SQUARE);
        //Test if all methods work as intended with SQUARE shape
        pieceMethods(s);
        TetrisPiece rd = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        //Test if all methods work as intended with RIGHT_DOG shape
        pieceMethods(rd);
        TetrisPiece t = new TetrisPiece(Piece.PieceType.T);
        //Test if all methods work as intended with T type
        pieceMethods(t);
        TetrisPiece ld = new TetrisPiece(Piece.PieceType.LEFT_DOG);
        //Test if all methods work as intended with LEFT_DOG shape
        pieceMethods(ld);

        //Equals edge cases: Same type with same content(2 diff objects) (true)
        //Same type with diff rotations (false)
        //Not a tetris piece (false)
        //Not same type (false)
        TetrisPiece a = new TetrisPiece(t.getType());
        System.out.println();
        System.out.println("Same Type Same Rot Equals Test:"+t.equals(a));
        a.clockwisePiece();
        System.out.println("Same Type Diff Rot Equals Test:"+t.equals(a));
        System.out.println("Not Tetris Type Equals Test:"+t.equals(new String()));
        System.out.println("Diff Type Equals Test:"+ t.equals(ld));
        
    }
    //Prints the content that requires all methods of the TetrisPiece class to work
    //Assume all the predefined types' shapes cannot be changed
    public static void pieceMethods(TetrisPiece tp){
        System.out.println("Type: "+tp.getType());
        System.out.println("Width: "+tp.getWidth());
        System.out.println("Height: "+tp.getWidth());
        //ClockWise Test
        for (int i = 0; i<12; i++)
        {
            System.out.println("Rotation #"+tp.getRotationIndex()+":"+ Arrays.toString(tp.getBody()));
            System.out.println("Skirt #"+tp.getRotationIndex()+":"+ Arrays.toString(tp.getSkirt()));
            tp = (TetrisPiece) tp.clockwisePiece();
        }
        //CounterClockWise Test
        for (int i = 0; i<12; i++)
        {
            System.out.println("Rotation #"+tp.getRotationIndex()+":"+ Arrays.toString(tp.getBody()));
            System.out.println("Skirt #"+tp.getRotationIndex()+":"+ Arrays.toString(tp.getSkirt()));
            tp = (TetrisPiece) tp.counterclockwisePiece();
        }
        System.out.println();
    }
    public static void testTetrisBoard() {
        //Edge Cases (Move): Wall kicks around walls, wall kicks around other objects, trying to shift left or right into walls or other objects,
        //Edge Cases, drop (with and without a piece at the bottom), equals (object is not of type board)
        System.out.println();
        System.out.println("Test if testBoardMove is working.");
        //Testing testMove, getCurrentPiece, getCurrentPiecePosition, nextPiece, getLastAction, getLastResult, equals, getRowsClear, getWidth, getHeight, getGrid, getRowWidth, getColumnHeight
        TetrisBoard board1 = new TetrisBoard(10,12);
        Piece nextPiece = new TetrisPiece(Piece.PieceType.STICK);
        //Make sure nextPiece is working as intended
        board1.nextPiece(nextPiece, new Point(board1.getWidth() / 2 - nextPiece.getWidth() / 2, 10));
        TetrisBoard board2 = (TetrisBoard) board1.testMove(Board.Action.DROP);
        board1.move(Board.Action.DROP);
        System.out.println("Actions:"+ (board1.getLastAction() == board2.getLastAction()));
        System.out.println("Results:"+ (board1.getLastResult() == board2.getLastResult()));
        System.out.println("Positions:"+ (board1.getCurrentPiecePosition().equals(board2.getCurrentPiecePosition())));
        System.out.println("Grid1: ");
        System.out.println("Column Widths: " + Arrays.toString(board1.columnHeight)+" {");
        for (int i = 0; i< board1.boardHeight; i++) {
            System.out.println("Row Width:"+ (board1.rowWidth[i]));
            System.out.print("{");
            for (int j = 0; j < board1.boardWidth; j++)
            {
                System.out.print(board1.getGrid(j,i)+" ");
            }
            System.out.println("}");
        }
        System.out.println("Grid2: ");
        System.out.println("Column Widths: " + Arrays.toString(board2.columnHeight)+" {");
        for (int i = 0; i< board2.boardHeight; i++) {
            System.out.println("Row Width:"+ (board2.rowWidth[i]));
            System.out.print("{");
            for (int j = 0; j < board2.boardWidth; j++)
            {
                System.out.print(board2.getGrid(j,i)+" ");
            }
            System.out.println("}");
        }
        System.out.println("}");
        System.out.println("Board Height:"+ (board1.getHeight() == board2.getHeight()));
        System.out.println("Board Width:"+ (board1.getWidth() == board2.getWidth()));
        System.out.println("Max Height:"+ (board1.getMaxHeight() == board2.getMaxHeight()));
        System.out.println("Piece:"+ (board1.getCurrentPiece().equals(board2.getCurrentPiece())));
        System.out.println("Rows Cleared:"+ (board1.getRowsCleared() == board2.getRowsCleared()));
        System.out.println("Both boards are equal: "+ board1.equals(board2));
        System.out.println("Drop Height1: " + board1.dropHeight(board1.piece, board1.bodyX));
        System.out.println("Drop Height2: " + board1.dropHeight(board2.piece, board2.bodyX));
        System.out.println();
        testMoveBoard();

        //Edge Cases (Move): Wall kicks around walls, wall kicks around other objects, trying to shift left or right into walls or other objects, down into another object

    }

    //Prints the content that requires all methods of the TetrisPiece class to work
    //Assume all the predefined types' shapes cannot be changed
    public static void testMoveBoard() {
        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece.PieceType grid[][] = new Piece.PieceType[12][10];
        grid[0][3] = Piece.PieceType.STICK;
        grid[1][3] = Piece.PieceType.STICK;
        grid[2][3] = Piece.PieceType.STICK;
        grid[3][3] = Piece.PieceType.STICK;
        System.out.println("ORIGINAL");
        printGrid(grid);
        TetrisBoard board1 = new TetrisBoard(10,12);
        board1.grid = grid;
        Piece nextPiece = new TetrisPiece(Piece.PieceType.STICK);
        board1.nextPiece(nextPiece, new Point(0, 8));
        TetrisBoard board2 = (TetrisBoard) board1.testMove(Board.Action.DROP);
        System.out.println(board2.getLastResult()+"- AFTER DROP");
        printGrid(board2.grid);
        board2.nextPiece(nextPiece, new Point(6, 8));
        board2.move(Board.Action.COUNTERCLOCKWISE);
        board2.move(Board.Action.RIGHT);
        board2.move(Board.Action.RIGHT);
        board2.move(Board.Action.CLOCKWISE);
        board2.move(Board.Action.DROP);
        System.out.println(board2.getLastResult()+"- AFTER WALLKICK CLOCKWISE");
        printGrid(board2.grid);
        board2.nextPiece(nextPiece, new Point(6, 8));
        board2.move(Board.Action.COUNTERCLOCKWISE);
        board2.move(Board.Action.RIGHT);
        System.out.println(board2.getLastResult()+"- AFTER RIGHT: "+board2.bodyX);
        board2.move(Board.Action.RIGHT);
        System.out.println(board2.getLastResult()+"- AFTER RIGHT: "+board2.bodyX);
        board2.move(Board.Action.COUNTERCLOCKWISE);
        board2.move(Board.Action.DROP);
        System.out.println(board2.getLastResult()+"- AFTER WALLKICK COUNTERCLOCKWISE");
        printGrid(board2.grid);
        board2.nextPiece(nextPiece, new Point(4, 3));
        board2.move(Board.Action.CLOCKWISE);
        board2.move(Board.Action.LEFT);
        System.out.println(board2.getLastResult()+"- AFTER LEFT: "+board2.bodyX);
        board2.move(Board.Action.LEFT);
        System.out.println(board2.getLastResult()+"- AFTER LEFT: "+board2.bodyX);
        board2.move(Board.Action.CLOCKWISE);
        board2.move(Board.Action.DROP);
        System.out.println(board2.getLastResult()+"- AFTER OBJECT WALLKICK CLOCKWISE");
        printGrid(board2.grid);
        board2.nextPiece(nextPiece, new Point(4, 3));
        board2.move(Board.Action.CLOCKWISE);
        board2.move(Board.Action.LEFT);
        System.out.println(board2.getLastResult()+"- AFTER LEFT: "+board2.bodyX);
        board2.move(Board.Action.LEFT);
        System.out.println(board2.getLastResult()+"- AFTER LEFT: "+board2.bodyX);
        board2.move(Board.Action.COUNTERCLOCKWISE);
        board2.move(Board.Action.DROP);
        System.out.println(board2.getLastResult()+"- AFTER OBJECT WALLKICK COUNTERCLOCKWISE");
        printGrid(board2.grid);
        board2 = (TetrisBoard) board1.testMove(Board.Action.DOWN);
        System.out.println(board2.getLastResult()+"- AFTER DOWN: "+board2.bodyY);
        board2 = (TetrisBoard) board1.testMove(Board.Action.LEFT);
        System.out.println(board2.getLastResult()+"- AFTER LEFT:"+board2.bodyX);
        board2 = (TetrisBoard) board1.testMove(Board.Action.RIGHT);
        System.out.println(board2.getLastResult()+"- AFTER RIGHT: "+board2.bodyX);
    }

    public static void printGrid(Piece.PieceType [][] grid) {

        for (int i = grid.length-1; i>=0; i--) {
            System.out.print("{");
            for (int j = 0; j <= grid[i].length-1; j++) {
                System.out.print(grid[i][j]+" ");
            }
            System.out.println("}");
        }
    }

    public static void testBrain(){

        Piece.PieceType square = Piece.PieceType.SQUARE;
        Piece.PieceType grid[][] = new Piece.PieceType[24][10];
        System.out.println("Grid Length: " + (grid.length-1));
        grid[0][3] = Piece.PieceType.STICK;
        grid[1][3] = Piece.PieceType.STICK;
        grid[2][3] = Piece.PieceType.STICK;
        grid[3][3] = Piece.PieceType.STICK;

        grid[0][5] = Piece.PieceType.STICK;
        grid[1][5] = Piece.PieceType.STICK;
        grid[2][5] = Piece.PieceType.STICK;
        grid[3][5] = Piece.PieceType.STICK;

        grid[0][7] = Piece.PieceType.STICK;
        grid[1][7] = Piece.PieceType.STICK;
        grid[2][7] = Piece.PieceType.STICK;
        grid[3][7] = Piece.PieceType.STICK;

        grid[3][6] = Piece.PieceType.STICK;
        System.out.println("ORIGINAL");
        printGrid(grid);
        TetrisBoard board = new TetrisBoard(10,24);
        board.grid = grid;
        Piece nextPiece = new TetrisPiece(Piece.PieceType.STICK);
        board.nextPiece(nextPiece, new Point(2, 8));
        //TetrisBoard board2 = (TetrisBoard) board1.testMove(Board.Action.DROP);
        System.out.println("AFTER DROP");
        //printGrid(board2.grid);

        RealBrain bob = new RealBrain();

        bob.enumerateOptions(board);
        ArrayList<Board> chicken = bob.options;
        ArrayList<Board.Action> fries = bob.firstMoves;
        if (chicken.size()==40){
            System.out.println("EnumerateOptions is working");
        }
        double[] scores = new double[chicken.size()];
        for (int i = 0;i<chicken.size();i++){
            scores[i] = bob.scoreBoard(chicken.get(i));
        }

        Board.Action yeet = bob.nextMove(board);
        double maxScore = 0;
        int indexMaxScore = 0;
        for (int i = 0; i<scores.length;i++){
            double score = scores[i];
            if (score>maxScore){
                maxScore = score;
                indexMaxScore = i;
            }
        }
        System.out.println("MaxScore: " + maxScore);
        System.out.println("Index of Max Score: " + indexMaxScore);

        if (fries.get(indexMaxScore).equals(yeet)){
            System.out.println("nextScore is using the maximum score");
        } else {
            System.out.println("Shiba its lookin a bit potatoed");
        }

        brainMethods(board);


    }

    public static void brainMethods(Board board){
        RealBrain bob = new RealBrain();
        int billy = bob.getRoughness(board);
        System.out.println("GetRoughness: " + billy);
        if (billy == 16){
            System.out.println("GetRoughness works!");
        } else {
            System.out.println("GetRoughness doesn't work");
        }

        int stefan = bob.getHoles(board);
        System.out.println("GetHoles: " + stefan);
        if (stefan == 3){
            System.out.println("GetHoles works!");
        } else {
            System.out.println("GetHoles doesn't work");
        }

        int baguette = bob.deepestHole(board);
        System.out.println("DeepestHole: " + baguette);
        if (baguette == 4){
            System.out.println("DeepestHole works!");
        } else {
            System.out.println("DeepestHole doesn't work");
        }

        int strawberry = bob.columnTransitions(board);
        System.out.println("ColumnTransitions: " + strawberry);
        if (strawberry == 1){
            System.out.println("ColumnTransitions works!");
        } else {
            System.out.println("ColumnTransitions doesn't work");
        }

        int jill = bob.rowTransitions(board);
        System.out.println("RowTransitions: " + jill);
        if (jill == 11){
            System.out.println("RowTransitions works!");
        } else {
            System.out.println("RowTransitions doesn't work");
        }

        int jack = bob.avgHeights(board);
        System.out.println("avgHeights: " + jack);
        if (jack == 1){
            System.out.println("avgHeights works!");
        } else {
            System.out.println("avgHeights doesn't work");
        }

        int jean = bob.getRowsCleared(board);
        System.out.println("getRowsCleared: " + jean);
        if (jean == 0){
            System.out.println("getRowsCleared works!");
        } else {
            System.out.println("getRowsCleared doesn't work");
        }

        TetrisBoard boarder = (TetrisBoard) board;
        int juan = boarder.getAllColumnHeights()[3];
        System.out.println("getMaxHeight: " + juan);
        if (juan == 4){
            System.out.println("getMaxHeight works!");
        } else {
            System.out.println("getMaxHeight doesn't work");
        }
    }

}
