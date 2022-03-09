/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

public enum Player{
	WHITE, BLACK;
	
	@Override
	public String toString() {
		switch(this) {
			case WHITE:
				return "White";
			case BLACK:
				return "Black";
			default:
				return null;
		
		}
	}
	
	public StoneState toStoneState() {
		switch(this) {
			case WHITE:
				return StoneState.WHITE;
			case BLACK:
				return StoneState.BLACK;
			default:
				return null; 
		}
	}
	
	public Player other() {
		switch(this) {
		case WHITE:
			return BLACK;
		case BLACK:
			return WHITE;
		default:
			return null; 
		}
	}
	
}