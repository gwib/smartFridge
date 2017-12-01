package Testing;

import static org.junit.Assert.*;

import org.junit.Test;

import food.FoodItem;

public class Test_str2foodItem {
	
	@Test
	public void test() {
		
		String str = "Apple/,q/,2/,2017-11-10";
		
		FoodItem f = null;
		String[] item = str.split("/,");
		String name = item[0];
		String expirationDate = null;//item[3]; //TODO: correct type of expiration date
		if(item[1].equals("q")) {
			int quantity = Integer.parseInt(item[2]);
			f = new FoodItem(name, quantity, expirationDate);
		}
		else {
			double weight = Double.parseDouble(item[2]);
			f = new FoodItem(name, weight, expirationDate);
		}
		assertEquals(f.toString("/,",""), str);
		

	}

}
