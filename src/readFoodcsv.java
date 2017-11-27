import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class readFoodcsv {

	public static void main(String[] args) throws IOException{
		      
		        // open file input stream
				BufferedReader reader = new BufferedReader(new FileReader("food.csv"));
				reader.readLine();

				// read file line by line
				String line = null;
				Scanner scanner = null;
				int index = 0;
				List<FoodItem> foodList = new ArrayList<>();
				
				while ((line = reader.readLine()) != null) {
					FoodItem food = new FoodItem(line, null, null);
					scanner = new Scanner(line);
					scanner.useDelimiter(";");
					
					String name = null;
					int quantity = 0;
					double weight = 0.0;
					String expirationDate = null;
					
					while (scanner.hasNext()) {
						String data = scanner.next();
						if (index == 0) {
							//food.setName(data);
							name = data;
						}
						else if (index == 1) {
							try {
								//food.setQuantity(Integer.parseInt(data));
								quantity = Integer.parseInt(data);
							}catch(NumberFormatException e) {
								System.out.println("Double!!!");
								//food.setWeight(Double.parseDouble(data));
								weight = Double.parseDouble(data);
							}
						}
						else
							System.out.println("invalid data::" + data);
						index++;
						//System.out.println(data);
					}
					index = 0;
					//foodList.add(food);
					if(quantity == 0) {
						foodList.add(new FoodItem(name,weight,null));
					}
					else {
						foodList.add(new FoodItem(name, quantity, null));
					}
				
				}
				
				//close reader
				reader.close();
				String s = "";
				for(FoodItem f: foodList) {
					if(f.getQuantity() == 0) {
						s += f.getName() + ", " + String.valueOf(f.getWeight()) + "\n";
					}
					else {
						s += f.getName() + ", " + String.valueOf(f.getQuantity()) + "\n";
					}
				}
				System.out.println(s); // Print out the arrayList of foodItems
			
				
	}

}
