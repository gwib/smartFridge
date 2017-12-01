package food;
//package com.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
/**
 * This is a sample class to launch a rule.
 */
public class ShoppingList {
	public static SimpleDateFormat Dates = new SimpleDateFormat("yyyy-MM-dd");
	public static Date today= new Date();
	public static ArrayList <FoodItem> foodRecipe1 = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> foodRecipe2 = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> foodRecipe3 = new ArrayList <FoodItem>() ;
	public static ArrayList <Recipe> Recipes = new ArrayList <Recipe>() ;
	public static ArrayList <FoodItem> totalIngredients = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> shoppingList = new ArrayList <FoodItem>() ;
	static int rec;
	static int accumrec;
	//public  static ArrayList <FoodItem> food_in_fridge;
	
    public static String getShoppingList(Fridge OurFridge) {
    	
            
            
            //shoppingList = new ArrayList<FoodItem>();
            //totalIngredients = new ArrayList<FoodItem>();
            shoppingList.clear();
            totalIngredients.clear();
            for (Recipe recipe: Recipes) {
            	for (FoodItem ingredient : recipe.getIngredients()) {
            		totalIngredients.add(ingredient);
            	}
            }
             HashSet <FoodItem> hashset = new HashSet <FoodItem> (totalIngredients);
             totalIngredients.clear();
             totalIngredients.addAll(hashset);
            
             for (FoodItem ingredient : totalIngredients) {
         		
         		for (FoodItem fridgeingredient : OurFridge.getFood()) {
         			if (ingredient.getName()==fridgeingredient.getName()) {
         				rec=rec+1;// add 1 if there is at least one element of this ingredient in the fridge	
         		}//3for
         		
         		
         	}//2for
         	if (rec==0) {
         		shoppingList.add(ingredient);}
         	rec=0;
         	}// if accumrec
            String list = "Shopping list ("+shoppingList.size()+" items):";
            for (FoodItem item : shoppingList) {
            	list += "\n - " + item.getName();
            }
            System.out.println(list);
            // Insert this function in the Master and it should return this list to the slave
            return list;
  
    }
    
    public static final void main(String[] args) throws ParseException {
    	// load up the knowledge base
    	Fridge OurFridge = new Fridge();
    	
        
        // go !
    	
        
		// create food items
        FoodItem item1 = new FoodItem("chicken", 250.0, "2017-11-28");
        OurFridge.addFoodItem(item1);
        FoodItem item2 = new FoodItem("fish", 50.0, "2017-11-30");
        //OurFridge.addFoodItem(item2);
        FoodItem item3 = new FoodItem("apple", 10, "2017-12-27");
        OurFridge.addFoodItem(item3);
        
        
        
        // create recipes
        foodRecipe1.add(item1);
        foodRecipe1.add(item2);
        foodRecipe2.add(item3);
        foodRecipe2.add(item1);
        foodRecipe3.add(item3);
        
        Recipe recipe1 = new Recipe("chicken and fish",foodRecipe1,"pollo y pescao");
        Recipe recipe2 = new Recipe("chicken and apples",foodRecipe2,"pollo y manzanas");
        Recipe recipe3 = new Recipe("apples",foodRecipe3,"manzanas");
        Recipes.add(recipe1);
        Recipes.add(recipe2);
        Recipes.add(recipe3);
        
    	for(int i=0; i<4;i++) {
    		System.out.println("\nList"+Integer.toString(i));
    		getShoppingList(OurFridge);
    	}
    }
}
