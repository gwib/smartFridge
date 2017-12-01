package food;
import java.util.ArrayList;

public class Recipe {
	
	public String name;
	public ArrayList<FoodItem> ingredients;
	public String description;
	
	public Recipe(String n, ArrayList<FoodItem> ing, String d) {
		this.name = n;
		this.ingredients = ing;
		this.description = d;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<FoodItem> getIngredients() {
		return ingredients;
	}
	public void setIngredients(ArrayList<FoodItem> ingredients) {
		this.ingredients = ingredients;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return getName() + ";" + ingredientsToString() + ";" + getDescription();
	}
	
	public String ingredientsToString() {
		String s = "";
		for(FoodItem f: getIngredients()) {
			s += f.toString(",", ":");
		}
		return s;
	}
}
