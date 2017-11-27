
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


import commexercise.rpc.*;


/*
 * Comments:
 * ---------
 * 
 * - all slaves running in the same PC ("localhost") with different ports and generated with the same main
 */

public class Slave {
	//private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	// All the actions we can do (if modification, also modify the server)
	public static final String ADDITEMS="addItems";
	private static final String GETLISTITEMS = "getListItems";
	private static final String CHOOSEITEMS="chooseItems";
	private static final String GETSHOPPINGLIST="getShoppingList";
	
	private static final String ADDRECIPIE="addRecipe";
	private static final String GETRECIPES="getRecipes";
	private static final String REMOVERECIPIE="removeRecip";
	private static final String MODIFYRECIPIE="modifyRecip";
	
	public static final String TESTCOMM="testComm";
	
	//Default server parameters
	private static int port = 8080;
	private static String serverIp = "localhost";
	private static String consola = null;
	private static String[] params = null;
	private static String[] fridgeItems = null; // updated everytime 'GETLISTITEMS' is called
	private static String directions = "\nType the number of the action and then hit Enter.\n\n"
			+ " 0     "+TESTCOMM+"\n"
			+ " 1     "+ADDITEMS+"\n"
			+ " 2     "+GETLISTITEMS+"\n"
			+ " 3     "+CHOOSEITEMS+"\n"
			+ " 4     "+GETSHOPPINGLIST+"\n"
			+ " 5     "+ADDRECIPIE+"\n"
			+ " 6     "+GETRECIPES+"\n"
			+ " 7     "+REMOVERECIPIE+"\n"
			+ " 8     "+MODIFYRECIPIE+"\n"
			+ "\nOther commands:\n"
			+ " d     print directions\n"
			+ " c     close the program or cancell action\n"
			+ " ip    change destination IP\n"
			+ " p     change port (8080 default)\n";
	
	/*
	 * Constructors..........................................
	 */
	public Slave() {
		
	}
	
	public Slave(int port) {
		setPort(port);
	}
	
	/*
	 * Getters and setters...................................
	 */
	public static int getPort() {
		return port;
	}

	public static void setPort(int p) {
		port = p;
	}

	public static String getServerIp() {
		return serverIp;
	}

	public static void setServerIp(String servIp) {
		serverIp = servIp;
	}
	
	public static String getConsola() {
		return consola;
	}

	public static void setConsola(String cons) {
		consola = cons;
	}
	
	public static String[] getFridgeItems() {
		return fridgeItems;
	}

	public static void setFridgeItems(String[] fi) {
		fridgeItems = fi;
	}
	
	
	/*
	 * RPC comm.............................................
	 */
	private String[] rpcComm(String function, String[] args) {
		//start syncRPCmaster
		String[] reply = null;
		try {
			RpcClient client = new RpcClientImpl(getServerIp(),getPort());
			reply = client.callSync(function, args);
	                //new String[]{args});
			
			//System.out.println("Comunicació establerta!!!");
		}
		catch(Exception e) {
			reply[0] = "Excepcio!!!";
			System.out.println(reply[0]);
			//log.info(sdf.format(new Date()) + " - " + "Couldn't reach the slave: "+e.getMessage()+".");
		}
		return reply;
	}
	
	private void readConsole(Slave sl) throws InterruptedException {
		setConsola(null);
		sl.params = null;
		while(getConsola() == null) {
			Thread.sleep(1000);
			setConsola(System.console().readLine());
		}
	}
	private void changeIp(Slave sl){
		System.out.println("Will be able to change destination IP,\nlike it can be done with the port.");
	}
	
	private void changePort(Slave sl) throws InterruptedException {
		setConsola(null);
		System.out.print("\nCurrent port: "+sl.getPort()+"\nNew port: ");
		while(getConsola() == null) {
			readConsole(sl);
			String param = getConsola().split(" ")[0];
			
			if (param.equals("c")) {
				System.out.println("\nAction cancelled.");
				return;
			}
			try {
				int port = Integer.parseInt(param);
				if ((port < 1000) || (port > 30000)) {
					System.out.print("\nPort value must be an integer > 1000 and <= 30000.\nNew port: ");
					setConsola(null);
				}
				else {
					sl.setPort(port);
					System.out.println("\nPort updated to "+param+".");
				}
			}catch (NumberFormatException e){
				System.out.print("\nPort value must be an integer >= 1000 and <= 30000.\nNew port: ");
				setConsola(null);
			}
		}
	}
	
	private boolean isPositiveInteger(Slave sl, String parameter) throws InterruptedException {
		String errorMsg = "\n  Must be an integer > 0.\n  " + parameter + ": ";
		setConsola(null);
		while(getConsola() == null) {
			readConsole(sl);
			try {
				int value = Integer.parseInt(getConsola());
				if (value <= 0) {
					System.out.print(errorMsg);
					setConsola(null);
				}
				else {
					return true;
				}
			}catch (NumberFormatException e){
				System.out.print(errorMsg);
				setConsola(null);
			}
		}
		return false;
	}
	
