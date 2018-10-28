package maas;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import maas.tutorials.BookBuyerAgent;

public class Start {
	
	
    public static void main(String[] args) {
    	String[] BookTitles = { "Vulture Of Nightmares", "Witch Of Destruction","Bandits Of Darkness","Invaders Of The World","Horses And Spiders",
    			"Children And Lords","Surprise With Strength","Hope Of The Prison","Battle Of The Demons","Going To The Universe"};
    	
    	int[] bookPrices = {45,12,47,63,47,89,29,71,23,68,74};
    	int Number_BuyerAgents = 20;
    	int Number_SellerAgents = 3; 
    	
    	Random rand = new Random();

    	
    	
    	List<String> agents = new Vector<>();
    	agents.add("tester:maas.tutorials.BookBuyerAgent");
    	for(int BookBuyerAgent = 1; BookBuyerAgent <= Number_BuyerAgents; BookBuyerAgent ++ ) {
    		agents.add("BuyerAgent"+BookBuyerAgent+":maas.tutorials.BookBuyerAgent (" + BookTitles[rand.nextInt(5)]+")");
    		}
    	for (int BookSellerAgent =1; BookSellerAgent <= 3; BookSellerAgent ++) {
    		agents.add("SellerAgent"+BookSellerAgent+":maas.tutorial.BookSellerAgent")
    	}
    	// List<String> TargetBooks = new Vector<>();


    	List<String> cmd = new Vector<>();
    	cmd.add("-agents");
    	StringBuilder sb = new StringBuilder();
    	for (String a : agents) {
    		sb.append(a);
    		sb.append(";");
    	}
    	cmd.add(sb.toString());
        jade.Boot.main(cmd.toArray(new String[cmd.size()]));
    }
}
