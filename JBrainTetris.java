package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.plaf.synth.SynthScrollBarUI;

import assignment.Piece.PieceType;

public class JBrainTetris extends JTetris {
    static LameBrain brain = new LameBrain();
    static RealBrain brainer = new RealBrain();
    protected javax.swing.Timer BrainTimer;
    public static void main(String[] args){
        createGUI(new JBrainTetris());

    }

    JBrainTetris(){
        super();
        /* 
        BigTimer = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick(Board.Action.DOWN);
            }
        });
        */
        BrainTimer = new javax.swing.Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    Board.Action verb = brainer.nextMove(board);
                    tick(verb);
        }
        });
    }

//     @Override
//     public void tick(Board.Action verb){
//         System.out.println(verb);
//         if (verb == Board.Action.DOWN){
//             super.tick(verb);
//         } else {
//         System.out.println("NextMove: " + brain.nextMove(board));
//         verb = brain.nextMove(board);
//         super.tick(verb);
//         }
//     }


    @Override
    public void startGame(){
        super.startGame();
        BrainTimer.start();
        //BigTimer.start();
    }

    @Override
    public void stopGame(){
        super.stopGame();
        BrainTimer.stop();
        //BigTimer.start();
        }


}
