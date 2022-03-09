/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

public enum StoneState {
    WHITE, BLACK, EMPTY;
	
	@Override
	public String toString() {
		switch(this) {
			case WHITE:
				return "White";
			case BLACK:
				return "Black";
			default:
				return "Empty";
		
		}
	}
	
	public int toNumString() {
		switch(this) {
			case WHITE:
				return 1;
			case BLACK:
				return 2;
			default:
				return 0;
		
		}
	}
	
	public StoneState other() {
		switch(this) {
		case WHITE:
			return BLACK;
		case BLACK:
			return WHITE;
		case EMPTY:
			return EMPTY;
		default:
			return null; 
		}
	}
	
}