	private boolean isPositiveDouble(Slave sl, String parameter) throws InterruptedException {
		String errorMsg = "\n  Must be a double > 0.\n  " + parameter + ": ";
		setConsola(null);
		while(getConsola() == null) {
			readConsole(sl);
			try {
				double value = Double.parseDouble(getConsola());
				if (value <= 0) {
					System.out.print(errorMsg);
					setConsola(null);
				}
				else {
					return true;
				}
			}catch (NumberFormatException e){
				System.out.print(errorMsg);
				setConsola(null);
			}
		}
		return false;
	}
	
	private void validateDate(Slave sl, String parameter) throws InterruptedException {
		String errorMsg = "\n  Please introduce the date in the correct format.\n  " + parameter + ": ";
		setConsola(null);
		while(getConsola() == null) {
			readConsole(sl);
			if(getConsola().matches("(\\d{4})\\-(0?0[1-9]|1[012])\\-(0?0[1-9]|[12][0-9]|3[01])")) {
				String[] inDate = getConsola().split("-"); //["yyyy","MM","dd"]
				try {
					Date expDate = new Date((Integer.parseInt(inDate[0])-1900),Integer.parseInt(inDate[1]), Integer.parseInt(inDate[2]));
					Date currentDate = new Date();
					System.out.println("     Exp: "+expDate.toString());
					System.out.println("     Now: "+currentDate.toString());
					if(expDate.getTime() > currentDate.getTime()) {
						System.out.println("      Future date!!!");
						return;
					}
					else {
						System.out.print(errorMsg);
						setConsola(null);
						System.out.println("      Past date...");
					}
				}
				catch(NumberFormatException e) {
					System.out.print(errorMsg);
					setConsola(null);
					System.out.println("      Not an integer...");
				}
			}
			else {
				System.out.print(errorMsg);
				setConsola(null);
				System.out.println("      Wrong format...");
			}
		}
	}
	
