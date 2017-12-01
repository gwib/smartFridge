//package ...

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import commexercise.rpc.CallListener;
import commexercise.rpc.RpcServer;
import commexercise.rpc.RpcServerImpl;
import food.FoodItem;
import food.Fridge;
import food.Recipe;
import food.ShoppingList;
import food.csvFood;

public class Server {
	
	// All the actions we can do (if modification, also modify the client)
	private static final String ADDITEMS="addItems";
	private static final String GETLISTITEMS = "getListItems";
	private static final String CHOOSEITEMS="chooseItems";
	private static final String GETSHOPPINGLIST="getShoppingList";
	
	private static final String ADDRECIPE="addRecipe";
	private static final String GETRECIPES="getRecipes";
	private static final String REMOVERECIPIE="removeRecip";
	private static final String MODIFYRECIPIE="modifyRecip";
	
	private static final String GETPOSSIBLERECIPESTODO="getPossibleRecipesToDo";
	private static final String GETRECIPESWITHINGREDIENTSABOUTTOEXPIRE="getRecipesWithIngredientsAboutToExpire";
	
	private static final String TESTCOMM="testComm";
	
	private static Fridge fridge;
	//private static RecipeBase recipeBase;
	private static ArrayList<Recipe> recipes;
	private static int port = 8080;
	private static csvFood csv;
	private static csvFood csvRecipes;
	
	public static void main(String[] args) throws Exception {
		System.out.println("Start");
		//Check if there is stored data available in csv's
		fridge = new Fridge();
		//recipeBase = new RecipeBase();
		recipes = new ArrayList<Recipe>();
		//items stored in the kitchen used in recipies but that are not stored inside the fridge???
		
		csv = new csvFood(); // Using this constructor initialize it with the stored values
		fridge.setFood(csv.readCsv());
		
		csvRecipes = new csvFood("Recipe.csv","Recipe_Name;FoodItems;Description");
		recipes = csvRecipes.readCsvRecipie(); // initialize with the stored values
		
		// create an rpc server listening on a specific port
	    RpcServer server = new RpcServerImpl(port).start();

	    // add a call listener that will get called when a client does an RPC call to the server
	    server.setCallListener(new CallListener() {
	      @Override
	      public String[] receivedSyncCall(String function, String[] fargs) throws Exception {
	    	  String[] response = null;
	    	  switch (function) {
		        case ADDITEMS:  response = addItems(fargs); break;
		        case GETLISTITEMS:  response = getListItems(); break;
		        case CHOOSEITEMS:  response = chooseItems(fargs); break;
		        case GETSHOPPINGLIST:  response = getShoppingList(); break;
		        
		        case ADDRECIPE:  response = addRecipe(new String[] {""}); break;
		        case GETRECIPES:  response = getRecipes(); break;
		        case REMOVERECIPIE:  response = removeRecip(fargs); break;
		        case MODIFYRECIPIE:  response = modifyRecip(); break;
		        
		        case GETPOSSIBLERECIPESTODO:  response = getPossibleRecipesToDo(); break;
		        case GETRECIPESWITHINGREDIENTSABOUTTOEXPIRE:  response = getRecipesWithIngredientsAboutToExpire(); break;

		        case TESTCOMM:  response = testComm(); break;
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
		csvFood.writeCsvFile(fridge.getFood()); // update the csv file
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
    	
    	csvFood.writeCsvFile(fridge.getFood()); // update the csv file
    	
    	String msg = CHOOSEITEMS;
		System.out.println(msg);
		return new String[] {msg};
	}
    
    private static String[] getShoppingList() {
    	//TODO: fridge.getUsualItems() - fridge.getCurrentItems()
    	
    	String msg = "getShoppingList";
		System.out.println(msg);
		return new String[] {new ShoppingList().getShoppingList(fridge)};
    }
    
    private static String[] addRecipe(String[] recipe) throws IOException {
    	//TODO: sth similar to 'chooseItems'
    	//recipeBase.addRecipe(r);
    	
    	 // update csv
    	
    	String msg = "addRecipe";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] recipeArray2str(ArrayList<Recipe> rec) {
    	String[] strRecipes = new String[rec.size()];
    	int i = 0;
    	for(Recipe r: rec) {
    		strRecipes[i] = r.toString();
    		i++;
    	}
    	return strRecipes;
    }
    
    private static String[] getRecipes() {
    	String msg = "getRecipes";
		System.out.println(msg);
		return recipeArray2str(recipes);
    }
    
    private static String[] removeRecip(String[] recipeNames) {
    	String recNameList = "";
    	for(String rn: recipeNames) {
    		recNameList += "'" + rn + "', ";
	    	for (Recipe r : recipes) {
				if (r.getName().equals(rn)) {
					recipes.remove(r);
					break;
				}
			}
    	}
    	csv.writeCsvRecipe(recipes); // update csv
    	
    	String msg = " " + recNameList + "removed from the list of recipes!";
		System.out.println(msg);
		return new String[] {msg};
    }
    
    private static String[] modifyRecip() { //TODO:
    	/*String text = null;
    	for(Recipe r: recipes) {
    		if(r.getName().equals(text) ) {
    			if(!(text.equals(""))) {
    				
    			}
    		}
    	}*/
    	
    	csv.writeCsvRecipe(recipes); // update csv
    	
    	String msg = "modifyRecip";
		System.out.println(msg);
		return new String[] {msg};
    }
    
	private static String[] getPossibleRecipesToDo() { //TODO:
		System.out.println(GETPOSSIBLERECIPESTODO);
		
    	//call function that return arraylist of recipes
		//for now consider all recipes
		ArrayList<Recipe> possible = recipes;
		return recipeArray2str(possible);
    }
	
	private static String[] getRecipesWithIngredientsAboutToExpire() { //TODO:
		System.out.println(GETRECIPESWITHINGREDIENTSABOUTTOEXPIRE);
		
    	//call function that return arraylist of recipes
		//for now consider all recipes
		ArrayList<Recipe> possible = recipes;
		return recipeArray2str(possible);
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