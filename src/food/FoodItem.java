package food;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.input.DataFormat;



public class FoodItem {
	private String name;
	private int quantity;
	private Double weight;
    private static String expirationDate;
    private static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    private int expire = 0; // priority default
    

    // Constructor for countable food items

    public FoodItem(String name, int quantity, String expirationDate) {
    	this.name = name;
    	this.quantity = quantity;
    	this.weight = 0.0;
    	this.expirationDate = expirationDate;
    }

    public FoodItem(String name, Double weight, String expirationDate) {
    	this.name = name;
    	this.quantity = 0;
    	this.weight = weight;
    	this.expirationDate = expirationDate;
    }
    
    public FoodItem(String data, String delimiter) {
    	String[] s = data.split(delimiter);
    	this.name = s[0];
    	try {
    		this.quantity = Integer.parseInt(s[1]);
    		this.weight = 0.0;
    	}catch(NumberFormatException e) {
    		this.weight = Double.parseDouble(s[1]);
    		this.quantity = 0;
    	}
    	if (s.length>2) {
    		this.expirationDate = s[2];
    	}
    }

    public void removeQuantityFromItem(int quantityRemoved) {
    	this.quantity = this.getQuantity() - quantityRemoved;
    }

    public void removeWeightFromItem(Double weightRemoved) {
    	this.weight = this.getWeight() - weightRemoved;
    }
    
    public static int calculateDaystogo() throws ParseException {
		int days=0;
		double diff1=0.0;
		double dais=0.0;
		long diff = getExpirationDate_dateFormat().getTime() - new Date().getTime();
		diff1=(double)diff;
		dais=diff1/ (1000 * 60 * 60 * 24);
		days =(int)Math.ceil(dais);
		return days;
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

	public static String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public static Date getExpirationDate_dateFormat() throws ParseException {
		return dateFormat.parse(getExpirationDate());
	}
	
	public  void setExpirePriority(int expire) {
		this.expire = expire;
	}
	public int getExpirePriority() {
		return expire;
	}

	public String toString() {
		String s = "";
		if(getQuantity() == 0) {
			s += getName() + ";" + String.valueOf(getWeight())  +";"+ getExpirationDate()+ "\n";
		}
		else {
			s += getName() + ";" + String.valueOf(getQuantity()) +";"+ getExpirationDate() +"\n";
		}
		return s;
	}
	public String toString(String delimiter, String finalDelimiter) {
		String s = "";
		if(getQuantity() == 0) {
			s += getName() + delimiter + String.valueOf(getWeight())  + delimiter + getExpirationDate()+ finalDelimiter;
		}
		else {
			s += getName() + delimiter + String.valueOf(getQuantity()) + delimiter + getExpirationDate() + finalDelimiter;
		}
		return s;
	}


}