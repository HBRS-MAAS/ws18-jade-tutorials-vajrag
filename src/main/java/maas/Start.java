package maas;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Start {


	public static void main(String[] args) {
		Random rand = new Random();

//		String[] BookTitles = { "Vulture Of Nightmares", "Witch Of Destruction","Bandits Of Darkness","Invaders Of The World","Horses And Spiders",
//				"Children And Lords","Surprise With Strength","Hope Of The Prison","Battle Of The Demons","Going To The Universe"};
//

		int Number_BuyerAgents = 20;
		int Number_SellerAgents = 3; 
		List<String> agents = new Vector<>();
//		// initializing buyer agents
//		for(int i = 1; i <= Number_BuyerAgents; i++ ) {
//			agents.add("BuyerAgent"+i+":maas.tutorials.BookBuyerAgent("+BookTitles[rand.nextInt(5)]+" , "+BookTitles[rand.nextInt(5)]+","+BookTitles[rand.nextInt(4)]+")");
//		}
//		// Initializing seller agents
//		agents.add("SellerAgent1:maas.tutorials.BookSellerAgent");
		
		for (int i = 0; i < Number_BuyerAgents; ++i){
	           StringBuilder sb = new StringBuilder();
	           sb.append("Buyer");
	           sb.append(Integer.toString(i));
	           sb.append(":maas.tutorials.BookBuyerAgent");
	           agents.add(sb.toString());
	       }

	    
	        for (int i = 0; i < Number_SellerAgents ; ++i){
	           StringBuilder sb = new StringBuilder();
	           sb.append("Seller");
	           sb.append(Integer.toString(i));
	           sb.append(":maas.tutorials.BookSellerAgent");
	           agents.add(sb.toString());
	       }


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