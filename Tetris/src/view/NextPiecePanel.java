/*
 * Mohamed Dahir.
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import model.Point;
import model.TetrisPiece;

/**
 * This panel is responsible for displaying the nextpiece to be shown.
 * 
 * @author n4tunec@uw.edu.
 * @version winter 2015.
 *
 */
public class NextPiecePanel extends JPanel implements PropertyChangeListener {

    /** The serial version id for the next piece panel. */
    private static final long serialVersionUID = 1L;
    /** The Tetris size. */
    private static final int TETRIS_SIZE = 20;
    /** The next piece coordinate ratio to the panel. */
    private static final int NEXT_PIECE_SCALE = 3;

    /** The dimension of the nextPiece panel. */
    private static final Dimension MY_SIZE = new Dimension(new Dimension(180, 200));
    /** The next piece Points. */
    private Point[] myPoints;

    /** The next piece to be drawn. */
    private TetrisPiece myPiece;
    /** The nextPiece Color. */
    private Color myPieceColor;
    
    /**
     * Constructor initialized the fields. sets up my jpanel.
     */
    public NextPiecePanel() {
        super();
        setPreferredSize(MY_SIZE);
        setBackground(Color.BLACK);

    }

    /**
     * Paints the Blocks on the panel.
     * @param theGraphics this graphics o=drawing objects.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {

        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (myPiece != null) {
            myPoints = myPiece.getPoints();

            for (int i = 0; i < myPoints.length; i++) {
                g2d.setColor(myPieceColor);
                
                g2d.fillRect(myPoints[i].x() * TETRIS_SIZE + getWidth() / NEXT_PIECE_SCALE,
                             getHeight() - myPoints[i].y() * TETRIS_SIZE - TETRIS_SIZE,
                             TETRIS_SIZE, TETRIS_SIZE);
            }

        }

    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {

        if ("board".equals(theEvent.getPropertyName())) {
            
            myPiece = (TetrisPiece) theEvent.getNewValue();
           
            myPieceColor = myPiece.getColor();
            repaint();
        }

    }
}
