package Testing;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRegExExpirationDate {

	@Test
	public void test() {
		
		//Modify text to test if it works
		//String text = "2017-11-26";
		String text = "17-11-26";
		
		boolean match = text.matches("(\\d{4})\\-(0?0[1-9]|1[012])\\-(0?0[1-9]|[12][0-9]|3[01])");
		assertTrue(match);
		
		
		if(match) {
			System.out.println(text+" is a date.");
		}
		else {
			System.out.println(text+" is NOT a date.");
		}
	}

}
