/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatetictactoe;

/**
 *
 * @author SACHIN
 */

import processing.core.PVector;
import java.util.List;
import java.util.Arrays;


public class UltimateTicTacToeGame {

    private int[][][][] board = new int[3][3][3][3];//0, 1, or -1
    //private int[][] selected = new int[3][3];
    private int[][] wboard = new int[3][3];//board with all winners
    private PVector selected;//seleced board
    private int activePlayer;//1 or -1
    private int winner;
    //bitwise winning boards 
    private final List<Integer> valid = Arrays.asList(7, 56, 448, 73, 146, 292, 273, 84);

    public UltimateTicTacToeGame(int[][][][] board) {
        this.board = board;//make sure that this board doesn't change
        for(int r=0;r < 3;r++) {
            for(int c=0;c < 3;c++) {
                this.wboard[r][c] = 0;
        }   }
        this.selected = new PVector(-1,-1);//x and y
        this.activePlayer = 1;
        this.winner = 0;
    }

    public UltimateTicTacToeGame() {
        for(int br=0;br < 3;br++) {
            for(int bc=0;bc < 3;bc++) {
                this.wboard[br][bc] = 0;
                for(int r=0;r < 3;r++) {
                    for(int c=0;c < 3;c++) {
                        board[br][bc][r][c] = 0;
        }   }   }   }
        this.selected = new PVector(-1,-1);//x and y
        this.activePlayer = 1;
        this.winner = 0;
    }

    private int boardBitwise(int[][] board, int check) {//calculate board for check
        int bwb = 0;
        for(int r=0;r < 3;r++) {
            for(int c=0;c < 3;c++) {
                if(board[r][c] == check) bwb += Math.pow(2, r*3+c);     
            }
        }
        return bwb;
    }

    private int check(int[][] sboard) {//0, 1 wins or 2 wins this board
        int xb = boardBitwise(sboard, 1);
        int ob = boardBitwise(sboard, -1);
        for(int v : valid) {//check bitwise
            if((v & xb) == v) return 1;
            if((v & ob) == v) return -1;
        }
        return 0;
    }

    public int turn(int sr, int sc) {//input next move, logic
        //in the correct board
        if(selected.x == -1 || (selected.x == sr/3 && selected.y == sc/3)) {
            //not already occupied
            if(board[sr/3][sc/3][sr%3][sc%3] == 0) {
                board[sr/3][sc/3][sr%3][sc%3] = activePlayer;//either +1, or -1
                activePlayer *= -1;//switch player
                selected.x = sr%3;//small board position is the next selected
                selected.y = sc%3;
                //mark that any board is possible
                if(boardBitwise(board[(int) selected.x][(int) selected.y], 0) == 0) {
                    selected.x = -1;
                    //System.out.println("Player " + getActivePlayer() + " can go anywhere on the board.");
                }
                //board won
                if(wboard[sr/3][sc/3] == 0 && check(board[sr/3][sc/3]) != 0) {
                    wboard[sr/3][sc/3] = check(board[sr/3][sc/3]);//put into this
                    if(check(wboard) != 0) {//Game won
                        return 3;
                    }
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }

    public PVector getSelected() { return selected; }
    public int[][][][] getBoard() { return board; }//alternative?
    public int getActivePlayer() { return activePlayer; }
    public int getWinner() { return winner; }
    public String getActivePlayerStr() { 
        if(activePlayer == 1) return "X";
        if(activePlayer == -1) return "O";
        return "none";
    }
    public String getWinnerStr() { 
        if(winner == 1) return "X";
        if(winner == -1) return "O";
        return "none";
    }
}

