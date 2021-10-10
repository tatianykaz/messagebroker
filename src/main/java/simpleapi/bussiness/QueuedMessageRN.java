package simpleapi.bussiness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import simpleapi.api.RequestStatus;
import simpleapi.errors.ErrorMessage;
import simpleapi.errors.ErrorsEnum;
import simpleapi.errors.RequestStatusEnum;
import simpleapi.model.QueuedMessage;

@Stateless
@SuppressWarnings("serial")
public class QueuedMessageRN implements Serializable {

	@PersistenceContext
	EntityManager em;
	
	@Inject
	CostumerRN costumerRN;
	
	public List<ErrorMessage> validateMessageCode(String code) {
		
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		
		if (this.find(code) == null)
			errors.add(new ErrorMessage(ErrorsEnum.E004));
		
		return errors;
	}
	
	public QueuedMessage find(String id) {
		return em.find(QueuedMessage.class, id);
	}
	
	public void save(QueuedMessage queuedMessage) {
		em.persist(queuedMessage);
	}
	
	public void edit(QueuedMessage queuedMessage) {
		em.merge(queuedMessage);
	}
	
	public List<ErrorMessage> getErrorsByQueuedMessageCode(String code){
		
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		
		String hql = "SELECT pe.error FROM ProcessingError pe JOIN pe.queuedMessage q WHERE q.id = :CODE ";
		
		Query query = em.createQuery(hql);
		
		query.setParameter("CODE", code);
		
		@SuppressWarnings("unchecked")
		List<String> errorCodes = (List<String>) query.getResultList();
		
		for (String errorCode : errorCodes) {
			errors.add(new ErrorMessage(ErrorsEnum.valueOf(errorCode)));
		}
		
		return errors;
		
	}
	
	public RequestStatus buildRequestStatus(String code) {

		QueuedMessage queuedMessage = this.find(code);

		if (queuedMessage.getStatus().equals(RequestStatusEnum.PROCESSED.getStatus())) {

			List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
			errors.addAll(this.getErrorsByQueuedMessageCode(code));

			if (!errors.isEmpty())
				return new RequestStatus(RequestStatusEnum.PROCESSED, errors, null);

			else
				return new RequestStatus(RequestStatusEnum.PROCESSED, null, costumerRN.getCostumerByMessageCode(code));
		}

		return new RequestStatus(RequestStatusEnum.WAITING, null, null);
	}

	
}
