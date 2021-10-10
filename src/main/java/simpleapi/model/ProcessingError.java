package simpleapi.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "processing_error", schema = "test")
public class ProcessingError implements Serializable{
	
	private static final long serialVersionUID = 511661345572000500L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="fk_queued_message")
	@NotNull
	private QueuedMessage queuedMessage;
	
	@NotNull
	private String error;
	
	public ProcessingError() {
	}

	public ProcessingError(QueuedMessage queuedMessage, String error) {
		super();
		this.queuedMessage = queuedMessage;
		this.error = error;
	}

	public Long getId() {
		return id;
	}

	public QueuedMessage getQueuedMessage() {
		return queuedMessage;
	}

	public String getError() {
		return error;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setQueuedMessage(QueuedMessage queuedMessage) {
		this.queuedMessage = queuedMessage;
	}

	public void setError(String error) {
		this.error = error;
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
		ProcessingError other = (ProcessingError) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
