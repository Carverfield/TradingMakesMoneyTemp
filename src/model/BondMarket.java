package model;//package TradingMakesMoney;

import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import Database.DBHelper;
import java.sql.SQLException;

public class BondMarket extends Market {
	
	BondMarket(double tf) {
		super(tf);
	}
	
	public boolean addBond(Bond toAdd) throws SQLException{
		if(this.hasBond(toAdd)) {
			return false;
		}
		DBHelper helper = new DBHelper();
		String companyName = toAdd.getIssuer();
		String bondID = toAdd.getID();
		String type = toAdd.getClass().getSimpleName();
		double price = toAdd.getPrice();
		double yield = toAdd.getYield();
		//Needs to be written
		helper.addMarketBond(companyName, type, yield, price, bondID);
		return true;
	}
	
	public boolean removeBond(Bond toRemove) throws SQLException{
		if(!this.hasBond(toRemove)) {
			return false;
		}
		DBHelper helper = new DBHelper();
		String bondID = toAdd.getID();
		//needs to be written
		helper.removeMarketBond(bondID);
		return true;
	}

	public boolean removeBond(String bondID) throws SQLException{
		if(!this.hasBond(bondID)) {
			return false;
		}
		DBHelper helper = new DBHelper();
		//needs to be written
		helper.removeMarketBond(bondID);
		return true;
	}
	
	public boolean hasBond(Bond toCheck) {
		DBHelper helper = new DBHelper();
		String bondID = toCheck.getID();

		if(helper.marketHasBond(bondID)) {
			return true;
		}
		return false;
	}

	public boolean hasBond(String bondID) {
		DBHelper helper = new DBHelper();
		
		if(helper.marketHasBond(bondID)) {
			return true;
		}
		return false;
	}
	
	public Bond getBond(Bond toGet) {
		Iterator<Bond> iter = bondMarket.iterator();
		
		while (iter.hasNext()) {
			Bond ex = iter.next();
			if(ex.equals(toGet)) {
				return ex;
			}
		}
		return new OneWkBond("Empty", 0, 0);
	}

	public Bond getBond(String toGet) {
		Iterator<Bond> iter = bondMarket.iterator();
		
		while (iter.hasNext()) {
			Bond ex = iter.next();
			if(ex.getID().equals(toGet)) {
				return ex;
			}
		}
		return new OneWkBond("Empty", 0, 0);
	}
	
	public void updatePrices() throws SQLException{

		DBHelper helper = new DBHelper();
		helper.updateBondMarket();
		//Needs to be written, will be like 


		Iterator<Bond> iter = bondMarket.iterator();
		
		while (iter.hasNext()) {
			double randPercent = ThreadLocalRandom.current().nextDouble(-0.05, 0.05);
			randPercent = Math.round(randPercent * 10000.0)/ 10000.0;
			Bond toUpdate = iter.next();
			double newPrice = (toUpdate.getPrice() * randPercent) + toUpdate.getPrice();
			double newYield = toUpdate.getYield();
			
			//if the percent change in price is negative, yield should go up and vice versa
			if ((newYield - randPercent) <= 0) {
				newYield = 0.01;
			}
			else {
				newYield -= randPercent;
			}
			
			toUpdate.updatePrice(newPrice);
			toUpdate.updateYield(newYield);
		}
		
	}

}
