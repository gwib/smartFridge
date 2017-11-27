import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class csvFood {

	
	static String fileName="food.csv";
	
	
	public static void main(String[] args) throws IOException{
		

		 //FoodItem food1= new FoodItem("Watermelon",1,"2017-11-28");
		 //writeCsvFile(food1);
		      
		        // open file input stream
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				reader.readLine();

				// read file line by line
				String line = null;
				Scanner scanner = null;
				int index = 0;
				List<FoodItem> foodList = new ArrayList<>();
				
				while ((line = reader.readLine()) != null) {
					
					scanner = new Scanner(line);
					scanner.useDelimiter(";");
					
					String name = null;
					int quantity = 0;
					double weight = 0.0;
					String expirationDate = null;
					
					while (scanner.hasNext()) {
						String data = scanner.next();
						Object String;
						if (index == 0) {
							
							name = data;
						}
						else if (index == 1) {
							try {
								quantity = Integer.parseInt(data);
							}catch(NumberFormatException e) {
								weight = Double.parseDouble(data);
							}
						}
						else if(index==2) {
							expirationDate=data;
						}
						index++;
					}
					index = 0;
					//foodList.add(food);
					if(quantity == 0) {
						foodList.add(new FoodItem(name,weight, expirationDate));
					}
					else {
						foodList.add(new FoodItem(name, quantity, expirationDate));
					}
				
				}
				
				//close reader
				reader.close();
				String s = "";
				for(FoodItem f: foodList) {
					if(f.getQuantity() == 0) {
						s += f.getName() + ", " + String.valueOf(f.getWeight())  +", "+ f.getExpirationDate()+ "\n";
					}
					else {
						s += f.getName() + ", " + String.valueOf(f.getQuantity()) +", "+ f.getExpirationDate() +"\n";
					}
				}
				System.out.println(s); // Print out the arrayList of foodItems
			
	}

	
	// Delimiter used in CSV file
		private static final String SEMICOLON_DELIMITER = ";";
		private static final String NEW_LINE_SEPARATOR = "\n";
		
		//CSV file header
		    private static final String FILE_HEADER = "name,quantity_weight,expiration_date,unit";

		    FoodItem food1= new FoodItem("Watermelon",1,"2017-11-28");
		    public static void writeCsvFile(FoodItem f) {
		      
		    List<FoodItem> foodList = new ArrayList<>();
		    
		    foodList.add(f);
	    	
		    	FileWriter fileWriter = null;
		    	
		    	try {
		    		fileWriter= new FileWriter(fileName);
		    		
		    		// Write to CSV file header:
		    		fileWriter.append(FILE_HEADER.toString());
		    		
		    		//Add a new line separator after the header
		    		fileWriter.append(NEW_LINE_SEPARATOR);
		    		
		    		for(FoodItem f1: foodList) {
		    			 fileWriter.append(f1.getName());
		    			 fileWriter.append(SEMICOLON_DELIMITER);
		    			 fileWriter.append(Integer.toString(f1.getQuantity()));
		    			 fileWriter.append(SEMICOLON_DELIMITER);
		    			 fileWriter.append(f1.getExpirationDate());
		    			 fileWriter.append(SEMICOLON_DELIMITER);
		    			 fileWriter.append("NaN"); // for the unit
		    			 fileWriter.append(NEW_LINE_SEPARATOR);
		    		}
		    		System.out.println("CSV file was created successfully !!!");
		    	
		      }
		    		catch (Exception e) {
		    			System.out.println("Error in csv File Writer !!!");
		    			e.printStackTrace();
		    		}finally {

		    			try {
		    				 fileWriter.flush();
		    				 fileWriter.close();
		    			} catch (IOException e) {
		    				System.out.println("Error while flushing/closing fileWriter !!!");
		    			    e.printStackTrace();
		    		      }


		    	}

	
		    }
}
