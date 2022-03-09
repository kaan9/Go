/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.util.List;


import javax.swing.*;
import javax.swing.Timer;

import java.io.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
	private GameBoard gameBoard; //gameBoard background, static 
    private StoneArray stones;
    
    public boolean playing = false; // whether the game is running 
    private Player currentPlayer = Player.BLACK;
    private InvalidMove currentError = InvalidMove.NONE;
    private int blackPoints = 0;
    private int whitePoints = 7;
    
    private JLabel status; // Current status text, i.e. "Running..."
    private JLabel errorStatus; // Any error associated with attempted move is displayed
    private JLabel blackState; // Current points of Black
    private JLabel whiteState; // Current points of White
    
    private StoneArray previousStones;
    private StoneArray previouspreviousStones;
    
    public static final String TXT_FILE = "files/savedGame.txt";

    private boolean previousPassed = false; //Did the previous player pass?
    
    // Game constants
    public static final int COURT_WIDTH = 316;
    public static final int COURT_HEIGHT = 316;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status, JLabel blackState, JLabel whiteState, JLabel errorStatus) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!     
        
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
               if(stones != null) {
            	   stoneClicked(stones.getNearestStone(new Point(Math.abs(e.getX() - 10), Math.abs(e.getY() - 10))));
               }
            }
        
        });
        
        this.status = status;
        this.blackState = blackState;
        this.whiteState = whiteState;
        this.errorStatus = errorStatus;
        
        
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	gameBoard = new GameBoard(COURT_WIDTH, COURT_HEIGHT);
        stones = new StoneArray(COURT_WIDTH, COURT_HEIGHT, 3, 3, 24, this); //topLeftX = 3, topLeftY = 3, width = 24
        currentPlayer = Player.BLACK;
        
        blackPoints = 0;
        whitePoints = 7;
        
        playing = true;
        status.setText("Black's turn");
    	currentError = InvalidMove.NONE;
        
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        
        List<StoneArray> stonesList = new LinkedList<StoneArray>();
        stonesList.add(stones.produceDuplicate());
    }
    
    private List<String> readLines() {
    	List<String> lines = new LinkedList<String>();
    	BufferedReader reader;
    	try {
    		reader = new BufferedReader(new FileReader(TXT_FILE));
    		String line = reader.readLine();
    		while (line != null) {
    			lines.add(line);
    			line = reader.readLine();
    		}
    	} catch (IOException e) {
    		currentError = InvalidMove.IERROR;
    	}
    	return lines;
    }
    
    private void writeLines(List<String> lines){
    	FileWriter writer = null;
    	try {
    		writer = new FileWriter(TXT_FILE);
    		for(String s : lines) {
    			writer.write(s + "\n");
    		}
    		
    	} catch (IOException e) {
    		currentError = InvalidMove.OERROR;
    	}
    	finally {
    		try {
				writer.close();
			} catch (IOException e) {
				currentError = InvalidMove.OERROR;
			} catch (NullPointerException e) {
				currentError = InvalidMove.OERROR;
			}
    	}
    }
    
    public void open() {
    	if (currentError.equals(InvalidMove.IWARNING)) {
    		currentError = InvalidMove.NONE;
        	List<String> readLines = readLines();
        	
        	if(currentError != InvalidMove.IERROR && readLines.size() == 16) {
        		
        		try {
        			int blackPnts = Integer.parseInt(readLines.get(0));
        			int whitePnts = Integer.parseInt(readLines.get(1));
        			StoneArray newStoneArray = new StoneArray(COURT_WIDTH, COURT_HEIGHT, 3, 3, 24, this);
        			for(int i = 2; i < 15; i++) {
        				if(readLines.get(i).length() == 13) {
        					String[] positions = new String[13];
        					positions = readLines.get(i).split("");
        					for(int j = 0; j < 13; j++) {
        						if (!positions[j].equals("0") && !positions[j].equals("1") && !positions[j].equals("2")) {
        							throw new NumberFormatException();
        						}
        						newStoneArray.setStoneState(new Point(j,i-2), positions[j].equals("0") ? StoneState.EMPTY :
        								(positions[j].equals("1") ? StoneState.WHITE : StoneState.BLACK));
        					}
        				}
        				else {
        					throw new NumberFormatException();
        				}
        			}
        			
        			int openedPlayer =  Integer.parseInt(readLines.get(15));
        			if((openedPlayer != 1 && openedPlayer != 2) || (blackPnts < 0) || (whitePnts < 7)) {
        				throw new NumberFormatException();
        			}    	
        			
        			reset();
        			stones = newStoneArray;
        			currentPlayer = (openedPlayer == 1 ? Player.WHITE : Player.BLACK);
        			blackPoints = blackPnts;
        			whitePoints = whitePnts;
        		}
        		catch (NumberFormatException e){
        			currentError = InvalidMove.IERROR;
        		}
        	}
        	else {
        		currentError = InvalidMove.IERROR;
        	}
    	}
    	else {
    		currentError = InvalidMove.IWARNING;
    	}
    }
    
    public void save() {
    	if (currentError.equals(InvalidMove.OWARNING)) {
    		currentError = InvalidMove.NONE;
        	List<String> printLines = new LinkedList<String>();
        	printLines.add((new Integer(blackPoints)).toString());
        	printLines.add((new Integer(whitePoints)).toString());
        	for (int i = 0; i < 13; i++) {
        		String line = "";
        		for (int j = 0; j < 13; j++) {
        			line += stones.getStoneState(new Point(j,i)).toNumString();
        		}
        		printLines.add(line);
        	}
        	printLines.add("" + (currentPlayer == Player.WHITE ? 1 : 2));
        	writeLines(printLines);
    	}
    	else {
    		currentError = InvalidMove.OWARNING;
    	}
    }
    
    public void pass() {
    	currentError = InvalidMove.NONE;
    	currentPlayer = currentPlayer.other();
    	if (previousPassed) {
    		terminateGame();
    	}
    	else {
    		previousPassed = true;
    	}
    }
    
    private void stoneClicked (Point point) {
    	if (point != null) {
    		currentError = isMoveValid(point);
        	if(currentError == InvalidMove.NONE) {
        		previouspreviousStones = previousStones != null ? previousStones.produceDuplicate() : null;
        		previousStones = stones.produceDuplicate();
        		stones.setStoneState(point, currentPlayer.toStoneState());
        		StoneArray.checkForRemoval(point, stones.getStoneState(point), stones, this);
        		
        		currentPlayer = currentPlayer.other();
        		previousPassed = false;
        	}
        	else if (currentError == InvalidMove.SUICIDE) {
        		StoneArray customArray = stones.produceDuplicate();
        		customArray.setStoneState(point, currentPlayer.toStoneState());
        		
        		StoneArray checkArray = customArray.produceDuplicate();
        		
        		GameCourt fakeCourt = new GameCourt(new JLabel(""), new JLabel(""), new JLabel(""), new JLabel(""));
        		
        		StoneArray.checkForRemoval(point, customArray.getStoneState(point), customArray, fakeCourt);
        		
        		if(!customArray.equals(checkArray)) {
        			previouspreviousStones = previousStones != null ? previousStones.produceDuplicate() : null;
        			previousStones = stones.produceDuplicate();
        			stones.setStoneState(point, currentPlayer.toStoneState());
            		StoneArray.checkForRemoval(point, stones.getStoneState(point), stones, this);

            		currentPlayer = currentPlayer.other();
            		currentError = InvalidMove.NONE;
            		previousPassed = false;
        		}
        	}
    	}
    }
    
    private InvalidMove isMoveValid(Point p) {
    	if (stones.getStoneState(p) != StoneState.EMPTY) {
    		return InvalidMove.NONEMPTY_POINT;
    	}
    	else if (stones.isSurrounded(p, currentPlayer)) { //implements the suicide rule
    		return InvalidMove.SUICIDE;
    	}
    	else if (previouspreviousStones != null) { //implements the KO rule
    		StoneArray customArray = stones.produceDuplicate();
    		customArray.setStoneState(p, currentPlayer.toStoneState());
    		StoneArray.checkForRemoval(p, currentPlayer.toStoneState(), customArray, 
    				new GameCourt(new JLabel(), new JLabel(), new JLabel(), new JLabel()));
    		if(customArray.equals(previousStones)) {
    			return InvalidMove.KO;
    		}    		
    	}
    	return InvalidMove.NONE;
    }
    
    
     
    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            
            // check for the game end conditions
            blackState.setText("Black: " + blackPoints);
            whiteState.setText( "White: " + (whitePoints + 0.5));
            
            status.setText(currentPlayer.toString() + "'s turn");
            errorStatus.setText(currentError.toString());
            
            // update the display
            repaint();
        }
    }

    
    private void terminateGame() {
    	previousPassed = false;
    	playing = false;
    	status.setText((blackPoints > whitePoints ? "Black Wins" : "White Wins"));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameBoard.draw(g);
        stones.drawStones(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    public void increasePoints(Player player) {
    	switch(player) {
    		case BLACK:
    			blackPoints++;
    			break;
    		case WHITE:
    			whitePoints++;
    			break;
    		default:
    			break;
    	}
    }
    
}