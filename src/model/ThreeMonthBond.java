package model;

import java.util.Calendar;
import java.util.Date;

public class ThreeMonthBond extends Bond{

	private int numMonth;
	private Date maturityDate;
	
	ThreeMonthBond(String name, double p, double ir) {
		super(name, p, ir);
		numMonth = 3;
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
