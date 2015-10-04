package sg.edu.astar.ihpc.schedulerapp.socialwebservice.exception;

public class StoreDataException extends Exception {

	private static final long serialVersionUID = -5004233003313546124L;
	
	public StoreDataException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public StoreDataException(String message){
		super(message);
	}

}
