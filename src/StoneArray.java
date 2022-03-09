/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, April 2017
 */

import java.awt.*;
import java.util.*;


/**
 * Encapsulates the 13x13 array of stones. GameCourt accesses StoneArray, individual Stones 
 * are not accessed, GameCourt.PaintComponent calls on the drawStones method of this class 
 * to draw individual stones
 */
public class StoneArray{ //part of the model, not the view -- is not visible, contains stone array info

	private Stone[][] stones; //CONCEPT : implementation of 2D arrays that keeps a 2d array of stone objects
	
	private int courtWidth, courtHeight;
	private GameCourt gameCourt;
	
    public StoneArray(int courtWidth, int courtHeight, int topLeftX, int topLeftY, int width, GameCourt gameCourt) { 
        stones = new Stone[13][13];
        
        for (int i = 0; i < stones.length; i++) {
        	for (int j = 0; j < stones[0].length; j++) {
				stones[i][j] = new Stone(courtWidth, courtHeight, topLeftX + width*i, topLeftY + width*j,
        							StoneState.EMPTY);
			}
        }
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
        this.gameCourt = gameCourt;
    }	

    public void drawStones(Graphics g) {
    	for (Stone[] stoneLine : stones) {
    		for (Stone stone : stoneLine) {
    			stone.draw(g);
    		}
    	}
    }

    public Point getNearestStone (Point point) {
    	Point p = new Point(0,0);
    	double minDist = 10000.0;
    	for (int i = 0; i < stones.length; i++) {
    		for (int j = 0; j < stones[0].length; j++) {
    			double dist = Math.sqrt(((stones[i][j].getPx() - point.getX())*(stones[i][j].getPx() - point.getX())
    					+ (stones[i][j].getPy() - point.getY())*(stones[i][j].getPy() - point.getY())));
    			
    			p = minDist > dist ? new Point(i, j) : p;
    			minDist = Math.min(minDist, dist);
    		}
    	}
    	
    	return minDist > 8 ? null : p;
    }
    
    
    public StoneArray produceDuplicate () {
    	StoneArray dupl = new StoneArray(courtWidth, courtHeight, stones[0][0].getPx(),
    			stones[0][0].getPy(), stones[0][0].getWidth(), gameCourt);
    	for (int i = 0; i < stones.length; i++) {
        	for (int j = 0; j < stones[0].length; j++) {
				dupl.setStoneState(new Point(i,j), stones[i][j].getStoneState()); 
			}
        }
		return dupl;
    }
    
    /**
     * This method determines whether a stone to be placed at a point is a suicide or not. That is,
     * it determines whether all points surrounding the group of stones of the same color as this stone
     * (a group which may only include this new stone) are non-empty or not. If any of the points are
     * empty, then the stone isn't surrounded. Otherwise it is. This is achieved through a recursive
     * algorithm.
     */
    public boolean isSurrounded (Point p, Player playerType) {
    	//A set object that keeps track of already viewed stones so that the recursive algorithm doesn't
    	//continuously call on the same stones
    	//A set is chosen as the order of Stones is irrelevant and duplicate (pointer) elements are not 
    	//desired.
    	//CONCEPT: Implements the concept of Collections
    	Set<Point> viewedStones = new HashSet<Point>();
    	StoneArray customArray = this.produceDuplicate();
    	customArray.setStoneState(p, playerType.toStoneState());
    	return !recursiveLookup(p, customArray, playerType.toStoneState(), viewedStones);
    }
    //NOTE: isSurrounded determines whether an empty point, if a stone where to be placed there,
    //would be surrounded, isStoneSurrounded determines whether an already placed stone is surrounded
    /**
     * CONCEPT: Collections
     * This method determines whether a stone at a point is surrounded or not. That is,
     * it determines whether all points surrounding the group of stones of the same color as this stone
     * (a group which may only include this new stone) are non-empty or not. If any of the points are
     * empty, then the stone isn't surrounded. Otherwise it is. This is achieved through a recursive
     * algorithm.
     */
    public boolean isStoneSurrounded (Point p, StoneState stoneState) {
    	//A set object that keeps track of already viewed stones so that the recursive algorithm doesn't
    	//continuously call on the same stones
    	//A set is chosen as the order of Stones is irrelevant and duplicate (pointer) elements are not 
    	//desired.
    	//CONCEPT: Implements the concept of Collections
    	Set<Point> viewedStones = new HashSet<Point>();
    	return !recursiveLookup(p, this, stoneState, viewedStones);
    }
    
