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
	
	public boolean hasBond(Bond toCheck) throws SQLException{
		DBHelper helper = new DBHelper();
		String bondID = toCheck.getID();

		if(helper.marketHasBond(bondID)) {
			return true;
		}
		return false;
	}

	public boolean hasBond(String bondID) throws SQLException{
		DBHelper helper = new DBHelper();
		
		if(helper.marketHasBond(bondID)) {
			return true;
		}
		return false;
	}
	
	public String getAllBond(){
		DBHelper helper = new DBHelper();
		return helper.getAllMarketBond();
	}

	public Bond getBond(Bond toGet) throws SQLException{
		DBHelper helper = new DBHelper();
		String bondID = toGet.getID();
		
		return helper.getMarketBond(bondID);
	}

	public Bond getBond(String toGet) throws SQLException{
		DBHelper helper = new DBHelper();
		return helper.getMarketBond(toGet);
	}
	
	public void updatePrices() throws SQLException{
		DBHelper helper = new DBHelper();
	}

}
