package model;//package TradingMakesMoney;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import Database.DBHelper;
import java.lang.Math;
import java.sql.SQLException;

public class StockMarket extends Market {
	
	StockMarket(double tf) {
		super(tf);
	}
	
	public boolean addStock(Stock toAdd, Date date) throws SQLException{
		if (this.hasStock(toAdd)) {
			return false;
		}
		
		Date date1= new java.sql.Date(date.getTime());
		String companyName = toAdd.getName();
		String ticker = toAdd.getTicker();
		double price = toAdd.getPrice();

		DBHelper helper = new DBHelper();
		//Needs to be written
		helper.addMarketStock((java.sql.Date)date1, companyName, ticker, price);

		return true;
	}
	
	public boolean removeStock(String ticker) throws SQLException{
		if(this.hasStock(ticker)) {
			return false;
		}
		DBHelper helper = new DBHelper();
		helper.removeMarketStock(ticker);
		
		return true;
	}
	
	
	public void updatePrices(Date date) throws SQLException{
		DBHelper helper = new DBHelper();

		Date date1= new java.sql.Date(date.getTime());
		helper.updateStockMarket((java.sql.Date) date1);
		
	}

	public String getAllStock(){
		DBHelper helper = new DBHelper();
		return helper.getAllMarketStock();
	}
	
	public boolean hasStock(String ticker) throws SQLException{
		DBHelper helper = new DBHelper();
		if(helper.marketHasStock(ticker)) {
			return true;
		}
		return false;
	}
	
	public boolean hasStock(Stock toCheck) throws SQLException{
		DBHelper helper = new DBHelper();
		String ticker = toCheck.getTicker();
		if(helper.marketHasStock(ticker)) {
			return true;
		}
		return false;
		
	}
	
	public Stock getStock(Stock toGet) throws SQLException{
		DBHelper helper = new DBHelper();
		String ticker = toGet.getTicker();
		
		return helper.getMarketStock(ticker);
	}
	
	public Stock getStock(String ticker) throws SQLException{
		DBHelper helper = new DBHelper();
		
		return helper.getMarketStock(ticker);
	}
	
	
}

