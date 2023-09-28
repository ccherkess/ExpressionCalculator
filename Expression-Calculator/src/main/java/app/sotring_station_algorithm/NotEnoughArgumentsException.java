package app.sotring_station_algorithm;

public class NotEnoughArgumentsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotEnoughArgumentsException(int count) {
		
		super("There are not enough " + count + " arguments to perform the operation");
		
	}

}
