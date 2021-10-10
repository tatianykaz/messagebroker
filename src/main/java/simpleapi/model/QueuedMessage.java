package simpleapi.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import simpleapi.errors.RequestStatusEnum;

@Entity
@Table(name = "queued_messages", schema = "test")
public class QueuedMessage implements Serializable {
	
	private static final long serialVersionUID = -1239975219639948218L;

	@Id
	private String id;
	
	private String status;
	
	@OneToOne
	@JoinColumn(name="fk_costumer")
	private Costumer costumer;
	
	public QueuedMessage() {
	}
	
	public QueuedMessage(String id, RequestStatusEnum requestStatusEnum) {
		this.status = requestStatusEnum.getStatus();
		this.id = id;
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Costumer getCostumer() {
		return costumer;
	}

	public void setCostumer(Costumer costumer) {
		this.costumer = costumer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		QueuedMessage other = (QueuedMessage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
