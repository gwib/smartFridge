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
/**
 * This is a sample class to launch a rule.
 */
public class Recommender {
	public static SimpleDateFormat Dates = new SimpleDateFormat("yyyy-MM-dd");
	public static Date today= new Date();
	public static ArrayList <FoodItem> foodRecipe1 = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> foodRecipe2 = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> foodRecipe3 = new ArrayList <FoodItem>() ;
	public static ArrayList <Recipe> Recipes = new ArrayList <Recipe>() ;
	private static ArrayList <Recipe> possibleRecipes = new ArrayList <Recipe>() ;
	private static ArrayList <Recipe> recommendedRecipes = new ArrayList <Recipe>() ;
	static int rec;
	static int accumrec;
	//public  static ArrayList <FoodItem> food_in_fridge;
	
    public static final void main(String[] args) throws ParseException {

		// load up the knowledge base
		Fridge OurFridge = new Fridge();
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("ksession-rules");	

		// go !


		// create food items
		FoodItem item1 = new FoodItem("chicken", 250.0, "2017-11-28");
		OurFridge.addFoodItem(item1);
		FoodItem item2 = new FoodItem("fish", 50.0, "2017-11-30");
		OurFridge.addFoodItem(item2);
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

		kSession.insert(OurFridge);
		for (FoodItem item : OurFridge.getFood()) {
			kSession.insert(item);
			
		}


		for (Recipe recipe: Recipes) {
			accumrec=1;
			
			for (FoodItem recipeingredient : recipe.getIngredients()) {
				
				for (FoodItem fridgeingredient : OurFridge.getFood()) {
					if (recipeingredient.getName()==fridgeingredient.getName()) {
						rec=rec+1;// add 1 if there is at least one element of this ingredient in the fridge
					}
				}//3for

				if (rec >= 1) {
					accumrec=accumrec*1; // this is working as an "and" for every ingredient that is in the fridge
				}
				else {accumrec=0;}// as soon as 1 ingredient is not in the fridge, do not recommend
					
				rec=0;
			}//2for
			if (accumrec==1) {
				possibleRecipes.add(recipe);
			}// if accumrec
		}//1for}
   
		OurFridge.setPossibleRecipes(possibleRecipes);
		for (Recipe posrecipe : OurFridge.getPossibleRecipes()) {
			kSession.insert(posrecipe);
			
		}
   
   
		kSession.fireAllRules();
   
		for (Recipe recipe: possibleRecipes) {
			accumrec=0;
			
			for (FoodItem recipeingredient : recipe.getIngredients()) {
				
				for (FoodItem fridgeingredient : OurFridge.getFood_to_expire()) {
					if (recipeingredient.getName()==fridgeingredient.getName()) {
						accumrec=1;}// add 1 if there is at least one element of this ingredient in the fridge	
				}//3for
			}//2for
			if (accumrec==1) {
				recommendedRecipes.add(recipe);	
			}// if accumrec
		}//1for}
		for (Recipe recipe: recommendedRecipes) {
			System.out.println(recipe.getName());
		}
   
		kSession.destroy();    

    }
    
}

	