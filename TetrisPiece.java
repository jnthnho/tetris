package assignment;

import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.awt.*;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 *
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     *
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */

    Dimension boundingBox;
    Point[] body;
    Color pieceColor;
    PieceType type;
    int rotationIndex;
    TetrisPiece clockWise;
    TetrisPiece counterClockWise;
    TetrisPiece currentPiece;


    int height;
    int width;
    int[] skirt;

    //Main constructor of TetrisPiece
    public TetrisPiece(PieceType type) {
        // TODO: Implement me.
        this(type,0);
        TetrisPiece zero = this;
        TetrisPiece one = new TetrisPiece(type, 1);
        TetrisPiece two = new TetrisPiece(type, 2);
        TetrisPiece three = new TetrisPiece(type, 3);

        zero.clockWise  = one;
        one.clockWise  = two;
        two.clockWise  = three;
        three.clockWise  = zero;

        zero.counterClockWise  = three;
        one.counterClockWise  = zero;
        two.counterClockWise  = one;
        three.counterClockWise  = two;
    }
    //Custom Constructor of TetrisPiece
    public TetrisPiece(PieceType type, int rotationIndex){
        this.type = type;
        this.boundingBox = type.getBoundingBox();
        this.height = boundingBox.height;
        this.width = boundingBox.width;
        this.pieceColor = type.getColor();
        this.rotationIndex = rotationIndex;

        Point[][] bodyRots= piecesArray(type);
        this.body = bodyRots[rotationIndex];
        this.skirt = makeSkirt(this.body);
    }

    //Return a 2D array of Points for different rotations
    public Point[][] piecesArray(PieceType type){
        int bound = (int)Math.max(boundingBox.getWidth(),boundingBox.getHeight());
        Point [] og = type.getSpawnBody();
        Point[][] rots = new Point[4][4];
        rots[0] = og;
        if (bound==4){
            rots[1] = new Point[] { new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) };
            rots[2] = new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) };
            rots[3] = new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) };
        }
        else if(bound==3){
            for(int i=1; i<4; i++){
                for (int j=0; j<4; j++) {
                    rots[i][j] = new Point((int) (rots[i-1][j].getY()), (int) (2 - rots[i-1][j].getX()));
                }
            }
        } else if (bound==2){
            rots[1] = og;
            rots[2] = og;
            rots[3] = og;
        }
        return rots;
    }
    //Creates a skirt array for a certain array of points
    public int[] makeSkirt(Point [] body){
        skirt = new int[ (int) boundingBox.getWidth()];
        Arrays.fill(skirt, 5);
        for (int i=0; i < body.length; i++)
        {
            if(skirt[(int)body[i].getX()]>(int)body[i].getY()){
                skirt[(int)body[i].getX()] = (int)body[i].getY();
            }
        }
        for (int i=0; i < skirt.length; i++)
        {
            if (skirt[i]==5)
            {
                skirt[i]=Integer.MAX_VALUE;
            }
        }
        return skirt;
    }
    /**
     * Returns the type of this piece.
     */
    @Override
    public PieceType getType() {
        // TODO: Implement me.
        return type;
    }
    /**
     * Return the rotation index of this piece, which is the index of it's current rotation state.
     * An index of 0 refers to the "spawn" orientation, an index of 1 refers to one clockwise
     * rotation of the "spawn" orientation, an index of 2 refers to two clockwise rotations, and
     * an index of 3 refers to three clockwise rotations.
     *
     * Indexes should always fall within [0, 3], and wrap around; i.e., rotating index
     * 3 clockwise produces rotation index 0, and rotating index 0 counterclockwise produecs rotation
     * index 3.
     */
    @Override
    public int getRotationIndex() {
        // TODO: Implement me.
        return rotationIndex;
    }
    /**
     * Returns a piece that is 90 degrees clockwise rotated from this piece,
     * according to the Super Rotation System.
     *
     * This method should not mutate this piece, but rather return a different piece object.
     */
    @Override
    public Piece clockwisePiece() {
        // TODO: Implement me.
        return clockWise;
    }
    /**
     * Returns the piece that is 90 degrees counterclockwise rotated from this piece,
     * according to the Super Rotation System.
     *
     * This method should not mutate this piece, but rather return a different piece object.
     */
    @Override
    public Piece counterclockwisePiece() {
        // TODO: Implement me.
        return counterClockWise;
    }
    /**
     * Returns the width of the piece's SRS bounding box, measured in blocks.
     */
    @Override
    public int getWidth() {
        // TODO: Implement me.
        return width;
    }
    /**
     * Returns the height of the piece's SRS bounding box, measured in blocks.
     */
    @Override
    public int getHeight() {
        // TODO: Implement me.
        return height;
    }
    /**
     * Returns the points in the piece's body, relative to the lower-left hand corner
     * of the piece's SRS bounding box.
     *
     * These points should reflect the current rotation of the piece - the body
     * of a stick, and a 90-degree clockwise rotated stick are different!
     */
    @Override
    public Point[] getBody() {
        // TODO: Implement me.
        return body;
    }
    /**
     * Returns the piece's skirt. For each x value across the piece, the skirt
     * gives the lowest y value in the body relative to the bottom of the SRS
     * bounding box. If there is no block in a given column, the skirt for that column
     * should be Integer.MAX_VALUE.
     */
    @Override
    public int[] getSkirt() {
        // TODO: Implement me.
        return skirt;
    }
    /**
     * Returns true if two pieces are the same - they are the same type and rotation.
     */
    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece otherPiece)) return false;

        // TODO: Implement me.
        if (type == otherPiece.getType() && rotationIndex == otherPiece.getRotationIndex()){
            return true;
        }
        return false;
    }


}