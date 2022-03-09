/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Go");
        frame.setResizable(false);
        frame.setLocation(300, 300);
        
        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        
        GridLayout statLay = new GridLayout(0,3);
        
        final JLabel status = new JLabel("Running...");
        final JLabel blackState = new JLabel("Black: 0");
        final JLabel whiteState = new JLabel("White: 7");
        final JLabel errorStatus = new JLabel(InvalidMove.NONE.toString());
        status_panel.setLayout(statLay);
        status_panel.add(blackState);
        status_panel.add(status);
        status_panel.add(whiteState, BorderLayout.EAST);
        
        
        // Main playing area
        final GameCourt court = new GameCourt(status, blackState, whiteState, errorStatus);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel1 = new JPanel();
        final JPanel control_panel2 = new JPanel();
        final JPanel control_panel3 = new JPanel();
       // frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        GridLayout gridLay = new GridLayout(1,2);
        
        final JButton open = new JButton("Open");
        final JButton restart = new JButton("Restart");
        final JButton save = new JButton("Save");
        final JButton pass = new JButton("Pass");
        final JButton quit = new JButton("Quit");
        final JButton inst = new JButton("Instructions");
        
        open.addActionListener(e -> court.open());
        restart.addActionListener(e -> court.reset());
        save.addActionListener(e -> court.save());
        pass.addActionListener(e -> court.pass());
        quit.addActionListener(e -> System.exit(0));
        inst.addActionListener(e -> openInstructions());
        control_panel1.setLayout(gridLay);
        control_panel1.add(quit);
        control_panel1.add(restart);
        control_panel2.setLayout(gridLay);
        control_panel2.add(open);
        control_panel2.add(save);
        control_panel3.setLayout(gridLay);
        control_panel3.add(pass);
        control_panel3.add(inst);
        
        GridLayout topLay = new GridLayout(4,1);
        final JPanel top = new JPanel();
        top.setLayout(topLay);
        top.add(control_panel1);
        top.add(control_panel2);
        top.add(control_panel3);
        top.add(errorStatus);
        frame.add(top, BorderLayout.NORTH);
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    public void openInstructions() {
    	final JFrame frame2 = new JFrame("Instructions");
        frame2.setResizable(false);
        frame2.setLocation(300, 300);
        
        final JPanel inst_panel = new JPanel();
        frame2.add(inst_panel, BorderLayout.SOUTH);
        
        GridLayout gridLay = new GridLayout(17,1);
        
        final JLabel i0 = new JLabel(
        		"Go is a strategy board game for two players. This game of Go is played on\n");
        final JLabel i1 = new JLabel(" a 13x13 board. In each turn a player places a stone of their own color \n");
        final JLabel i2 = new JLabel( "(black or white) on the board. \n");
        final JLabel i3 = new JLabel( "The stones can be placed on an intersection on the board and a player can\n");
        final JLabel i4 = new JLabel( "play on empty intersections. Stones that have been placed cannot be removed\n");
        final JLabel i5 = new JLabel( "unless they are captured in which case the stone is taken off the board.\n");
        final JLabel i6 = new JLabel( "The 'liberties' of a stones are the number of empty intersections around \n");
        final JLabel i7 = new JLabel( "a stone. A single stone is captured if it has no liberties such that, ob all\n");
        final JLabel i8 = new JLabel( "four sides, it has been occupied by stones of the opposite color.");
        final JLabel i9 = new JLabel( "A group of stones of the same color can be captured together. If, for a group\n");
        final JLabel i10 = new JLabel( "of stones (where any stone of the group is adjacent to at least one more stone of \n");
        final JLabel i11 = new JLabel( "the group and all of them are of the same type), none of the stones have any\n");
        final JLabel i12 = new JLabel( "liberties left, then they are all captured. That is, a group of stones is captured\n");
        final JLabel i13 = new JLabel( "if for each stone, none of the four intersections around them are empty.");
        final JLabel i14 = new JLabel( "\n Players cannot make a suicide play. That is, they cannot play a stone that would\n");
        final JLabel i15 = new JLabel( "result in the immediate removal of any of their own stones.\n");
        final JLabel i16 = new JLabel( "Players can pass. If the two players pass consecutively, the game is concluded.");
        		
        inst_panel.setLayout(gridLay);
        
        inst_panel.add(i0);
        inst_panel.add(i1);
        inst_panel.add(i2);
        inst_panel.add(i3);
        inst_panel.add(i4);
        inst_panel.add(i5);
        inst_panel.add(i6);
        inst_panel.add(i7);
        inst_panel.add(i8);
        inst_panel.add(i9);
        inst_panel.add(i10);
        inst_panel.add(i11);
        inst_panel.add(i12);
        inst_panel.add(i13);
        inst_panel.add(i14);
        inst_panel.add(i15);
        inst_panel.add(i16);
        frame2.pack();
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setVisible(true);

        // Start game
        
    }
    
    
    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}