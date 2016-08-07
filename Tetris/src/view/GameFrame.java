/*
 * Mohamed Dahir.
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The main frame of the game.
 * 
 * @author n4tunec@uw.edu.
 * @version winter 2015
 *
 */
public class GameFrame {
    /** The dimension of my frame. */
    private static final Dimension MY_FRAME_SIZE = new Dimension(500, 600);

    /** The dimension of my frame. */
    private static final Dimension MY_GAMEPANEL_SIZE = new Dimension(280, 600);

    /** The dimension of my information panel. */
    private static final Dimension MY_INFOPANEL_SIZE = new Dimension(200, 550);
    
    /** Next level starts with 10 points. */
    private static final int NEW_LEVEL_SCORE = 10;
    
    /** The Timer pause. */
    private static final int PAUSE = 500;

    /** The main frame. */
    private final JFrame myFrame;

    /** The game panel. */
    private final BoardPanel myPanel;

    /** The informations panel. */
    private final JPanel myInfoPanel;

    /** The nextPiece Panel. */
    private final NextPiecePanel myNextPiecePanel;

    /** The game Timer. */
    private final Timer myTimer;

    /** The Statistics PAnel. */
    private final StatisticsPanel myStatsPanel;
    /**The next level score benchmark.*/
    private int myNewLevelScore;
    /**The users current level.*/
    private int myLevel;
    /**The new game menu item.*/
    private JMenuItem myNewGame;
    
   
    
    /**
     * Constructor initializes all fields.
     */
    public GameFrame() {

        // set up frame
        myFrame = new JFrame("Mohamed Dahir's Tetris");
        myFrame.setIconImage(new ImageIcon("./images/tetris-lamp.jpg").getImage());
        
        myNextPiecePanel = new NextPiecePanel();

        myTimer = new Timer(PAUSE, new TimerListener());

        // set up board panel
        myPanel = new BoardPanel();

        myInfoPanel = new JPanel();
        myStatsPanel = new StatisticsPanel();
        setUpControlsDialog();
    }

