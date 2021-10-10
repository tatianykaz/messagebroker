package simpleapi.api;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import simpleapi.bussiness.CostumerRN;
import simpleapi.model.Costumer;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "TestQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") },
        mappedName="jms/queue/TestQueue")
public class RegisterCostumerQueueListener implements MessageListener{
	
	@Resource(mappedName = "java:/queue/TestQueue")
	private Queue fila;
 
	@Inject
	@JMSConnectionFactory("java:/ConnectionFactory")
	private JMSContext jmsContext;	
	
	@Inject
	private CostumerRN costumerRN;
	
    @Override
    public void onMessage(Message message)
    {       
    	try {
			Object receivedObject = ((ObjectMessage) message).getObject();
			
			Costumer costumer = (Costumer) receivedObject;
				
		    System.out.println("Got costumer from the queue. Sending to worker... " + costumer.toString());
		       
		    costumerRN.registerCostumer(costumer, message.getJMSMessageID().replace("ID:", ""));
		       
		} catch (JMSException e) {
			e.printStackTrace();
		}  
    }
}
