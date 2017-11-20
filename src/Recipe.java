import java.util.ArrayList;

public class Recipe {
	
	public String name;
	public ArrayList<FoodItem> ingredients;
	public ArrayList<Double> quantities;
	public String description;
	
	public Recipe(String n, ArrayList<FoodItem> ing, ArrayList<Double> q, String d) {
		this.name = n;
		this.ingredients = ing;
		this.quantities = q;
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
	public ArrayList<Double> getQuantities() {
		return quantities;
	}
	public void setQuantities(ArrayList<Double> quantities) {
		this.quantities = quantities;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