    /**
     * This displays the game controls to the user before the game starts.
     */
    private void setUpControlsDialog() {
        // setUp controls Dialog
        JOptionPane.showMessageDialog(null,
                                      "Key ----- function\n"
                                       + "'A' key - move left\n'D' key - move right\n"
                                       + "'S' key - move down \n'SPACE' key - drop\n"
                                       + "'LEFT' key - rotateCCW \n'RIGHT' key - rotateCW\n"
                                       + "'P' key -- pause/play \n ",
                                      "Game Controls", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Start loading up components of the GUI.
     */
    public void start() {

        myFrame.setSize(MY_FRAME_SIZE);
        myFrame.setResizable(false);
        myFrame.setLocationRelativeTo(null);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        myPanel.setPreferredSize(MY_GAMEPANEL_SIZE);
        myPanel.addPropertyChangeListener(myNextPiecePanel);

        myInfoPanel.setPreferredSize(MY_INFOPANEL_SIZE);
        myInfoPanel.setBackground(Color.GRAY);

        myInfoPanel.add(myNextPiecePanel);
        myInfoPanel.add(myStatsPanel);

        myFrame.addKeyListener(new KeyEventHandler());
        myFrame.add(myInfoPanel, BorderLayout.EAST);
        myFrame.add(myPanel, BorderLayout.WEST);
        myFrame.add(new JPanel());
        
        final File[] soundFiles = new File[1];
        soundFiles[0] = new File("./music/Dubstep-tetris.wav");
        

        myFrame.setJMenuBar(createMenuBar());
        myFrame.setVisible(true);
        myTimer.start();
    }

    /**
     * Creates the menu bar for the GUI.
     * @return the menubar created.
     */
    public JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu fileMenu = new JMenu("File...");
        fileMenu.setMnemonic('F');
        fileMenu.add(createmyNewGameItem());
        fileMenu.add(createExitGame());
        
        final JMenuItem exitMenuItem = new JMenuItem("Exit...");
        exitMenuItem.setMnemonic('x');
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myTimer.stop();
                myFrame.dispose();
            }
           
        });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(createOptionsMenu());
        menuBar.add(createInfoMenu());
        return menuBar;
    }
    
    /**
     * creates the infotmation menu.
     * @return the menu creeated.
     */
    public JMenu createInfoMenu() {
        final JMenu menu = new JMenu("Info...");
        menu.setMnemonic('I');
        final JMenuItem about = new JMenuItem("About...");
        about.setMnemonic('A');
        
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                pauseGame();
                
                JOptionPane.showMessageDialog(null,
                                              "Icon Gotton from: www.laserlemming.com\n "
                                              + "Sound effects courtsey of : http:"
                                              + "//free-sounds.net/special-sound-effects.htm\n"
                                              + "Back ground music = https://soundcloud.com/"
                                             + "north-pole-twin/tetris-theme-dubstep-remix-by",
                                              "About Game", JOptionPane.INFORMATION_MESSAGE);
                
                pauseGame();
            }
            
            
        });
        
        final JMenuItem help = new JMenuItem("Help...");
        help.setMnemonic('H');
        
        help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                pauseGame();
                
                JOptionPane.showMessageDialog(null,
                                              "Every line cleared increases your level by 1.\n"
                                          + "Every line cleared increase the speed of the .\n"
                                         + "pieces Every line cleared is equals to 10 points.",
                                              "Instructions", JOptionPane.INFORMATION_MESSAGE);
                
                pauseGame();
            }
            
            
        });
        menu.add(help);
        menu.add(about);
        return menu;
    }
    
    /**Pauses and unpauses the game.*/
    public void pauseGame() {
        myPanel.pause();
        if (myTimer.isRunning()) {
            myTimer.stop();
        } else {
            myTimer.start();
        }
     
    }
    
    
    /**
     * Creates the new game menu item.
     * @return the created menu item.
     */
    public JMenuItem createmyNewGameItem() {
        myNewGame = new JMenuItem("New Game..");
        myNewGame.setMnemonic('N');
        myNewGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myLevel = 1;
                myNewLevelScore = NEW_LEVEL_SCORE;
                myPanel.setEndGame(false);
                myPanel.restart();
                myTimer.restart();
                myTimer.setDelay(PAUSE);
            }
            
        });
        myNewGame.setEnabled(false);
        return myNewGame;
    }
    
    /**
     * Creates the exit menuitem.
     * @return the created menu item.
     */
    public JMenuItem createExitGame() {
        final JMenuItem menuItem = new JMenuItem("End Game..");
        menuItem.setMnemonic('E');
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myPanel.setEndGame(true);
                myNewGame.setEnabled(true);
                myTimer.stop();
                
            }
        });
        
        return menuItem;
        
    }

    /**
     * Creates the options menu on the menu bar.
     * 
     * @return the options menu.
     */
    public JMenu createOptionsMenu() {
        final JMenu options = new JMenu("Options...");
        final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem("Grid");
        checkbox.setMnemonic('G');
        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myPanel.setGridSelected(checkbox.isSelected());
                myPanel.repaint();
            }
        });
        
        
        final JCheckBoxMenuItem muteCheckbox = new JCheckBoxMenuItem("Mute");
        muteCheckbox.setMnemonic('M');
        muteCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                if (muteCheckbox.isSelected()) {
                } else if (!myPanel.isPause()) {
                }
            } 
        });
        options.add(muteCheckbox);
        options.add(checkbox);
        return options;
    }

    /**
     * The panel displays the users game statistics.
     * 
     * @author n4tunec@uw.edu.
     * @version winter 2015.
     *
     */
    public class StatisticsPanel extends JPanel {

        /**
         * serialversion UID.
         */
        private static final long serialVersionUID = 2609660940661193100L;

        /** The game over label font size. */
        private static final int FONT_SIZE = 16;
        
        /** The score y co-ordinates. */
        private static final int SCORE_Y = 30;
        
        /** The minimum time delay. */
        private static final int MIN_DELAY = 100;
        
        /**The lines cleared to score ratio.*/
        private static final int LINE_TO_SCORE_RATIO = 10;
        
        /** The dimension of the nextPiece panel. */
        private final Dimension mySize = new Dimension(180, 200);
        
        

        /** Default constructor initializes the variables. */
        public StatisticsPanel() {
            super();
            setPreferredSize(mySize);
            setBackground(Color.CYAN);
            myNewLevelScore = NEW_LEVEL_SCORE;
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

            g2d.setFont(new Font("Comic Sans ms", Font.BOLD, FONT_SIZE));
            g2d.setColor(Color.WHITE);
            g2d.drawString(gameClearedLines(), 0, SCORE_Y);
            g2d.drawString("Level : " + getLevel(), 0, 2 * SCORE_Y);
            g2d.drawString("Score : " + getScore(), 0, SCORE_Y + SCORE_Y + SCORE_Y);

            setBackground(Color.BLACK);
        }
        /**
         * Returns the cleared lines.
         * @return the cleared lines in a strng format.
         */
        public String gameClearedLines() {

            final StringBuilder sb = new StringBuilder("Cleared Lines : ");
            
            sb.append(getCompletedLines());
            return sb.toString();
        }

        /**
         * Gets the number of lines cleared.
         * 
         * @return the completed lines in string format.
         */
        public String getCompletedLines() {

            return String.valueOf(myPanel.getCompletedLines());
        }
        
        /**
         * Thos returs the user's score in a string format.
         * @return the users score.
         */
        public String getScore() {
            final int score = myPanel.getCompletedLines() * LINE_TO_SCORE_RATIO;

            if (score >= myNewLevelScore) {
                if (myTimer.getDelay() > MIN_DELAY) {
                    myTimer.setDelay(myTimer.getDelay() - MIN_DELAY);
                    System.out.println(myTimer.getDelay());
                    
                    myNewLevelScore += NEW_LEVEL_SCORE;
                    myLevel++;
                }
            }
            return String.valueOf(score);
        }
        
        /**
         * This gets the current players level.
         * @return the current players level in string format.
         */
        public String getLevel() {
            return String.valueOf(myLevel);
        }
    }

    /**
     * An Action Listener for the timer.
     * 
     * @author n4tunec@uw.edu.
     * @version winter 2015.
     *
     */
    private class TimerListener implements ActionListener {
        /** Default constructor for the listener. */
        protected TimerListener() {
            // do nothing.
        }

        /**
         * This moves the object with the timer.
         * @param theEvent this is the event fired.
         */
        @Override
        public void actionPerformed(final ActionEvent theEvent) {

            myPanel.moveObjects();
            myPanel.fireChangeListsner();
            myStatsPanel.repaint();
            
//            if (!myMusicPlayer.isStarted()) {
//                myMusicPlayer.restart();
//            }
            
            if (myPanel.isGameOver()) {
                myTimer.stop();
            }

        }
    }

    /**
     * This handles the key events from the user.
     * 
     * @author n4tunec@uw.edu
     * @version winter 2015.
     *
     */
    public class KeyEventHandler extends KeyAdapter {

        /**
         * Default constructor.
         */
        protected KeyEventHandler() {
            super();
            // do nothing
        }

        /**
         * Takes care of the board movement.
         * 
         * @param theEvent the key event.
         */
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            switch (theEvent.getKeyCode()) {
                case KeyEvent.VK_D:
                    if (myTimer.isRunning()) {
                        myPanel.moveRight();
                    }
                    
                    break;

                case KeyEvent.VK_A:

                    if (myTimer.isRunning()) {
                        myPanel.moveLeft();
                    }

                    break;

                case KeyEvent.VK_S:
                    if (myTimer.isRunning()) {
                        myPanel.moveDown();
                    }

                    break;

                case KeyEvent.VK_LEFT:
                    if (myTimer.isRunning()) {
                        myPanel.moveRotateLeft();
                    }

                    break;

                case KeyEvent.VK_RIGHT:
                    if (myTimer.isRunning()) {
                        myPanel.moveRotateRight();
                    }

                    break;

                case KeyEvent.VK_SPACE:
                    if (myTimer.isRunning()) {
                        myPanel.drop();
                    }
                    break;

                case KeyEvent.VK_P:
                    pauseGame();
                    break;
                default:
                    break;
            }
        }
    }
}