	private void addItems(Slave sl) throws InterruptedException {
		System.out.println("Sorry, we are still working on it! ;)");
		String newItems = "";
		int nItem = 0;
		setConsola("y");
		
		while(getConsola().equals("y")) {
			String item = "";
			System.out.print("\nIntroduce item "+Integer.toString(nItem+1)+":\n");
			System.out.print("  Name: ");
			readConsole(sl);
			item += getConsola() + "/,";
			
			System.out.print("  For products without barcode introduce the quantity (press 'q'),\n  for the others just the weight (press other) is enough... ");
			readConsole(sl);
			if(getConsola().equals("q")) {
				System.out.print("  Quantity: ");
				isPositiveInteger(sl, "Quantity");
				item += "q/," + getConsola() + "/,";
			}else {
				System.out.print("  Weight (g): ");
				isPositiveDouble(sl, "Weight (g)");
				item += "w/," + getConsola() + "/,";
			}
			//System.out.print("  Cooking time (min): ");
			//readConsole(sl);
			//newItems += getConsola() + "/,";
			System.out.print("  Expiration date (yyyy-MM-dd): ");
			validateDate(sl, "Expiration date (yyyy-MM-dd)");
			item += getConsola();
			
			System.out.print("\nDo you want to add this item to the list? (y = yes) ");
			readConsole(sl);
			if(getConsola().equals("y")) {
				nItem += 1;
				newItems += item + "/;";
			}
			
			System.out.print("\nDo you want to add an other item? (y = yes) ");
			readConsole(sl);
		}
		
		// Send newItems to the server
		String[] reply = sl.rpcComm(sl.ADDITEMS, newItems.split("/;"));
		System.out.println(reply[0]);
	}
	private void getListItems(Slave sl) throws InterruptedException {
		//System.out.println("Sorry, we are still working on it! ;)");
		
		String[] reply = sl.rpcComm(sl.GETLISTITEMS, new String[] {""});
		setFridgeItems(reply);
		System.out.print("\nList of items updated.\nDo you want to see the list? (y = yes) ");
		readConsole(sl);
		if(getConsola().equals("y")) {
			String list = "\nList of items inside the fridge:";
			for(int i=0; i<reply.length; i++) {
				String[] item = reply[i].split("/,");
				list += "\n" + String.valueOf(i+1) + "  Name: " + item[0];
				if(item[1].equals("q")) {
					list += "\n   Quantity: " + item[2];
				}else {
					list += "\n   Weight: " + item[2] + " g";
				}
				list += "\n   Expiration date: " + item[3];
			}
			System.out.println(list);
		}
	}
	private void chooseItems(Slave sl) throws InterruptedException {
		System.out.println("Sorry, we are still working on it! ;)");
		
		getListItems(sl);
		if (getFridgeItems().length < 0.5) {
			System.out.println("\nSorry, there are no items inside the fridge.");
		}
		String correctIntroducedItems = "";
		String   correctPairs = "Correct pairs:\n";
		String incorrectPairs = "Inorrect pairs:\n";
		setConsola("y");
		while(getConsola().equals("y") && getFridgeItems().length > 0) {
			System.out.print("\nChoosing items:"+
					   "\n  Now type the number of the item you want followed"+
					   "\n  by a space and quantity/weight."+
					   "\n  If you want to choose several items please separate"+
					   "\n  the pair of values with a coma and a space."+
					   "\n  Example: 1 2, 4 20\n  ");
			readConsole(sl);
			String[] selection = getConsola().split(", ");
			for(String s: selection) {
				boolean isCorrect = false;
				String name = ""; //name of the product
				String[] pair = s.split(" ");
				try {
					
					int itemNumber = Integer.parseInt(pair[0]);
					if(itemNumber > 0 && itemNumber <= getFridgeItems().length) {
						String[] item = getFridgeItems()[itemNumber-1].split("/,");
						if(item[1].equals("q")) {
							int quantity = Integer.parseInt(pair[1]);
							if(quantity <= 0 || quantity > Integer.parseInt(item[2])) {
								//error
								continue;
							}else {
								//select to remove
								isCorrect = true;
								name = item[0];
							}
						}else {
							double weight = Double.parseDouble(pair[1]);
							if(weight <= 0.0 || weight > Double.parseDouble(item[2])) {
								//error
								continue;
							}else {
								//select to remove
								isCorrect = true;
								name = item[0];
							}
						}
					}else {
						//error
						continue;
					}
				}catch(NumberFormatException e) {
					//error
					continue;
				}
				
				if (isCorrect) {
					if(!correctIntroducedItems.equals("")) {
						correctIntroducedItems += "/;";
					}
					correctIntroducedItems += name + "/," + pair[1];
					correctPairs += "  " + pair[0] + " " + pair[1] + "\n";
				}else {
					incorrectPairs += "  " + pair[0] + " " + pair[1] + "\n";
				}
			}
			
			//print correct and incorrect items
			System.out.println(correctPairs);
			System.out.println(incorrectPairs);
			
			System.out.println("\nDo you want to choose more items? (y = yes)");
			readConsole(sl);
		}
		if (!correctIntroducedItems.equals("")) {
			System.out.println("\nDo you want to choose the selected items? (y = yes)");
			readConsole(sl);
			if(getConsola().equals("y")) {
				String[] reply = sl.rpcComm(sl.CHOOSEITEMS, correctIntroducedItems.split("/;"));
				if (reply[0] == sl.CHOOSEITEMS) {
					System.out.println("Have a nice meal!");
				}
			}
		}
	}
	private void getShoppingList(Slave sl) {
		System.out.println("Sorry, we are still working on it! ;)");
	}
	private void addRecipe(Slave sl) {
		System.out.println("Sorry, we are still working on it! ;)");
	}
	private void getRecipes(Slave sl) {
		System.out.println("Sorry, we are still working on it! ;)");
	}
	private void removeRecip(Slave sl) {
		System.out.println("Sorry, we are still working on it! ;)");
	}
	private void modifyRecip(Slave sl) {
		System.out.println("Sorry, we are still working on it! ;)");
	}
	
	private void testComm(Slave sl) {
		String[] reply = sl.rpcComm(sl.TESTCOMM, new String[]{""});
		System.out.println(reply[0]);
	}
	
	/*
	 * Main.................................................
	 */
	public static void main(String[] args) throws InterruptedException {
		//log.info(sdf.format(new Date()) + "User Start");
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nUser Start");
    	System.out.println(directions);
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nNew action");
		
    	boolean isRunning = true;
    	
    	Slave sl = new Slave();
    	
    	while(isRunning) {
        	Thread.sleep(1000);
        	sl.consola = System.console().readLine();
        	if(sl.consola != null) {
        		
        		sl.params = sl.consola.split(" "); //first split but just to detect if the action is printing the directions or closing the program
        		switch (sl.params[0]) {
	        		case "d":  System.out.println(directions); break;
	        		case "c":
	        			//log.info(sdf.format(new Date()) + "Master controller closing...");
	        			System.out.println("\n\nClosing program...\n");
	        			isRunning = false;
	        			break;
	        		case "ip":  sl.changeIp(sl); break;
	        		case "p":  sl.changePort(sl); break;
	        		
			        case "1":  sl.addItems(sl); break;
			        case "2":  sl.getListItems(sl); break;
			        case "3":  sl.chooseItems(sl); break;
			        case "4":  sl.getShoppingList(sl); break;
			        
			        case "5":  sl.addRecipe(sl); break;
			        case "6":  sl.getRecipes(sl); break;
			        case "7":  sl.removeRecip(sl); break;
			        case "8":  sl.modifyRecip(sl); break;
			        
			        case "0":  sl.testComm(sl); break;
			        
			        default:  System.out.println("Invalid entry, try again!");
			    }
        		
        		if(sl.consola != null && isRunning) {
        			System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nNew action");
        			sl.consola = null;
        			sl.params = null;
        		}
        	}
    	}
		
		
		
	}
}
