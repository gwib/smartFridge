import java.util.ArrayList;

public class Fridge {
	ArrayList <FoodItem> food;
	Double motor_velocity;
	Double cooling_liquid;
	ArrayList<Double> shelf_temp;
	ArrayList<Double> shelf_weights;
	Double outside_temp;
	int num_shelves;
	ArrayList<Double> shelf_humidity;
	// temperature setpoints
	
	
	public void addFoodItem(FoodItem foodItem) {
		food.add(foodItem);
		System.out.println(foodItem+" added to fridge!");
	}
	
	public void removeFoodItem(FoodItem foodItem) {
		food.remove(foodItem);
		System.out.println(foodItem+" removed!!");
	}
	
	// update setpoints according to rules
	
	// getters and setters
	public ArrayList<Double> getShelf_weights() {
		return shelf_weights;
	}

	public void setShelf_weights(ArrayList<Double> shelf_weights) {
		this.shelf_weights = shelf_weights;
	}

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

}
