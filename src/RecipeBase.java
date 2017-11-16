import java.util.ArrayList;

public class RecipeBase {
	ArrayList<Recipe> recipes;
	
	//TODO: read recipes from csv and add these recipes to recipebase
	
	public void addRecipe(Recipe r) {
		this.recipes.add(r);
		//TODO: add to csv-file
	}
	
	public void removeRecipeByName(String n) {
		for (Recipe r : recipes) {
			if (r.getName()==n) {
				recipes.remove(r);
				break;
			}
		}
	}

}
