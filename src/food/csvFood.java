package food;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class csvFood {
	
	//Default values
	private static String fileName ="food.csv";
	private static String fileHeader = "name;quantity_weight;expiration_date,unit";
	private static String delimiter = ";";
	private static String newLineSeparator = "\n";

	public csvFood(String fileName, String fileHeader, String delimiter, String newLineSeparator) {
		this.fileName = fileName;
		this.fileHeader = fileHeader;
		this.delimiter = delimiter;
		this.newLineSeparator = newLineSeparator;
	}
	
	public csvFood(String fileName, String fileHeader) {
		this.fileName = fileName;
		this.fileHeader = fileHeader;
	}
	
	public csvFood() {
		//Will work with the default values
	}
	
	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		csvFood.fileName = fileName;
	}

	public static String getFileHeader() {
		return fileHeader;
	}

	public static void setFileHeader(String fileHeader) {
		csvFood.fileHeader = fileHeader;
	}

	public static String getDelimiter() {
		return delimiter;
	}

	public static void setDelimiter(String delimiter) {
		csvFood.delimiter = delimiter;
	}

	public static String getNewLineSeparator() {
		return newLineSeparator;
	}

	public static void setNewLineSeparator(String newLineSeparator) {
		csvFood.newLineSeparator = newLineSeparator;
	}
	
	
	public static ArrayList<Recipe> readCsvRecipie() throws IOException {
		ArrayList<Recipe> recipies = new ArrayList<Recipe>();
		String name = null;
		ArrayList<FoodItem> ingredients = new ArrayList<FoodItem>();
		String description = null;
		
		BufferedReader reader = new BufferedReader(new FileReader(getFileName()));
		reader.readLine();
		String line = null;
		Scanner scanner = null;
		while ((line = reader.readLine()) != null) {
			int index = 0;
			scanner = new Scanner(line);
			System.out.println(scanner.toString());
			scanner.useDelimiter(";");
			while (scanner.hasNext()) {
				String data = scanner.next();
				Object String;
				if (index == 0) {
					//System.out.println("data[0] = "+data);
					name = data;
					//System.out.println("name: "+name);
				}else if (index == 1) {
					/*
					 * 
					System.out.println("data[1] = "+data);
					Reader inputString = new StringReader(data);
				    BufferedReader foodReader = new BufferedReader(inputString);
					ingredients = readFood(foodReader);
					*/
					ingredients = new ArrayList<FoodItem>();
					String[] list = data.split(":");
					for (String l: list) {
						ingredients.add(new FoodItem(l, ","));
					}
					
					//System.out.println("ingredients: "+s);
				}else if (index == 2) {
					//System.out.println("data[2] = "+data);
					description = data;
					//System.out.println("description: "+description);
				}
				index++;
			}
	    	recipies.add(new Recipe(name,ingredients,description));
		}
		return recipies;
	}
		
	public static ArrayList<FoodItem> readCsv() throws IOException{
		// open file input stream
		BufferedReader reader = new BufferedReader(new FileReader(getFileName()));
		reader.readLine();
		
		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;
		ArrayList<FoodItem> foodList = new ArrayList<>();
		
		while ((line = reader.readLine()) != null) {
			
			scanner = new Scanner(line);
			scanner.useDelimiter(getDelimiter());
		
			String name = null;
			int quantity = 0;
			double weight = 0.0;
			String expirationDate = null;
		
			while (scanner.hasNext()) {
				String data = scanner.next();
				Object String;
				if (index == 0) {
					name = data;
				}else if (index == 1) {
					try {
						quantity = Integer.parseInt(data);
					}catch(NumberFormatException e) {
						weight = Double.parseDouble(data);
					}
				}else if(index == 2) {
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
			System.out.println(f.toString());
		}
		System.out.println(s); // Print out the arrayList of foodItems
		return foodList;
	}

	public static void writeCsvFile(ArrayList<FoodItem> foodList) {
		
		FileWriter fileWriter = null;
		try {
			fileWriter= new FileWriter(getFileName());
			fileWriter.append(getFileHeader()); // Write to CSV file header
			fileWriter.append(getNewLineSeparator()); //Add a new line separator after the header
	
			for(FoodItem f: foodList) {
				 fileWriter.append(f.getName());
				 fileWriter.append(getDelimiter());
				 if(f.getQuantity() == 0) {
					 fileWriter.append(Double.toString(f.getWeight()));
					 
				 }
				 else {
					 fileWriter.append(Integer.toString(f.getQuantity()));
				 }
				 fileWriter.append(getDelimiter());
				 fileWriter.append(f.getExpirationDate());
				 fileWriter.append(getDelimiter());
				 fileWriter.append("NaN"); // for the unit
				 fileWriter.append(getNewLineSeparator());
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
			}catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
			    e.printStackTrace();
			}
		}
	}
	
public static void writeCsvRecipe(ArrayList<Recipe> recipeList) {
		
		FileWriter fileWriter = null;
		try {
			fileWriter= new FileWriter(getFileName());
			fileWriter.append(getFileHeader()); // Write to CSV file header
			fileWriter.append(getNewLineSeparator()); //Add a new line separator after the header
	
			for(Recipe r: recipeList) {
				 fileWriter.append(r.getName());
				 fileWriter.append(getDelimiter());
				 fileWriter.append(r.ingredientsToString());
				 fileWriter.append(getDelimiter());
				 fileWriter.append(r.getDescription());
				 fileWriter.append(getNewLineSeparator());
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
			}catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
			    e.printStackTrace();
			}
		}
	}

	private static String foodItems2str(ArrayList<FoodItem> foodList) {
		String s="\"";
		for(FoodItem f: foodList) {
			s += f.toString();
		}
		return s += "\"";
	}
}
