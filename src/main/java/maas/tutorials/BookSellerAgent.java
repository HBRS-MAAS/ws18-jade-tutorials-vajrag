package maas.tutorials;
import java.util.Hashtable;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookSellerAgent extends Agent{
	
	Random rand = new Random();
	private Hashtable paperback_quantities;
	
	//maps the title name of the paperback to  its price
	private Hashtable paperbacks_catalogue;
	
	//maps the title name of the ebooks to  its price
	private Hashtable ebooks_catalogue;
	
	String[] BookTitle = {"Vulture Of Nightmares", "Witch Of Destruction","Bandits Of Darkness",
								"Invaders Of The World","Horses And Spiders"}; 
	
	
	
	private AID BuyerAgents;
	
	//Agent initializations
	protected void setup() {
		System.out.println("Hello! Seller-agent "+getAID().getName()+" is ready.");
		
		// ***************
		// Add catalogue function here
		build_catalogue();
		// ***************
		
		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		//Add the behavior requests for offer from buyer agents
		addBehaviour(new OfferRequestsServer());
		
		//Add the behaviour serving purchase orders from buyer agents
		addBehaviour(new PurchaseOrdersServer());
	
	
	try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        //e.printStackTrace();
    }
	}
	
// ***************************DONT CHANGE**************************************************
	//Put agent clean up operations here
	protected void takeDown() {
		// De-register from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("Seller agent"+getAID().getName()+"terminating.");
	}
// ************************************************************************************
	
	private void build_catalogue() {
		paperbacks_catalogue = new Hashtable();
		ebooks_catalogue = new Hashtable();
		paperback_quantities = new Hashtable();
		String[] ebooks_titles = {"Vulture Of Nightmares", "Witch Of Destruction","Bandits Of Darkness",
				"Invaders Of The World"};
		String[] paperbacks_titles = {"Vulture Of Nightmares", "Witch Of Destruction","Bandits Of Darkness",
				"Invaders Of The World"};
		//int n_paperbacks = 20;
		int[] paperback_quantity = {5,5,5,5};
		int[] prices = {10,23,14,56,78,96,45,77};
		
		for(int i=0; i< (paperbacks_titles.length/2);i++) {
		paperbacks_catalogue.put(paperbacks_titles[rand.nextInt(4)], prices[rand.nextInt(5)]);
		paperback_quantities.put(paperbacks_titles[rand.nextInt(4)], paperback_quantity[rand.nextInt(2)]);
		}
		
		
		for(int i=0; i< (ebooks_titles.length/2);i++) {
			ebooks_catalogue.put(ebooks_titles[rand.nextInt(2)], prices[rand.nextInt(5)]);
			}
			
	}
///*******************************************************************************************
	private class OfferRequestsServer extends CyclicBehaviour{
		
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive();
			
			if(msg!= null) {
				//Message received. Process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				//Integer price = (Integer) catalogue.get(title);
				Integer price = (Integer)0;
				Integer quantity = (Integer) 0;
			
				boolean ebook = ebooks_catalogue.contains(title);
				boolean paperback = paperbacks_catalogue.contains(title);
				
				if (ebook) {
					price = (Integer) ebooks_catalogue.get(title);
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
					//System.out.println("Sold "+title+ " ebook");
					 
				}
				else if(paperback){
					// check the quamtity of the paperback available
					 quantity = (Integer) paperback_quantities.get(title);
					 price = (Integer) paperbacks_catalogue.get(title);
					if (quantity>0) {
						reply.setPerformative(ACLMessage.PROPOSE);
						reply.setContent(String.valueOf(price.intValue()));
					}
				}
				else {
					//The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}
			
	//********************************************************************************
		private class PurchaseOrdersServer extends CyclicBehaviour {
			
		  public void action() {
			  MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			  ACLMessage msg = myAgent.receive(mt);
			  
		    if (msg != null) {
		      // ACCEPT_PROPOSAL Message received. Process it
		      String title = msg.getContent();
		      ACLMessage reply = msg.createReply();
		      
		      Integer price = (Integer) 0;
				Integer quantity = (Integer) 0;

		      
		      boolean ebook = ebooks_catalogue.contains(title);
			  boolean paperback = paperbacks_catalogue.contains(title);
		      
		      if (ebook) {
					price = (Integer) ebooks_catalogue.get(title);
					reply.setPerformative(ACLMessage.INFORM);
					 System.out.println(title+" sold to agent "+msg.getSender().getName());
					 
				}
				else if(paperback){
					// check the quamtity of the paperback available
					 quantity = (Integer) paperback_quantities.get(title);
					 price = (Integer) paperbacks_catalogue.get(title);
					if (quantity>0) {
						reply.setPerformative(ACLMessage.INFORM);
						 System.out.println(title+" sold to agent "+msg.getSender().getName());
						 paperback_quantities.put(title, quantity-1);
					}
				}
				else {
					//The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.FAILURE);
			        reply.setContent("requested book is not-available");
				}
		      myAgent.send(reply);
		    }
			  else {
			    block();
			  }
		  }
	}
}