package simpleapi.errors;

public enum ErrorsEnum {

	E001("E001", "NUll name", "The name provided is null"),
	E002("E002", "Invalid e-mail", "Enter a valid e-mail"),
	E003("E003", "Invalid sex", "Choose either F for female or M for male"),
	E004("E004", "This message code does not exist", "Send a valid message code."),
	E005("E005", "This code is still in the processing queue", "This code wans't processed yet. Try again later.");
	
	private String code;
	private String errorMessage;
	private String fix;
	
	private ErrorsEnum(String code, String errorMessage, String fix) {
		this.code = code;
		this.errorMessage = errorMessage;
		this.fix = fix;
	}
	
	public ErrorMessage message() {
    	ErrorMessage msg = new ErrorMessage();
    	msg.setCode(code);
    	msg.setErrorMessage(errorMessage);
    	msg.setFix(fix);
    	return msg;
    }

	public String getCode() {
		return code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getFix() {
		return fix;
	}
}
