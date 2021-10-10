package simpleapi.bussiness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.validator.routines.EmailValidator;

import com.arjuna.ats.jta.exceptions.RollbackException;
import simpleapi.errors.ErrorMessage;
import simpleapi.errors.ErrorsEnum;
import simpleapi.errors.RequestStatusEnum;
import simpleapi.model.Costumer;
import simpleapi.model.ProcessingError;
import simpleapi.model.QueuedMessage;

@Stateless
@Transactional(rollbackOn = RollbackException.class, dontRollbackOn = Exception.class)
public class CostumerRN implements Serializable {

	private static final long serialVersionUID = 5501241178256403469L;
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	QueuedMessageRN queuedMessageRN;
	
	@Inject
	MessageErrorRN messageErrorRN;
	
	private List<ErrorMessage> validateCostumer(Costumer person){
		
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		
		errors.addAll(this.validateName(person.getName()));
		errors.addAll(this.validadeEmail(person.getEmail()));
		errors.addAll(this.validadeSex(person.getSex()));
		
		return errors;
		
	}

	private List<ErrorMessage> validateName(String name){
		
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		
		if (name == null)
			errors.add(new ErrorMessage(ErrorsEnum.E001));
		
		return errors;
	}
	
	private List<ErrorMessage> validadeEmail(String email){
		
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		
		if (email != null && email.length() > 0 && !EmailValidator.getInstance().isValid(email))
			errors.add(new ErrorMessage(ErrorsEnum.E002));
		
		return errors;
		
	}
	
	private List<ErrorMessage> validadeSex(char sex){
		
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		
		if (sex != 'F' && sex != 'M')
			errors.add(new ErrorMessage(ErrorsEnum.E003));
		
		return errors;
	}

	private Costumer save(Costumer costumer) {
		
		em.persist(costumer);
		
		return costumer;
	}

	public Costumer getCostumerByMessageCode(String code) {
		
		String hql = "SELECT p FROM QueuedMessage q JOIN q.costumer p WHERE q.id = :CODE ";
		
		Query query = em.createQuery(hql).setParameter("CODE", code);
		
		return (Costumer) query.getSingleResult();
	}
	
	@Transactional
	public void registerCostumer(Costumer costumer, String code) {
		
		/*Simulate a delay on processing the request, to check the queue handling it*/
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

		errors.addAll(this.validateCostumer(costumer));

		QueuedMessage queuedMessage = queuedMessageRN.find(code);
		queuedMessage.setStatus(RequestStatusEnum.PROCESSED.getStatus());

		if (!errors.isEmpty()) {
			for (ErrorMessage error : errors) {// Persist validation errors

				ProcessingError processingError = new ProcessingError(queuedMessage, error.getCode());
				messageErrorRN.save(processingError);
			}
		} else {
			try {
				costumer = this.save(costumer);
				queuedMessage.setCostumer(costumer);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		queuedMessageRN.edit(queuedMessage);
	}

}
