import java.text.SimpleDateFormat;



public class FoodItem {
	String name;
	int quantity;
	Double weight;
    String expirationDate;
    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

    //TODO: Connection to recipes

    // Constructor for countable food items

    public FoodItem(String name, int quantity, String expirationDate) {
    	this.name = name;
    	this.quantity = quantity;
    	this.expirationDate = expirationDate;
    	//TODO: get weight of item 
    }

    

    public FoodItem(String name, Double weight, String expirationDate) {
    	this.name = name;
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


	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	



}