    /**
     * CONCEPT: RECURSION
     * Checks whether the current stone has an EMPTY intersection one one of its four sides, then 
     * recurses through all stones surrounding it of the same stoneType to check the same property.
     * This method takes in a custom stoneArray object that is the same as the displayed stoneArray 
     * except for the added new stone.  
     * Recursion is the most efficient method of implementing this functionality as the structure of the
     * group the new stone is part of is unknown and arbitrary, making it troublesome to traverse with
     * alternatives to recursion like a for-loop.
     * This method also passes on a  Set<Point> viewedStones (CONCEPT: collections) object which is a set
     * object that keeps track of the stones that have already been viewed so that the recursive algorithm
     * does not continuously call on the same stones. 
     */
    private static boolean recursiveLookup (Point p, StoneArray stoneArray, StoneState originalState, Set<Point> viewedStones) {
    	boolean intersectionExists = false;
    	viewedStones.add(p);	
    	
    	//Check recurse on the stone above if it exists
    	Point above = new Point((int) p.getX(), (int) p.getY() - 1);
    	if(validPos(above) && stoneArray.getStoneState(above) == StoneState.EMPTY){
    		return true;
    	}
    	else if (validPos(above) && stoneArray.getStoneState(above) == originalState && !viewedStones.contains(above)){
    		intersectionExists |= recursiveLookup(above, stoneArray, originalState, viewedStones);
    	}
    	
    	//Check recurse on the stone below if it exists
    	Point below = new Point((int) p.getX(), (int) p.getY() + 1);
    	if(validPos(below) && stoneArray.getStoneState(below) == StoneState.EMPTY){
    		return true;
    	}
    	else if (validPos(below) && stoneArray.getStoneState(below) == originalState && !viewedStones.contains(below)){
    		intersectionExists |= recursiveLookup(below, stoneArray, originalState, viewedStones);
    	}
    	
    	//Check recurse on the stone to the left if it exists
    	Point left = new Point((int) p.getX() + 1, (int) p.getY());
    	if(validPos(left) && stoneArray.getStoneState(left) == StoneState.EMPTY){
    		return true;
    	}
    	else if (validPos(left) && stoneArray.getStoneState(left) == originalState && !viewedStones.contains(left)){
    		intersectionExists |= recursiveLookup(left, stoneArray, originalState, viewedStones);
    	}
    	
    	//Check recurse on the stone to the right if it exists
    	Point right = new Point((int) p.getX() - 1, (int) p.getY());
    	if(validPos(right) && stoneArray.getStoneState(right) == StoneState.EMPTY){
    		return true;
    	}
    	else if (validPos(right) && stoneArray.getStoneState(right) == originalState && !viewedStones.contains(right)){
    		intersectionExists |= recursiveLookup(right, stoneArray, originalState, viewedStones);
    	}
    	
    	return intersectionExists;
    }
    
    /**
     * Gets the set of points that are part of the group that the stone is. 
     * @param p
     * @param stoneArray
     * @return
     */
    public Set<Point> getGroup(Point p){
    	//A set is chosen as the order of Stones is irrelevant and duplicate (pointer) elements are not 
    	//desired.
    	//CONCEPT: Implements the concept of Collections
    	Set<Point> viewedStones = new HashSet<Point>();
    	recursiveLookup2(p, this, this.getStoneState(p), viewedStones);
    	return viewedStones;
    }
    
    /**
     * Adds the current stone to the Set of viewedStones and recurses through stones on fours sides of same type 
     */
    private static void recursiveLookup2(Point p, StoneArray stoneArray, StoneState originalState, Set<Point> viewedStones) {
    	viewedStones.add(p);
    	
    	
    	//Check recurse on the stone above if it exists
    	Point above = new Point((int) p.getX(), (int) p.getY() - 1);
    	if (validPos(above) && stoneArray.getStoneState(above) == originalState && !viewedStones.contains(above)){
    		 recursiveLookup2(above, stoneArray, originalState, viewedStones);
    	}
    	
    	//Check recurse on the stone below if it exists
    	Point below = new Point((int) p.getX(), (int) p.getY() + 1);
    	if (validPos(below) && stoneArray.getStoneState(below) == originalState && !viewedStones.contains(below)){
    		recursiveLookup2(below, stoneArray, originalState, viewedStones);
    	}
    	
    	//Check recurse on the stone to the left if it exists
    	Point left = new Point((int) p.getX() + 1, (int) p.getY());
    	if (validPos(left) && stoneArray.getStoneState(left) == originalState && !viewedStones.contains(left)){
    		recursiveLookup2(left, stoneArray, originalState, viewedStones);
    	}
    	
    	//Check recurse on the stone to the right if it exists
    	Point right = new Point((int) p.getX() - 1, (int) p.getY());
    	if (validPos(right) && stoneArray.getStoneState(right) == originalState && !viewedStones.contains(right)){
    		recursiveLookup2(right, stoneArray, originalState, viewedStones);
    	}

    }
    
