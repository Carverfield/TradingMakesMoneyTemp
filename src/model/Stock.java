package model;

public class Stock {
	private String ticker;
	private double price;
	private String name;
	
	
	Stock(String t, String n, double p) {
		ticker = t;
		name = n;
		price = p;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public boolean updatePrice(double newPrice) {
		if (newPrice < 0) {
			return false;
		}
		price = newPrice;
		return true;
	}
	
	public boolean equals(Stock s) {
		if (s == this) {
			return true;
		}
		return ticker.equals(s.getTicker()) && name.equals(s.getName());
	}
	
}
