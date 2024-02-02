package issia23.agents;

import issia23.data.Product;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.AgentWindowed;
import jade.gui.SimpleWindow4Agent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Distributor extends AgentWindowed {
    private List<Product> products;

    @Override
    public void setup() {
        this.window = new SimpleWindow4Agent(getLocalName(), this);
        this.window.setBackgroundTextColor(Color.lightGray);

        // Register as a distributor in the yellow pages
        registerInYellowPages();

        println("Hello, I'm just registered as a distributor");
        println("Do you want any product?");

        Random random = new Random();
        products = new ArrayList<>();
        List<Product> existingProducts = Product.getListProducts();

        for (Product p : existingProducts)
            if (random.nextBoolean())
                products.add(new Product(p.getName(), p.getType()));

        // We need at least one product
        if (products.isEmpty()) products.add(existingProducts.get(random.nextInt(existingProducts.size())));

        println("Here are the products I sell: ");
        products.forEach(p -> println("\t" + p));
    }

    private void registerInYellowPages() {
        // Create a description of the agent for registration with the DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        // Add a service to the agent
        ServiceDescription sd = new ServiceDescription();
        sd.setType("distributor");
        sd.setName("products");

        dfd.addServices(sd);

        try {
            // Register the agent in the DF
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
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
