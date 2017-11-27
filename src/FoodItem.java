import java.text.SimpleDateFormat;

public class FoodItem {
	String name;
	int quantity;
	Double weight;
	int cooking_time; // in minutes
    SimpleDateFormat expirationDate = new SimpleDateFormat("yyyy-MM-dd");
    //TODO: Connection to recipes
    
    // Constructor for countable food items
    public FoodItem(String name, int quantity, SimpleDateFormat expirationDate) {
    	this.name = name;
    	this.quantity = quantity;
    	this.weight = 0.0;
    	this.expirationDate = expirationDate;
    }
    
    public FoodItem(String name, Double weight, SimpleDateFormat expirationDate) {
    	this.name = name;
    	this.quantity = 0;
    	this.weight = weight;
    	this.expirationDate = expirationDate;
    }
    

	
    public void removeQuantityFromItem(int quantityRemoved) {
    	this.quantity = this.getQuantity() - quantityRemoved;
    }
    
    public void removeWeightFromItem(Double weightRemoved) {
    	this.weight = this.getWeight() - weightRemoved;
    }

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
	public SimpleDateFormat getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(SimpleDateFormat expirationDate) {
		this.expirationDate = expirationDate;
	}
	

}
