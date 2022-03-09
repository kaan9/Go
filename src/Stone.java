/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;


/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Stone extends GameObj {
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Stone other = (Stone) obj;
		if (state != other.state)
			return false;
		return true;
	}

	public static final int SIZE = 20;

    private StoneState state;
    
    public Stone(int courtWidth, int courtHeight, int posX, int posY, StoneState stoneState) {
        super(posX, posY, SIZE, SIZE, courtWidth, courtHeight);
        
        this.state = stoneState;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor()); 
        if (state != StoneState.EMPTY) { 
        	g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        }
    }
    
    public void set(StoneState stoneState) {
    	state = stoneState;
    }
    
    private Color getColor() { 
    	switch(state) {
    		case BLACK:
    			return new Color(0,0,0,255);
    			
    		case WHITE:
    			return new Color(255,255,255,255);
    			
    		default:
    			return new Color(0,0,0,0);
    			
    	}
    
    }

    public StoneState getStoneState() {
    	return state;
    }
    
}