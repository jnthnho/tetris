package assignment;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthDesktopIconUI;

import assignment.Board.Action;

public class RealBrain implements Brain{
    public ArrayList<Board> options = new ArrayList<>();
    public ArrayList<Board.Action> firstMoves = new ArrayList<>();
    public int rowsCleared;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        rowsCleared = currentBoard.getRowsCleared();
        enumerateOptions(currentBoard);

        double best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i));
            // System.out.println(score);
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        System.out.println("Best Move Score: " + best);

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    public void enumerateOptions(Board currentBoard) {
        // We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);

        // Now we'll add all the places to the left we can DROP
        Board left = currentBoard.testMove(Board.Action.LEFT);
        // System.out.println(left);
        // System.out.println("Left: " + left.getLastResult());

        Board.Action rotation = null;
        for(int i = 0; i < 4; i++){
            if(i==3){
                rotation = Action.COUNTERCLOCKWISE;
            }
            while (left.getLastResult() == Board.Result.SUCCESS) {
                options.add(left.testMove(Board.Action.DROP));
                if(rotation!=null){
                    firstMoves.add(rotation);
                } else {
                    firstMoves.add(Board.Action.LEFT);
                } 
                left.move(Board.Action.LEFT);
            }

            // And then the same thing to the right
            Board right = currentBoard.testMove(Board.Action.RIGHT);
            // System.out.println(right);
            // System.out.println("Right: " + right.getLastResult());
            while (right.getLastResult() == Board.Result.SUCCESS) {
                options.add(right.testMove(Board.Action.DROP));
                if(rotation!=null){
                    firstMoves.add(rotation);
                } else {
                    firstMoves.add(Board.Action.RIGHT);
                } 
                right.move(Board.Action.RIGHT);
            }
            currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);
            rotation = Board.Action.CLOCKWISE;
        }
    }

    public int getRoughness(Board board){
        TetrisBoard boarder = (TetrisBoard) board;
        int[] columnHeights = boarder.getAllColumnHeights();
        int roughness = 0;
        for (int i = 0;i<columnHeights.length-1;i++){
            int diff = columnHeights[i] - columnHeights[i+1];
            roughness += Math.abs(diff);
        }
        return roughness;
    }

    public int getHoles(Board board){
        TetrisBoard bigBoard = (TetrisBoard) board;
        int holes = 0;
        int[] heights = bigBoard.getAllColumnHeights();
        for (int x = 0; x < board.getWidth();x++){
            for (int y = 0;y<heights[x]-1;y++){
                if(board.getGrid(x,y)==null){
                   holes++;
                }
            }
        }
        return holes;

        // int holes = 0;
        // for (int y = 0;y<board.getHeight()-1;y++){
        //     for (int x = 0; x < board.getWidth();x++){
        //         if(board.getGrid(x,y)==null){
        //             if (board.getGrid(x,y+1)!=null){
        //                 holes++;
        //             }
        //         }
        //     }
        // }
        // return holes;
    }

    public int deepestHole(Board board){
        TetrisBoard boarder = (TetrisBoard) board;
        int[] columnHeights = boarder.getAllColumnHeights();
        int deepestHole = columnHeights[1] - columnHeights[0];
        for (int i = 1;i<columnHeights.length-1;i++){
            int hole1 = columnHeights[i-1] - columnHeights[i];
            int hole2 = columnHeights[i+1] - columnHeights[i];
            int hole = Math.min(hole1, hole2);
            if (hole>deepestHole){
                deepestHole = hole;
            }
        }

        int hole = columnHeights[8]-columnHeights[9];
        if (hole>deepestHole){
            deepestHole = hole;
        }
        return deepestHole;
    }

    public int columnTransitions(Board board){
        int columnTransitions = 0;
        for (int y = 0;y<board.getHeight()-1;y++){
            for (int x = 0; x < board.getWidth();x++){
                if(board.getGrid(x,y)==null){
                    if (board.getGrid(x,y+1)!=null){
                        columnTransitions++;
                    }
                }
            }
        }
        return columnTransitions;
    }

    public int rowTransitions(Board board){
        int rowTransitions = 0;
        for (int y = 0;y<board.getHeight();y++){
            for (int x = 0; x < board.getWidth()-1;x++){
                if(board.getGrid(x,y)==null){
                    if (board.getGrid(x+1,y)!=null){
                        rowTransitions++;
                    }
                }
            }
        }
        return rowTransitions;
    }

    public int avgHeights(Board board){
        TetrisBoard bigBoard = (TetrisBoard) board;
        int sum = 0;
        int[] sumHeights = bigBoard.getAllColumnHeights();
        for (int i = 0;i<sumHeights.length;i++){
            sum+=sumHeights[i];
        }
        return sum/bigBoard.getWidth();
    }

    public int getRowsCleared(Board board){
        return board.getRowsCleared() - rowsCleared;
    }

    public int getMaxHeight(Board board){
        int getMaxHeight = board.getMaxHeight();
        if (getMaxHeight>board.getHeight()-5){
            getMaxHeight =2000;
        }
        return getMaxHeight;
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    public double scoreBoard(Board newBoard) {
        // 3.05, 0.6, 0.25, 150, 15
        // System.out.println("Holes: " + getHoles(newBoard));
        return 2000 -  (avgHeights(newBoard) * 1.55) - (getMaxHeight(newBoard) * 0.45) - (getRoughness(newBoard)*0.6) + (getRowsCleared(newBoard) * 22) - (getHoles(newBoard)*5) - (deepestHole(newBoard)*3) - (rowTransitions(newBoard)*2.5) - (columnTransitions(newBoard)*2.5);
    }
    }
