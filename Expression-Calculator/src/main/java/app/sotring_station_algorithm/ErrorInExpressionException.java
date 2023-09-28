package app.sotring_station_algorithm;

public class ErrorInExpressionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ErrorInExpressionException(String message) {
		
		super("Error in the expression:" + message + ".");
		
	}

}
