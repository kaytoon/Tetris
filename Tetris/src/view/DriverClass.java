/*
 * Mohamed Dahir
 */

package view;


/**
 * This kicks start the GUI.
 * 
 * @author n4tunec@uw.edu.
 * @version winter 2015.
 * */
public final class DriverClass {

    /** Private constructor. */
    private DriverClass() {
        // do nothing
    }

    /**
     * The main method creates a new Game frame.
     * 
     * @param theArgs the arguments passed.
     */
    public static void main(final String[] theArgs) {

        final GameFrame game = new GameFrame();
        game.start();
    }

}
