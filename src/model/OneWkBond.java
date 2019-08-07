package model;

import java.util.*;

public class OneWkBond extends Bond{
	private int numDays;
	private Date maturityDate;
	
	OneWkBond(String name, String b, double p, double ir) {
		super(name, b, p, ir);
		numDays = 7;
		Calendar c = Calendar.getInstance();
		c.setTime(this.getPurchaseDate());
		c.add(Calendar.DAY_OF_MONTH, numDays);
		
		maturityDate = c.getTime();
	}
	
	public Date dateDue() {
		return maturityDate;
	}
	
	public boolean isTotalDue() {
		if(Market.today.compareTo(maturityDate) == 0) {
			return true;
		}
		return false;
	}
	
	public String getOriginalBondLength() {
		return numDays + " days";
	}

}
