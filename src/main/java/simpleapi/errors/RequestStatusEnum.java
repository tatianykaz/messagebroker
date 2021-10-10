package simpleapi.errors;

public enum RequestStatusEnum {

	WAITING("WAITING", "THE REQUEST IS STILL WAITING TO BE PROCESSED."),
	PROCESSED("PROCESSED", "THE REQUEST WAS PROCESSED.");
	
	private String status;
	private String statusMessage;
	
	private RequestStatusEnum(String status, String statusMessage) {
		this.status = status;
		this.statusMessage = statusMessage;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

}
