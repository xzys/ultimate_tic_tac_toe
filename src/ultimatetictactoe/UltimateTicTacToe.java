/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ultimatetictactoe;

import java.util.ArrayList;
import processing.core.*;


public class UltimateTicTacToe extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "ultimatetictactoe.UltimateTicTacToe" });
    }
    
    
    //processing dependent
    UltimateTicTacToeGame game;
    Console console;
    PGraphics gb;//graphics board
    PVector ms;//hovering box
    String buffer;
    boolean entered;
    int state;

    public void setup() {
        size(530, 300);
        background(255);
        frameRate(30);

        game = new UltimateTicTacToeGame();

        console = new Console(createFont("Courier New", 12), 21, false);
        gb = createGraphics(300, 300);//game board buffer
        ms = new PVector(0,0);
        buffer = "";
        entered = false;
        state = 0;
      //noLoop();
    }

    public void draw(){
        background(255);
        console.render(320,0);
        //translate(20,20);
        switch(state) {
            case 0://setup
                gb.beginDraw();
                drawBoard();
                gb.endDraw();
                image(gb, 0, 0);//background
                console.println("Game Started.");
                //console.println("Player 1 is X./nPlayer 2 is O.");
                console.print("Player " + game.getActivePlayerStr() + "'s move: ");
                state = 1;
                break;
            case 1://waiting
                image(gb, 0, 0);//background
                ms = getMouse();//mouse selected          
                fill(0,20);
                noStroke();
                if(ms.x != -1) 
                    rect(ms.x*33.33f, ms.y*33.33f, 33f, 33f);//draw selected rect
                
                if(game.getSelected().x != -1)
                    rect(game.getSelected().x*100, game.getSelected().y*100, 100, 100);           
                //mouse input
                if(entered) {//mouse click
                    entered = false;
                    if(ms.x != -1) {//check is click is valid
                        //println((int) selected.x + " " + (int) selected.y);
                        state = 2;
                    } else {
                        console.println("");
                        console.print("Err: Click inside the board.");
                    }
                }            
                break;
            case 2://next turn
                int sr, sc;
                sr = (int) ms.x;
                sc = (int) ms.y;
                int result = game.turn(sr, sc);
                if(result != 0) {//turn
                    console.println(sr + " " + sc);
                    gb.beginDraw();//print x to gb
                    //has to be opposite because game.turn flips it
                    if(game.getActivePlayer() == -1) drawX(sr, sc);
                    if(game.getActivePlayer() == 1) drawO(sr, sc);
                    if(result == 2 || result == 3) {//draw cross through board
                        if(game.getActivePlayer() == -1) drawBigX(sr/3, sc/3);
                        if(game.getActivePlayer() == 1) drawBigO(sr/3, sc/3);
                        console.println("Board won!");
                        if(result == 3) console.println("Game won!");
                    }
                    gb.endDraw();
                } else {
                    console.println("Bad input!");
                }
                image(gb, 0, 0);//background
                if(game.getSelected().x == -1) 
                    console.println("Player " + game.getActivePlayerStr() + " can go anywhere!");
                console.print("Player " + game.getActivePlayerStr() + "'s move: ");
                if(game.getWinner() != 0) state = 3;
                else state = 1;
                break;
            case 3: 
                console.print("Player " + game.getWinnerStr() + " wins!");
                state = 0;
                break;
            case 4:
                exit();
                break;
        }
    }

    //translate applied here
    public PVector getMouse() {
        if(mouseX >= 0 && mouseX <= 300 && mouseY >= 0 && mouseY <= 300)
            return new PVector((int) (mouseX)/33, (int) (mouseY)/33);//within bounds
        else return new PVector(-1, -1);
    }

    public void mouseReleased() { entered = true; }

    public void keyReleased() { if(keyCode == 10) entered = true; }
    //buffer += key;
    //println("released " + int(key) + " " + keyCode);

    //assume board is 100, small board is 30 this is the simplest way to do it
    public void drawBoard() {
        gb.smooth();
        gb.stroke(0);
        gb.strokeWeight(2);
        gb.background(255);
        //gb.pushMatrix();
        //gb.translate(0, 0);
        for(int i=1;i < 3;i++) {
            gb.line(i*100, 0, i*100, 300);
            gb.line(0, i*100, 300, i*100);
        }
        //gb.popMatrix();
        gb.stroke(50);
        gb.strokeWeight(1.2f);
        for(int i=0;i  <3;i++) {
            for(int k=0;k < 3;k++) {
                gb.pushMatrix();
                gb.translate(i*100, k*100);
                for(int j=1;j < 3;j++) {
                    gb.pushMatrix();
                    gb.translate(5, 5);
                    gb.line(j*30, 0, j*30, 90);
                    gb.line(0, j*30, 90, j*30);
                    gb.popMatrix();
                }
                gb.popMatrix();
            }
        }
    }

    public void drawX(int sr, int sc) {
        gb.smooth();
        gb.pushMatrix();
        gb.translate((sr/3)*100 + 5 + (sr%3)*30, (sc/3)*100 + 5 + (sc%3)*30);
        gb.stroke(0, 122, 255);
        gb.strokeWeight(1.0f);
        int of = 7;
        gb.line(of, of, 30-of, 30-of);
        gb.line(of, 30-of, 30-of, of);
        gb.popMatrix();
    }

    public void drawO(int sr, int sc) {
        gb.smooth();
        gb.pushMatrix();
        gb.translate((sr/3)*100 + 5 + (sr%3)*30, (sc/3)*100 + 5 + (sc%3)*30);
        gb.stroke(255, 51, 0);
        gb.fill(0,0,0,0);
        gb.strokeWeight(1.0f);
        int of = 11;
        gb.ellipse(15, 15, 30 - of, 30 - of);
        gb.popMatrix();
    }

    public void drawBigX(int r, int c) {
        gb.smooth();
        gb.pushMatrix();
        gb.translate(r*100, c*100);
        gb.stroke(0, 122, 255, 200);
        gb.strokeWeight(4.0f);
        int of = 21;
        gb.line(of, of, 100-of, 100-of);
        gb.line(of, 100-of, 100-of, of);
        gb.popMatrix();
    }

    public void drawBigO(int r, int c) {
        gb.smooth();
        gb.pushMatrix();
        gb.translate(r*100, c*100);
        gb.stroke(255, 51, 0, 200);
        gb.fill(0,0,0,0);
        gb.strokeWeight(4.0f);
        int of = 33;
        gb.ellipse(50, 50, 100-of, 100-of);
        gb.popMatrix();
    }
    

    
    
    
    public class Console  {
    private ArrayList<String> lines;//lines of text
    private int dlines;//number of lines displayed
    private PFont font;
    private int spacing;
    private boolean upwards;
    
    public Console(PFont font, int dlines, boolean upwards) {
        this.font = font;
        this.dlines = dlines;
        this.spacing = 14;//replace this later
        this.upwards = upwards;
        lines = new ArrayList<String>(dlines);
        for(int i=0;i < dlines;i++) lines.add("");
        System.out.println("Console started:");
        println("Console started:");
    }
    public void println(String s) {
        this.print(s);
        lines.add("");//newline
    }//bugs here
    public void print(String s) { lines.set(lines.size()-1, lines.get(lines.size()-1) + s); }
    public void clear() { for(int i=0;i < dlines;i++) lines.add(""); }
    public void render(int tx, int ty) {
        pushMatrix();
        translate(tx, ty);
        for(int i=lines.size()-dlines;i < lines.size();i++) {
            fill(20,20,20);//default fill
            textFont(font);      
            //different colors for different messages
            if(lines.get(i).startsWith("Err:")) fill(242,151,2);
            if(!lines.get(i).equals("")) {
                if(upwards) text(lines.get(i), 0, spacing*(lines.size()-i));
                else text(lines.get(i), 0, spacing*(dlines+1+i-lines.size()));
            }
        }
        popMatrix(); 
    }
    
}
}
    
