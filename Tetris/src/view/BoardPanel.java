/*
 * Mohamed Dahir
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Board;
import model.Board.BoardData;
import model.Board.CompletedLines;
import model.Board.GameStatus;
import model.Point;
import model.TetrisPiece;

/**
 * This panel controls the game board.
 * 
 * @author n4tunec@uw.edu.
 * @version winter 2015.
 *
 */
public class BoardPanel extends JPanel implements Observer {

    /** The board panel serial version id. */
    private static final long serialVersionUID = 1L;

    /** The start location of the grid to be drawn.*/
    private static final int MY_GRID_START = 12;
    
    /** The tetris piece size. */
    private static final int TETRIS_SIZE = 20;

    /** The piece point relative to its container.. */
    private static final int PIECE_TO_PANEL_RATIO = 3;

    /** The game over label font size. */
    private static final int FONT_SIZE = 16;

    /** The board X dimension. */
    private static final int BOARD_X = 14;

    /** The board Y dimension. */
    private static final int BOARD_Y = 30;

    /** The game board. */
    private final Board myBoard;
    /** The game board data. */
    private BoardData myBoardData;
    
    /**Determines weather the game was ended.*/
    private boolean myEndGame;

    /** The color data. */
    private List<Color[]> myColorData;

    /** The next tetris piece to be displayed. */
    private TetrisPiece myNextPiece;

    /** The present game status. */
    private GameStatus myGameStatus;
    
    /**Number of clear lines.*/
    private int myClearCount;
    
    /** Keeps track of the pause of the game.*/
    private boolean myPause;
    
    /** To determine of grid was selected.*/
    private boolean myGridSelected;
    
    

    /**
     * Default constructor initializes the fields.
     */
    public BoardPanel() {
        super();
        setBackground(Color.BLACK);
        myNextPiece = TetrisPiece.getRandomPiece();

        myBoard = new Board(BOARD_X, BOARD_Y);
        myBoard.addObserver(this);
        myColorData = new ArrayList<>();
        
        myBoard.clear();
    }

    /**
     * Paints the Blocks on the panel.
     * 
     * @param theGraphics this graphics o=drawing objects.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {

        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (myGridSelected) {
            int startX = MY_GRID_START;
            int startY = 0;
            final Line2D grid = new Line2D.Double();

            for (int j = 0; j < getWidth() / MY_GRID_START * 2; j++) {
                grid.setLine(startX, startY, startX, getHeight());
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1f));
                g2d.draw(grid);
                startX += MY_GRID_START * 2;
            }

            startX = 0;
            startY = MY_GRID_START;
            for (int i = 0; i < getHeight() / MY_GRID_START * 2; i++) {

                grid.setLine(startX, startY, getWidth(), startY);
                g2d.draw(grid);
                
                startY += MY_GRID_START * 2;
            }
        }

        
        for (int i = 0; i < myColorData.size(); i++) {
            final Color[] colorRow = myColorData.get(i);
            for (int j = 0; j < colorRow.length; j++) {
                final Color newColor = colorRow[j];
                g2d.setColor(newColor);

                final Point currentPoint = new Point(j * TETRIS_SIZE, i * TETRIS_SIZE);
                if (newColor != null) {
                    g2d.setFont(new Font("Comic Sans ms", Font.BOLD, FONT_SIZE));
                    if (myGameStatus.isGameOver()) {
                        
                        g2d.setColor(Color.WHITE);
                        g2d.drawString("Game Over", getWidth() / PIECE_TO_PANEL_RATIO,
                                       getHeight() / PIECE_TO_PANEL_RATIO);
                        
                    } else {
                        if (myPause) {
                            g2d.drawString("Game Paused", getWidth() / PIECE_TO_PANEL_RATIO,
                                       getHeight() / PIECE_TO_PANEL_RATIO);
                        } else if (myEndGame) {

                            g2d.fill(new Rectangle2D.Double(currentPoint.x() + 2, getHeight()
                                                            - currentPoint.y()
                                                            - TETRIS_SIZE,
                                          TETRIS_SIZE, TETRIS_SIZE));
                        }
//                        g2d.fill(new Rectangle2D.Double(currentPoint.x() + 2, getHeight() - 
//                                                        currentPoint.y() - 
//                                                        TETRIS_SIZE,TETRIS_SIZE, TETRIS_SIZE));
                        g2d.fill(new Ellipse2D.Double(currentPoint.x() + 2, getHeight() - 
                                                        currentPoint.y() - 
                                                        TETRIS_SIZE,TETRIS_SIZE, TETRIS_SIZE));
                                                                         
                                                                          
                                                        
                    }
                }
            }
        }
    }

    @Override
    public void update(final Observable theObservable, final Object theObject) {

        if (theObservable instanceof Board) {
            if (theObject instanceof BoardData) {

                myBoardData = (BoardData) theObject;
                myColorData = myBoardData.getBoardData();
              
                repaint();
            } else if (theObject instanceof TetrisPiece) {
                myNextPiece = (TetrisPiece) theObject;
                
            } else if (theObject instanceof GameStatus) {
                myGameStatus = (GameStatus) theObject;

            } else if (theObject instanceof CompletedLines) {
                myClearCount++;
            }
        }
    }

    /** Querry for the number of completed lines.
     * @return the number of cleared lines.*/
    public int getCompletedLines() {
        return myClearCount;
    }
    
    /**
     * Determines weather the game is over.
     * @return true if the game is over.
     */
    public boolean isGameOver() {
        return myGameStatus.isGameOver();
    }
    
    /** Sets the end game to true of false.
     * @param theEndGame the new state of the end game.
     * */
    public void setEndGame(final boolean theEndGame) {
        myEndGame = theEndGame;
    }
    
    /**
     * This lets the next piece panel know 
     * that some thing has changed in the board.
     */
    public void fireChangeListsner() {
        firePropertyChange("board", myBoard, myNextPiece);
    }
    
    /**
     * The returns the next tetris piece to be displayed.
     * @return the Next tetris piece.
     */
    public TetrisPiece getNextPanelPiece() {
        return myNextPiece;
    }

    /**
     * The returns the next tetris piece color.
     * @return the Next tetris piece color.
     */
    public Color getNextPieceColor() {
        return myNextPiece.getColor();
    }
    
    /**
     * returns the paused state of the board.
     * @return true of game is paused.
     */
    public boolean isPause() {
        return myPause;
    }
    
    /** This moves the object with the timer.*/
    public void moveObjects() {
        myBoard.step();
    }

    /** Moves the piece to the right.*/
    public void moveRight() {
        myBoard.right();
    }

    /** Moves the piece to the left.*/
    public void moveLeft() {
        myBoard.left();
    }

    /** Moves the piece to the down.*/
    public void moveDown() {
        myBoard.down();
    }

    /** Moves the piece to the down.*/
    public void drop() {
        myBoard.drop();
    }

    /** Rotates the piece CCW.*/
    public void moveRotateLeft() {
        myBoard.rotateCCW();
    }

    /** Rotates the piece CW.*/
    public void moveRotateRight() {
        myBoard.rotateCW();
    }
    /** Pauses the game. */
    public void pause() {
        myPause ^= true;
        repaint();
    }
    /** Restarts a new game.*/
    public void restart() {
        myBoard.clear();
        myClearCount = 0;
    }
    
    /**
     * to know is the grid check box was selected.
     * @param theGridSelected true if the checkbox was checked.
     */
    public void setGridSelected(final boolean theGridSelected) {
        myGridSelected = theGridSelected;
    }

}
