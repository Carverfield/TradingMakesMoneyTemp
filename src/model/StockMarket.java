package model;//package TradingMakesMoney;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import Database.DBHelper;
import java.lang.Math;
import java.sql.SQLException;

public class StockMarket extends Market {
	private ArrayList<Stock> stockMarket;
	
	StockMarket(double tf) {
		super(tf);
		stockMarket = new ArrayList<Stock>();
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

		stockMarket.add(toAdd);
		return true;
	}
	
	
	public void updatePrices(Date date) throws SQLException{
		Iterator<Stock> iter = stockMarket.iterator();
		DBHelper helper = new DBHelper();

		Date date1= new java.sql.Date(date.getTime());
		//Needs to be written, will be like getAllStock method but void
		helper.updateStockMarket((java.sql.Date) date1);
		
		while(iter.hasNext()) {
			double randPercent = ThreadLocalRandom.current().nextDouble(-0.05, 0.05);
			randPercent = Math.round(randPercent * 10000.0)/ 10000.0;
			Stock toUpdate = iter.next();
			double newPrice = (toUpdate.getPrice() * randPercent) + toUpdate.getPrice();
			toUpdate.updatePrice(newPrice); 
		}
		
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

