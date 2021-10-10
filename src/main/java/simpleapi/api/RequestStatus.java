package simpleapi.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import simpleapi.errors.ErrorMessage;
import simpleapi.errors.RequestStatusEnum;
import simpleapi.model.Costumer;

@SuppressWarnings("serial")
@XmlRootElement
public class RequestStatus implements Serializable {
	
	@XmlElement
	private String status;
	
	@XmlElement
	private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
	
	@XmlElement
	private Costumer costumer = new Costumer();
	
	public RequestStatus() {
		
	}
	
	public RequestStatus(RequestStatusEnum requestStatusEnum, List<ErrorMessage> errorMessages, Costumer costumer) {
		
		this.status = requestStatusEnum.getStatus();
		
		this.errorMessages = errorMessages;
		
		this.costumer = costumer;
	}

	public String getStatus() {
		return status;
	}

	public Costumer getCostumer() {
		return costumer;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCostumer(Costumer costumer) {
		this.costumer = costumer;
	}

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}	
	
}
