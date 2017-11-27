//package ...

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import commexercise.rpc.CallListener;
import commexercise.rpc.RpcServer;
import commexercise.rpc.RpcServerImpl;

public class Master {
	
	// All the actions we can do (if modification, also modify the client)
	private static final String ADDITEMS="addItems";
	private static final String GETLISTITEMS = "getListItems";
	private static final String CHOOSEITEMS="chooseItems";
	private static final String GETSHOPPINGLIST="getShoppingList";
	
	private static final String ADDRECIPIE="addRecipe";
	private static final String GETRECIPES="getRecipes";
	private static final String REMOVERECIPIE="removeRecip";
	private static final String MODIFYRECIPIE="modifyRecip";
	
	private static final String TESTCOMM="testComm";
	
	public static Fridge fridge;
	public static RecipeBase recipeBase;
	public static int port = 8080;
	
	public static void main(String[] args) throws Exception {
		System.out.println("Start");
		//TODO: Check if there is stored data available in csv's (fridge, recipeBase)
		fridge = new Fridge();
		recipeBase = new RecipeBase();
		//items stored in the kitchen used in recipies but that are not stored inside the fridge???
		
		
		// create an rpc server listening on a specific port
	    RpcServer server = new RpcServerImpl(port).start();

	    // add a call listener that will get called when a client does an RPC call to the server
	    server.setCallListener(new CallListener() {
	      @Override
	      public String[] receivedSyncCall(String function, String[] fargs) throws Exception {
	    	  String[] response = null;
	    	  switch (function) {
		        case "addItems":  response = addItems(fargs); break;
		        case "getListItems":  response = getListItems(); break;
		        case "chooseItems":  response = chooseItems(fargs); break;
		        case "getShoppingList":  response = getShoppingList(); break;
		        
		        case "addRecipe":  response = addRecipe(new String[] {""}); break;
		        case "getRecipes":  response = getRecipes(); break;
		        case "removeRecip":  response = removeRecip(); break;
		        case "modifyRecip":  response = modifyRecip(); break;
		        
		        case "testComm":  response = testComm(); break;
		        
		        default:  return new String[]{"Function '"+function+"' does not exist."};
		    }
	        return response;
	      }

	      @Override
	      public String[] receivedAsyncCall(String function, String[] args, long callID) throws Exception {
	        return receivedSyncCall(function,args);
	      }
	    });
	    System.out.println("Waiting for inputs...");
	}
	
	private static FoodItem str2foodItem(String str) {
		FoodItem f = null;
		String[] item = str.split("/,");
		String name = item[0];
		String expirationDate = item[3]; //TODO: correct type of expiration date
		if(item[1].equals("q")) {
			int quantity = Integer.parseInt(item[2]);
			f = new FoodItem(name, quantity, expirationDate);
		}
		else {
			double weight = Double.parseDouble(item[2]);
			f = new FoodItem(name, weight, expirationDate);
		}
		return f;
	}
	
	private static FoodItem str2recipe(String str) {
		Recipe r = null;
		FoodItem f = null;
		String[] item = str.split("/,");// alertaaaaaaaa!!!!
		String name = item[0];
		String expirationDate = item[3]; //TODO: correct type of expiration date
		if(item[1].equals("q")) {
			int quantity = Integer.parseInt(item[2]);
			f = new FoodItem(name, quantity, expirationDate);
		}
		else {
			double weight = Double.parseDouble(item[2]);
			f = new FoodItem(name, weight, expirationDate);
		}
		return f;
	}
	
	private static String foodItem2str(FoodItem f) {
		String s = "";
		s += f.getName();
		if(f.getWeight() != null) {
			s += "/,w/," + String.valueOf(f.getWeight());
		}
		else {
			s += "/,q/," + String.valueOf(f.getQuantity());
		}
		s += "/," + f.getExpirationDate();
		return s;
	}
	
	private static String[] addItems(String[] items) {
		for (String i: items) {
			fridge.addFoodItem(str2foodItem(i));
		}
		String msg = "addItems";
		for(String i: items) {
			System.out.println(i);
		}
		return new String[] {msg};
	}
	
	private static String[] getListItems() {
		//TODO:
		ArrayList<FoodItem> food = fridge.getFood();
		//String fridgeFood = "Current food inside the fridge:\n-------------------------------";
		String[] fridgeFood = new String[food.size()];
		int i = 0;
		for (FoodItem f: food) {
			//convert into String --> add toString() in FoodItem
			//fridgeFood += "\n"+ foodItem2str(f);//f.toString();
			fridgeFood[i] = foodItem2str(f);
			i++;
		}
		//return fridgeFood;
		
		String msg = "getListItems";
		System.out.println(msg);
		return fridgeFood;
	}
	
    private static String[] chooseItems(String[] items) {
    	//TODO: Temporal!!!
    	//ArrayList<FoodItem> chosenFood = new ArrayList<FoodItem>();
    	/*for(String i: items) {
    		fridge.removeFoodItem(str2foodItem(i));
    		//chosenFood.add(str2foodItem(i));
    	}*/
    	//fridge.removeFoodItem(chosenFood);
    	
    	String msg = CHOOSEITEMS;
		System.out.println(msg);
		return new String[] {msg};
	}
    
    private static String[] getShoppingList() {
    	//TODO: fridge.getUsualItems() - fridge.getCurrentItems()
    	String msg = "getShoppingList";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] addRecipe(String[] recipe) {
    	//TODO: sth similar to 'chooseItems'
    	//recipeBase.addRecipe(r);
    	
    	String msg = "addRecipe";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] getRecipes() {
    	//TODO:
    	
    	
    	String msg = "getRecipes";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] removeRecip() {
    	//TODO:
    	String msg = "removeRecip";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] modifyRecip() { //TODO:
    	String msg = "modifyRecip";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] testComm() { //TODO:
    	String msg = "\n************\n* testComm *\n************\n";
		System.out.println(msg);
		return new String[] {msg};
    }

}

/*
 * java -classpath bin/:lib/slf4j-api-1.7.21.jar:lib/commons-codec-1.2.jar:lib/commons-logging-1.1.jar:lib/ws-commons-util-1.0.2.jar:lib/xml-apis-1.0.b2.jar:lib/xmlrpc-client-3.1.3.jar:lib/xmlrpc-common-3.1.3.jar:lib/xmlrpc-server-3.1.3.jar:lib/commons-httpclient-3.1.jar:lib/slf4j-simple-1.7.5.jar commexercise.TestSubscriber "$@"
   java -classpath bin/:lib/slf4j-api-1.7.21.jar:lib/commons-codec-1.2.jar:lib/commons-logging-1.1.jar:lib/ws-commons-util-1.0.2.jar:lib/xml-apis-1.0.b2.jar:lib/xmlrpc-client-3.1.3.jar:lib/xmlrpc-common-3.1.3.jar:lib/xmlrpc-server-3.1.3.jar:lib/commons-httpclient-3.1.jar:lib/slf4j-simple-1.7.5.jar dtu.is31380.HouseControllerMaster3
   java -classpath bin/:lib/slf4j-api-1.7.21.jar:lib/commons-codec-1.2.jar:lib/commons-logging-1.1.jar:lib/ws-commons-util-1.0.2.jar:lib/xml-apis-1.0.b2.jar:lib/xmlrpc-client-3.1.3.jar:lib/xmlrpc-common-3.1.3.jar:lib/xmlrpc-server-3.1.3.jar:lib/commons-httpclient-3.1.jar:lib/slf4j-simple-1.7.5.jar commexercise.TestSyncMaster "$@"

java -classpath bin/ commexercise.TestBroadcastReceiver "$@"
*/