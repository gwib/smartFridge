import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import food.FoodItem;
import food.Fridge;
import food.Recipe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TestFridgeDrools {
	public static SimpleDateFormat Dates = new SimpleDateFormat("yyyy-MM-dd");
	public static Date today= new Date();
	public static ArrayList <FoodItem> foodRecipe1 = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> foodRecipe2 = new ArrayList <FoodItem>() ;
	public static ArrayList <FoodItem> foodRecipe3 = new ArrayList <FoodItem>() ;
	public static ArrayList <Recipe> recipes = new ArrayList <Recipe>() ;
	public static ArrayList <Recipe> possibleRecipes = new ArrayList <Recipe>() ;
	static int rec;
	static int accumrec;
	//public  static ArrayList <FoodItem> food_in_fridge;
	
	public static void recommendRecipe(Fridge OurFridge) {// 
		 // load up the knowledge base
        
		
        KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("ksession-rules");
		// go !

		for (FoodItem item : OurFridge.getFood()) {
			kSession.insert(item); //inserting in drools all the items inside the fridge
		}
		
		for (Recipe recipe: recipes) {
			accumrec=1;
			
			for (FoodItem recipeingredient : recipe.getIngredients()) {
				//System.out.println(recipeingredient.name+ " "+recipe.name );
				for (FoodItem fridgeingredient : OurFridge.getFood()) {
					if (recipeingredient.getName()==fridgeingredient.getName()) {
						rec=rec+1;// add 1 if there is at least one element of this ingredient in the fridge
					}
					//System.out.println(recipeingredient.name + " "+ recipe.name + " " +rec);
				}//3for
				
				if (rec >= 1) {
					accumrec=accumrec*1;
				}
				else {accumrec=0;}
				//System.out.println(accumrec);
				rec=0;
			}//2for
			if (accumrec==1) {
				possibleRecipes.add(recipe);
			}
		}//1for
        
		for(Recipe possiblerecipe:possibleRecipes) {
			System.out.println(possiblerecipe.getName());
		}
		kSession.fireAllRules();
		kSession.destroy();
	}
	
	public static void rec_pos_recipes(Fridge OurFridge) { // Possible recipes (considering food in fridge but not expDate)
		// load up the knowledge base
    	
    	// go !
        
        for (Recipe recipe: recipes) {
        	accumrec=1;
        	
        	for (FoodItem recipeingredient : recipe.getIngredients()) {
        		
        		for (FoodItem fridgeingredient : OurFridge.getFood()) {
        			if (recipeingredient.getName()==fridgeingredient.getName()) {
        				rec=rec+1;}// add 1 if there is at least one element of this ingredient in the fridge	
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
        	System.out.println(posrecipe.getName());
        }
	}
	
	public static void main(String[] args) {
		Fridge OurFridge = new Fridge();

        FoodItem item1 = new FoodItem("chicken", 250.0, "2017-11-28");
    	OurFridge.addFoodItem(item1);
    	FoodItem item2 = new FoodItem("fish", 50.0, "2017-11-28");
    	//OurFridge.addFoodItem(item2);
    	FoodItem item3 = new FoodItem("apple", 10, "2017-12-27");
    	OurFridge.addFoodItem(item3);

		foodRecipe1.add(item1);
		foodRecipe1.add(item2);
		foodRecipe2.add(item3);
		foodRecipe2.add(item1);
		foodRecipe3.add(item3);
		Recipe recipe1 = new Recipe("chicken and fish",foodRecipe1,"pollo y pescao");
		Recipe recipe2 = new Recipe("chicken and apples",foodRecipe2,"pollo y manzanas");
		Recipe recipe3 = new Recipe("apples",foodRecipe3,"manzanas");
		recipes.add(recipe1);
		recipes.add(recipe2);
		recipes.add(recipe3);
		
		System.out.println("\n---------------\nrec_pos_recipes\n");
		rec_pos_recipes(OurFridge);
		
		System.out.println("\n---------------\nrecommendRecipe\n");
		//recommendRecipe(OurFridge);
	}

}
