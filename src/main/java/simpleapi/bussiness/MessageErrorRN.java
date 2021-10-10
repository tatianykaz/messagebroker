package simpleapi.bussiness;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import simpleapi.model.ProcessingError;

@Stateless
@SuppressWarnings("serial")
public class MessageErrorRN implements Serializable{
	
	@PersistenceContext
	EntityManager em;

	public ProcessingError find(String id) {
		
		return em.find(ProcessingError.class, id);
		
	}
	
	public void save(ProcessingError messageError) {
		em.persist(messageError);
	}
}
