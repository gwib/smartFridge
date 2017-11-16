import java.text.SimpleDateFormat;

public class FoodItem {
	String name;
	int quantity;
	Double weight;
	int cooking_time; // in minutes
	String typeOfFood;
    SimpleDateFormat expirationDate = new SimpleDateFormat("yyyy-MM-dd");
    //TODO: Connection to recipes
	

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public int getCooking_time() {
		return cooking_time;
	}
	public void setCooking_time(int cooking_time) {
		this.cooking_time = cooking_time;
	}
	public String getTypeOfFood() {
		return typeOfFood;
	}
	public void setTypeOfFood(String typeOfFood) {
		this.typeOfFood = typeOfFood;
	}
	public SimpleDateFormat getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(SimpleDateFormat expirationDate) {
		this.expirationDate = expirationDate;
	}
	

}
