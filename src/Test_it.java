import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import de.erichseifert.gral.io.data.CSVReader;
import food.FoodItem;
import food.Recipe;
import food.csvFood;

public class Test_it {
	
	
	private static void isMatchingSimple(String text) {
		boolean match = text.matches("(\\d{4})\\-(0?0[1-9]|1[012])\\-(0?0[1-9]|[12][0-9]|3[01])");
		if(match) {
			System.out.println(text+" is a date.");
		}
		else {
			System.out.println(text+" is NOT a date.");
		}
	}
	
	
	private static void isMatching(String text) {
		boolean match = text.matches("(\\d{4})\\-(0?0[1-9]|1[012])\\-(0?0[1-9]|[12][0-9]|3[01])");
		if(match) {
			System.out.println(text+" is a date.");
			String[] inDate = text.split("-"); //["yyyy","MM","dd"]
			//System.out.println("inDate: any="+inDate[0]+" mes="+inDate[1]+" dia="+inDate[2]);
			try {
				Date expDate = new Date((Integer.parseInt(inDate[0])-1900),Integer.parseInt(inDate[1]), Integer.parseInt(inDate[2]));
				Date now = new Date();
				Date currentDate = new Date(now.getYear(),now.getMonth(),now.getDate());
				System.out.println("   "+text+" = "+ String.valueOf(expDate.getTime()/1000000000));
				System.out.println("   "+expDate.toString()+" = "+ String.valueOf(expDate.getTime()/1000000000));
				System.out.println("   "+currentDate.toString()+" = "+ String.valueOf(currentDate.getTime()/1000000000));
				if(expDate.getTime() > currentDate.getTime()) {
					System.out.println("      Future date!!!");
					return;
				}
				else {
					System.out.println("      Past date...");
					//setConsola(null);
				}
			}
			catch(NumberFormatException e) {
				System.out.print("      Not integer\n");
				//setConsola(null);
				
			}
			
		}
		else {
			System.out.println(text+" is NOT a date.");
		}
	}
	
	private static FoodItem str2foodItem(String str) {
		FoodItem f = null;
		String[] item = str.split("/,");
		String name = item[0];
		String expirationDate = null;//item[3]; //TODO: correct type of expiration date
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
	
	public static void testPrintRecipes() throws IOException {
		ArrayList<FoodItem> fi = new ArrayList<FoodItem>();
		fi.add(new FoodItem("Rice",1,"2017-11-28"));
		fi.add(new FoodItem("Ginger",10.0,"2017-11-28"));
		fi.add(new FoodItem("Curry",4,"2017-11-28"));
		for(FoodItem f: fi) {
			//System.out.println(f.toString(",", ":"));
		}
		
		csvFood csv = new csvFood("Recipe.csv","Recipe_Name;FoodItems;Description");
		ArrayList<Recipe> recipes = csv.readCsvRecipie();
		//ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(new Recipe("truita",fi,"descripcio"));
		for(Recipe r: recipes) {
			//System.out.println(r.toString());
			System.out.println("\nName:");
			System.out.println(r.getName());
			System.out.println("\nIngredients:");
			System.out.println(r.getIngredients().toString());
			System.out.println("\nDescription:");
			System.out.println(r.getDescription());
		}
	}
	
	public static void testGetExpDate_dateFormat() throws ParseException {
		ArrayList<FoodItem> fi = new ArrayList<FoodItem>();
		fi.add(new FoodItem("Rice",1,"2017-12-28"));
		fi.add(new FoodItem("Ginger",10.0,"2018-01-28"));
		fi.add(new FoodItem("Curry",4,"2018-02-28"));
		for(FoodItem f: fi) {
			System.out.println(f.getExpirationDate_dateFormat());
		}
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		/*
		isMatching("1234-12-12");
		isMatching("12341-12-12");
		isMatching("1234-1-12");
		isMatching("1234-12-40");
		System.out.println("");
		isMatching("1231-02-12");
		isMatching("1231-12-31");
		isMatching("1231-13-12");
		isMatching("1231-12-32");
		System.out.println("");
		isMatching("1232-0-12");
		isMatching("1232-10-0");
		isMatching("1232-01");
		isMatching("1231-12-30-12");
		System.out.println("");
		isMatching("hola");
		isMatching("2017-12-12");
		isMatching("2020-12-12");
		isMatching("2016-12-12");
		System.out.println("");
		isMatching("2017-11-23");
		isMatching("2017-11-24");
		isMatching("2020-11-25");
		isMatching("2016-11-26");
		*//*
		Fridge fridge = new Fridge();
		String[] items = {"Patata/,w/,500/,1111-11-11"};//,"Apple/,q/,4/,1112-12-12"};
		for (String i: items) {
			fridge.addFoodItem(str2foodItem(i));
		}*/
		/*
		csvFood csv = new csvFood();
		System.out.println(csv.getFileName());
		
		ArrayList<FoodItem> fi = new ArrayList<FoodItem>();
		try {
			fi = csvFood.readCsv();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fi.add(new FoodItem("Watermelon",1,"2017-11-28"));
		fi.add(new FoodItem("Ginger",10.0,"2017-11-28"));
		fi.add(new FoodItem("Curry",4,"2017-11-28"));
		csvFood.writeCsvFile(fi);
		try {
			csvFood.readCsv();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*
		ArrayList<Recipe> recipies = new ArrayList<Recipe>();
		System.out.println("rec.len = " + String.valueOf(recipies.size()));
		csvFood csv = new csvFood("Recipe.csv","Recipe_Name;FoodItems;Description");
		try {
			recipies = csv.readCsvRecipie();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("rec.len = " + String.valueOf(recipies.size()));
		
		
		ArrayList<FoodItem> fi = new ArrayList<FoodItem>();
		fi.add(new FoodItem("Rice",1,"2017-11-28"));
		fi.add(new FoodItem("Ginger",10.0,"2017-11-28"));
		fi.add(new FoodItem("Curry",4,"2017-11-28"));
		recipies.add(new Recipe("Spicy rice",fi,"Steps to cook"));
		System.out.println("rec.len = " + String.valueOf(recipies.size()));
		
		csv.writeCsvRecipe(recipies);
		
		try {
			recipies = csv.readCsvRecipie();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("rec.len = " + String.valueOf(recipies.size()));
		*/
		
		testPrintRecipes();
		
		//testGetExpDate_dateFormat();
		
	}
}
