package model;

import java.util.*;

public class OneMonthBond extends Bond{
	private int numMonth;
	private Date maturityDate;
	
	OneMonthBond(String name, String b, double p, double ir) {
		super(name, b, p, ir);
		numMonth = 1;
		Calendar c = Calendar.getInstance();
		c.setTime(this.getPurchaseDate());
		c.add(Calendar.MONTH, numMonth);
		
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
		return numMonth + " month";
	}

}
