package simpleapi.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import simpleapi.bussiness.CostumerRN;
import simpleapi.bussiness.MessageErrorRN;
import simpleapi.bussiness.QueuedMessageRN;
import simpleapi.errors.ErrorMessage;
import simpleapi.errors.RequestStatusEnum;
import simpleapi.model.Costumer;
import simpleapi.model.QueuedMessage;

@Path("/costumers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegisterServices {

	@Resource(mappedName = "java:/queue/TestQueue")
	private Queue fila;

	@Inject
	@JMSConnectionFactory("java:/ConnectionFactory")
	private JMSContext jmsContext;

	@Inject
	CostumerRN costumerRN;

	@Inject
	QueuedMessageRN queuedMessageRN;

	@Inject
	MessageErrorRN messageErrorRN;

	@POST
	@Consumes("application/json")
	@Transactional
	public Response sendObjectToQueue(Costumer costumer) {
		try {
			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage objMessage = jmsContext.createObjectMessage();
			objMessage.setObject(costumer);
			producer = producer.send(fila, objMessage);

			queuedMessageRN.save(
					new QueuedMessage(objMessage.getJMSMessageID().replace("ID:", ""), RequestStatusEnum.WAITING));

			System.out.println("Inserido na fila com sucesso.");

			return Response.ok(objMessage.getJMSMessageID(), MediaType.APPLICATION_JSON).build();

		} catch (JMSException ex) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("An error occured. Try again later.").build();// 500
		}
	}

	@GET
	@Path("")
	public Response getRequestStatus(@QueryParam("code") String code) {

		List<ErrorMessage> errorsCode = new ArrayList<ErrorMessage>();

		errorsCode.addAll(queuedMessageRN.validateMessageCode(code));

		if (errorsCode.isEmpty()) {
			RequestStatus requestStatus = queuedMessageRN.buildRequestStatus(code);
			
			return Response.ok(requestStatus, MediaType.APPLICATION_JSON).build();//200
		}

		return Response.status(Status.BAD_REQUEST).entity(errorsCode).build();// 400
	}


	// http://localhost:8080/ActiveMQAPI/api/costumers
	/*
	 * {"name": "John Apple", "email": "john@apple.com", "sex": "M", "birthDate":
	 * 1980-01-01, "phone": "9999999999"}
	 */

}
