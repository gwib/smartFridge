package food;
import java.util.ArrayList;

public class Fridge {
	private ArrayList <FoodItem> food;
	private Double motor_velocity;
	private Double cooling_liquid;
	private ArrayList<Double> shelf_temp;
	private ArrayList<Double> shelf_weights;
	private Double outside_temp;
	private int num_shelves;
	private ArrayList<Double> shelf_humidity;
	private ArrayList <FoodItem> food_to_expire = new ArrayList <FoodItem>();
	private ArrayList <Recipe> possibleRecipes = new ArrayList <Recipe>();
	
	public Fridge(ArrayList <FoodItem> f,
			Double mv,
			Double cl,
			ArrayList<Double> st,
			//ArrayList<Double> sw,
			Double ot,
			int ns,
			ArrayList<Double> sh) {
		food = f;
		motor_velocity = mv;
		cooling_liquid = cl;
		shelf_temp = st;
		//shelf_weights = sw;
		outside_temp = ot;
		num_shelves = ns;
		shelf_humidity = sh;
	}
	
	public Fridge() {
		food = new ArrayList <FoodItem>();
		motor_velocity = null;
		cooling_liquid = null;
		shelf_temp = new ArrayList <Double>();
		//shelf_weights = new ArrayList <Double>();
		outside_temp = null;
		num_shelves = 0;
		shelf_humidity = new ArrayList <Double>();
	}
	
	public void addFoodItem(FoodItem foodItem) {
		food.add(foodItem);
		System.out.println(foodItem.getName()+" added to fridge!");
	}
	
	public void removeFoodItem(FoodItem foodItem) {
		food.remove(foodItem);
		System.out.println(foodItem.getName()+" removed!!");
	}
	
	// getters and setters

	public ArrayList<Double> getShelf_humidity() {
		return shelf_humidity;
	}

	public void setShelf_humidity(ArrayList<Double> shelf_humidity) {
		this.shelf_humidity = shelf_humidity;
	}
	public ArrayList<Double> getShelf_temp() {
		return shelf_temp;
	}

	public void setShelf_temp(ArrayList<Double> shelf_temp) {
		this.shelf_temp = shelf_temp;
	}

	public int getNum_shelves() {
		return num_shelves;
	}

	public void setNum_shelves(int num_shelves) {
		this.num_shelves = num_shelves;
	}

	public void setFood(ArrayList<FoodItem> food) {
		this.food = food;
	}

	public ArrayList<FoodItem> getFood() {
		return food;
	}
	public Double getMotor_velocity() {
		return motor_velocity;
	}
	public void setMotor_velocity(Double motor_velocity) {
		this.motor_velocity = motor_velocity;
	}
	public Double getCooling_liquid() {
		return cooling_liquid;
	}
	public void setCooling_liquid(Double cooling_liquid) {
		this.cooling_liquid = cooling_liquid;
	}
	public Double getOutside_temp() {
		return outside_temp;
	}
	public void setOutside_temp(Double outside_temp) {
		this.outside_temp = outside_temp;
	}
	
	public ArrayList <FoodItem> getFood_to_expire() {
		return food_to_expire;
	}

	public void setFood_to_expire( FoodItem expirationItem) {
		food_to_expire.add(expirationItem);
		
	}
    
	public void clearFood_to_expire() {
		food_to_expire.clear();
		System.out.println("list was cleared");
	}

	public ArrayList <Recipe> getPossibleRecipes() {
		return possibleRecipes;
	}

	public void setPossibleRecipes(ArrayList <Recipe> possibleRecipes) {
		this.possibleRecipes = possibleRecipes;
	}
}