    public static boolean validPos(Point point) {
    	return point.getX() < 13 && point.getX() >= 0 && point.getY() < 13 && point.getY() >= 0;
    }
    
    public StoneState getStoneState(Point point) {
    	return validPos(point) ? stones[(int) point.getX()][(int) point.getY()].getStoneState() : null ;
    }
    
    public void setStoneState(Point point, StoneState state) {
    	if(validPos(point)) {
    		stones[(int) point.getX()][(int) point.getY()].set(state);
    	}
    }
    
    /**
    * Checks whether the groups of stones around the stone of the opposite type should be removed or not.
    * The stones are kept in a Set object and if they should be removed, the removeStones method removes them.
    */
   public static void checkForRemoval(Point p, StoneState state, StoneArray stoneArray, GameCourt gameCourt) {
   	
   	Point above = new Point((int) p.getX(), (int) p.getY() - 1);
   	if (StoneArray.validPos(above) && stoneArray.getStoneState(above) == state.other() && stoneArray.isStoneSurrounded(above, state.other())){
   		 removeStones(stoneArray.getGroup(above), stoneArray, gameCourt);
   	}
   	
   	Point below = new Point((int) p.getX(), (int) p.getY() + 1);
   	if (StoneArray.validPos(below) && stoneArray.getStoneState(below) == state.other() && stoneArray.isStoneSurrounded(below, state.other())){
   		 removeStones(stoneArray.getGroup(below), stoneArray, gameCourt);
   	}
   	
   	Point left = new Point((int) p.getX() - 1, (int) p.getY());
   	if (StoneArray.validPos(left) && stoneArray.getStoneState(left) == state.other() && stoneArray.isStoneSurrounded(left, state.other())){
   		 removeStones(stoneArray.getGroup(left), stoneArray, gameCourt);
   	}
   	
   	Point right = new Point((int) p.getX() + 1, (int) p.getY());
   	if (StoneArray.validPos(right) && stoneArray.getStoneState(right) == state.other() && stoneArray.isStoneSurrounded(right, state.other())){
   		 removeStones(stoneArray.getGroup(right), stoneArray, gameCourt);
   	}
   	
   }
   
   /**
    * This method changes the stone at the point to EMPTY if it is a valid point and is not already EMPTY.
    * It also increases by 1 the points of the player that is of opposite color to the stone.
    */
   private static void removeStone(Point p, StoneArray stoneArray, GameCourt gameCourt) {
   	if (StoneArray.validPos(p)) {
   		switch(stoneArray.getStoneState(p)) {
   			case BLACK:
   				gameCourt.increasePoints(Player.WHITE);;
   				stoneArray.setStoneState(p, StoneState.EMPTY);
   				break;
   			case WHITE:
   				gameCourt.increasePoints(Player.BLACK);
   				stoneArray.setStoneState(p, StoneState.EMPTY);
   				break;
   			default:
   				break;
   		}
   	}
   }
   
   /**
    * Removes the stones in the set from the stoneArray by executing removeStone
    * @param ps
    * @param stoneArray
    */
   private static void removeStones(Set<Point> ps, StoneArray stoneArray, GameCourt gameCourt) {

   	for (Point p : ps) {
   		removeStone(p, stoneArray, gameCourt);
   	}
   } 

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + courtHeight;
	result = prime * result + courtWidth;
	result = prime * result + Arrays.deepHashCode(stones);
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	StoneArray other = (StoneArray) obj;
	if (courtHeight != other.courtHeight)
		return false;
	if (courtWidth != other.courtWidth)
		return false;
	if (!Arrays.deepEquals(stones, other.stones))
		return false;
	return true;
}

}