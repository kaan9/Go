/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

public enum InvalidMove{
	SUICIDE, KO, NONEMPTY_POINT, NONE, IERROR, OERROR, IWARNING, OWARNING;
	
	@Override
	public String toString() {
		switch(this) {
			case SUICIDE:
				return "Invalid Move: Suicide";
			case KO:
				return "Invalid Move: Ko Rule";
			case NONEMPTY_POINT:
				return "Invalid Move: Point Already Full";
			case NONE:
				return "";
			case IERROR:
				return "File Open Failed: Invalid File/Format";
			case OERROR:
				return "File Save Failed";
			case OWARNING:
				return "Current game will be lost, press again to confirm";
			case IWARNING:
				return "Saved game will be lost, press again to confirm";
			default:
				return null;
		
		}
	}
	
}