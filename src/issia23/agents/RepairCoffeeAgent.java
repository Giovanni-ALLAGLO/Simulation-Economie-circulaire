package issia23.agents;

import issia23.data.ProductType;
import jade.core.AgentServicesTools;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.AgentWindowed;
import jade.gui.SimpleWindow4Agent;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.util.Random;

/** class that represents a repair agent.
 * It is declared in the service repair-coffee.
 * It owns some specialities
 * @author emmanueladam@
 * */
public class RepairCoffeeAgent extends AgentWindowed {
	  private List<ProductType> specialities;

	    @Override
	    public void setup() {
	        this.window = new SimpleWindow4Agent(getLocalName(), this);
	        this.window.setBackgroundTextColor(Color.orange);
	        println("Hello, do you want coffee?");
	        Random random = new Random();

	        specialities = new ArrayList<>();
	        for (ProductType type : ProductType.values())
	            if (random.nextBoolean()) specialities.add(type);

	        // We need at least one speciality
	        if (specialities.isEmpty()) specialities.add(ProductType.values()[random.nextInt(ProductType.values().length)]);

	        println("I have these specialties: ");
	        specialities.forEach(p -> println("\t" + p));

	        // Registration to the yellow pages (Directory Facilitator Agent)
	        registerInYellowPages();

	        println("I'm just registered as a repair-coffee");
	    }

	    private void registerInYellowPages() {
	        // Create a description of the agent for registration with the DF
	        DFAgentDescription dfd = new DFAgentDescription();
	        dfd.setName(getAID());

	        // Add a service to the agent
	        ServiceDescription sd = new ServiceDescription();
	        sd.setType("repair");
	        sd.setName("coffee");

	        // Add the specialities as user-defined properties
	        String specialitiesString = specialities.toString();
	        specialitiesString = specialitiesString.substring(1, specialitiesString.length() - 1);
	        sd.addProperties(createProperty("specialities", specialities));

	        dfd.addServices(sd);

	        try {
	            // Register the agent in the DF
	            DFService.register(this, dfd);
	        } catch (FIPAException e) {
	            e.printStackTrace();
	        }
	    }

	    private Property createProperty(String key, Object value) {
	        Property property = new Property();
	        property.setName(key);
	        property.setValue(value);
	        return property;
	    }
	    
	    @Override
	    public void takeDown() {
	        println("Goodbye! Unregistering from the yellow pages.");
	        try {
	            DFService.deregister(this);
	        } catch (FIPAException e) {
	            e.printStackTrace();
	        }
	    }
	}