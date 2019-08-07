package Database;

import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

import model.*;

/**
 * This is a helper class writig data into database.
 * GUI don't need to have read this class.
 */
public class DBHelper {
    static Statement statement = null;
    static ResultSet resultSet = null;

    public static void addInvestorStock(String ticker, String companyName, int numShare, double buyPrice) throws SQLException{
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();

            String sql = "insert into investorStock values(?,?,?,?)";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1,ticker);
            ptmt.setString(2, companyName);
            ptmt.setInt(3, numShare);
            ptmt.setDouble(4, buyPrice);
            ptmt.execute();

            //System.out.println("succeed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMarketStock(Date date, String companyName, String ticker, double price) throws SQLException{
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();

            String sql = "insert into stockMarket values(?,?,?,?)";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setDate(1, date);
            ptmt.setString(2, companyName);
            ptmt.setString(3, ticker);
            ptmt.setDouble(4, price);
            ptmt.execute();

            //System.out.println("succeed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMarketBond(String companyName, String type, double yield, double price, String bondID) throws SQLException{
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();

            String sql = "insert into bondMarket values(?,?,?,?,?)";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, companyName);
            ptmt.setString(2, type);
            ptmt.setDouble(3, yield);
            ptmt.setDouble(4, price);
            ptmt.setString(5, bondID);
            ptmt.execute();

            //System.out.println("succeed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void removeMarketStock(String ticker) throws SQLException {
    	try {
    		Connection conn = DB.getConnection();
    		String sql = "delete from stockMarket where Ticker = ?";
    		PreparedStatement ptmt = conn.prepareStatement(sql);
    		ptmt.setString(3, ticker);
    		
    		ptmt.execute();
    				
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void removeMarketBond(String bondID) throws SQLException{
    	try {
    		Connection conn = DB.getConnection();
    		String sql = "delete from bondMarket where bondID = ?";
    		PreparedStatement ptmt = conn.prepareStatement(sql);
    		ptmt.setString(5, bondID);
    		
    		ptmt.execute();
    				
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }

     /**
     * Will return a copy of the bond that represents the bondID
     * If the market doesn't contain that bond, it will return an empty bond.
     */
    public static Bond getMarketBond(String bondID) throws SQLException{
    	try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from bondMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
               String toComp = rs.getString("bondID");
               double yield = rs.getDouble("yield");
               double price = rs.getDouble("price");
               String name = rs.getString("companyName");
               String type = rs.getString("type");
               
               
               if (toComp.equals(bondID)) {
            	   if(type.equalsIgnoreCase("OneWkBond")) {
                	   return new model.OneWkBond(name, bondID, price, yield);
                   }
                   else if (type.equalsIgnoreCase("OneMonthBond")) {
                	   return new model.OneMonthBond(name, bondID, price, yield);
                   }
                   else {
                	   return new model.ThreeMonthBond(name, bondID, price, yield);
                   }
               }
            }
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
        return new model.OneWkBond("empty", "empty", 0.0, 0.0);
    }
    
    public static Stock getMarketStock(String ticker) throws SQLException{
    	try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from stockMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
               String toComp = rs.getString("Ticker");
               double price = rs.getDouble("Price");
               String name = rs.getString("CompanyName");
               Date date = rs.getDate("Date");
               
               
               if (toComp.equals(ticker)) {
            	   return new model.Stock(ticker, name, price);
               }
            }
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
        return new model.Stock("empty", "empty", 0.0);
    }

    public static void addInvestorTransaction(String buyOrSell, String ticker, String companyName, double price, int numShare, Date date, double benefit) throws SQLException {
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();

            String sql = "insert into stockTransaction values(?,?,?,?,?,?,?)";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, buyOrSell);
            ptmt.setString(2, ticker);
            ptmt.setString(3, companyName);
            ptmt.setDouble(4, price);
            ptmt.setInt(5, numShare);
            ptmt.setDate(6, date);
            ptmt.setDouble(7, benefit);
            ptmt.execute();

            //System.out.println("succeed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the investor has that much numShare to sell.
     * @param ticker
     * @param numShare
     * @return
     * @throws SQLException
     */
    public static boolean checkSellShare(String ticker, int numShare) throws SQLException{
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from investorStock where ticker=?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, ticker);
            ResultSet rs = ptmt.executeQuery();
            int testNumSum = 0;
            while(rs.next()){
                testNumSum += rs.getInt("numShare");
            }
            if (numShare > testNumSum){
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * A helper method for update database when sell a stock.
     * @param ticker
     * @param numShare
     * @param date
     * @return: benefit
     * @throws SQLException
     */
    public static double investorSellStock(String ticker, int numShare, Date date) throws SQLException {

        ///1. get market price and companyName by ticker and Date
        String companyName = "";
        double sellPrice = 0;
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from stockMarket where ticker=? and Date=?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, ticker);
            ptmt.setDate(2, date);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()) {
                companyName = rs.getString("CompanyName");
                sellPrice = rs.getDouble("Price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ///2.get buy price and calculate benefit.
        double benefit = 0;
        int shareHas =0;
        double buyPrice =0;
        int shareLeftToSell=numShare;
        int shareLeft = 0;
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from investorStock where ticker=?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, ticker);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()) {
                shareHas = rs.getInt("numShare");
                buyPrice = rs.getDouble("buyPrice");
                benefit += shareLeftToSell * (sellPrice - buyPrice);
                shareLeftToSell -= shareHas;
                if (shareLeftToSell >= 0) {
                    ///delete that line in DB
                    deleteStockLine(ticker, shareHas, buyPrice);
                }
                if (shareLeftToSell==0) {
                    break;
                }
                if (shareLeftToSell < 0) {
                    ///update numShare in investorStock
                    shareLeft = -shareLeftToSell;
                    //System.out.println(shareLeftToSell);
                    updateStockShare(ticker, buyPrice, shareLeft);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ///3.update trasactionn table
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "insert into stockTransaction values(?,?,?,?,?,?,?)";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, "sell");
            ptmt.setString(2, ticker);
            ptmt.setString(3, companyName);
            ptmt.setDouble(4, sellPrice);
            ptmt.setInt(5, numShare);
            ptmt.setDate(6, date);
            ptmt.setDouble(7, benefit);
            ptmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ///2.update investorStock table.

        return benefit;
    }

    public static String getAllStock(){
        StringBuilder sb = new StringBuilder();
        String ticker;
        String companyName;
        int numShare;
        double buyPrice;

        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from investorStock";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
                ticker = rs.getString("ticker");
                companyName = rs.getString("companyName");
                numShare = rs.getInt("numShare");
                buyPrice = rs.getDouble("buyPrice");
                sb.append("companyName: " + companyName + ", ticker: " + ticker + ", numShare: " + numShare +
                        ", buyPrice: " + buyPrice + ".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getAllMarketStock() {
        StringBuilder sb = new StringBuilder();
        Date date;
        String companyName;
        String ticker;
        double price;

        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from stockMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
                date = rs.getDate("Date");
                companyName = rs.getString("CompanyName");
                ticker = rs.getString("Ticker");
                price = rs.getDouble("Price");
                sb.append("date: " + date + ", companyName: " + companyName + ", ticker: " + ticker +
                        ", price: " + price + ".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getAllMarketBond() {
        StringBuilder sb = new StringBuilder();
        String companyName;
        String type;
        double yield;
        double price;
        String bondID;

        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from bondMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
                companyName = rs.getString("companyName");
                type = rs.getString("type");
                yield = rs.getDouble("yield");
                price = rs.getDouble("price");
                bondID = rs.getString("bondID");
                sb.append("companyName: " + companyName + ", type: " + type +
                        ", yield: " + yield + ", price: " + price + ", bondID: " + bondID + ".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static boolean marketHasStock(String toCompTicker) throws SQLException {
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from stockMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            //Needs to be fixed.
            while(rs.next()){
                //get fields
                String ticker = rs.getString("Ticker");

                if(ticker.equals(toCompTicker)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean marketHasBond(String toCompBondID) throws SQLException {
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from bondMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            //Needs to be fixed.
            while(rs.next()){
                //get fields
                String bondID = rs.getString("bondID");

                if(bondID.equals(toCompBondID)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateStockMarket(Date newDate) throws SQLException {
        StringBuilder sb = new StringBuilder();
        Date date;
        String companyName;
        String ticker;
        double price;

        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from stockMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            //Needs to be fixed.
            while(rs.next()){
                //get fields
                date = rs.getDate("Date");
                companyName = rs.getString("CompanyName");
                ticker = rs.getString("Ticker");
                price = rs.getDouble("Price");

                //calculate the newPrice
                double randPercent = ThreadLocalRandom.current().nextDouble(-0.05, 0.05);
			    randPercent = Math.round(randPercent * 10000.0)/ 10000.0;
                double newPrice = (price * randPercent) + price;
                
                rs.updateDate("Date", newDate);
                rs.updateDouble("Price", newPrice);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBondMarket() throws SQLException {
        StringBuilder sb = new StringBuilder();
        Date newDate;
        String bondID;
        String companyName;
        double price;
        String type;
        double yield;

        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from bondMarket";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            //Needs to be fixed.
            while(rs.next()){
                //get fields
                companyName = rs.getString("companyName");
                type = rs.getString("type");
                yield = rs.getDouble("yield");
                price = rs.getDouble("price");
                bondID = rs.getString("bondID");

                //calculate new price and yield
                double randPercent = ThreadLocalRandom.current().nextDouble(-0.05, 0.05);
			    randPercent = Math.round(randPercent * 10000.0)/ 10000.0;
			    double newPrice = (price * randPercent) + price;
			    double newYield = yield;
			
			    //if the percent change in price is negative, yield should go up and vice versa
			    if ((newYield - randPercent) <= 0) {
			    	newYield = 0.01;
			    }
			    else {
			    	newYield -= randPercent;
                }
                
                //update table
                rs.updateDouble("price", newPrice);
                rs.updateDouble("yield", newYield);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getAllTransaction() {
        StringBuilder sb = new StringBuilder();
        String buyOrSell;
        String ticker;
        String companyName;
        double price;
        Date date;
        double benefit;

        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "select * from stockTransaction";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
                buyOrSell = rs.getString("buyOrSell");
                ticker = rs.getString("ticker");
                companyName = rs.getString("companyName");
                price = rs.getDouble("price");
                date = rs.getDate("date");
                benefit = rs.getDouble("benefit");
                sb.append("Transaction: "+buyOrSell+", ticker: "+ticker+", companyName: "+companyName+
                        ", price: "+price+", date: "+date+", benefit: "+benefit+".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }



    /**
     * used for method investorSellStockin this class
     * @param ticker
     * @param numShare
     * @param buyPrice
     * @throws SQLException
     */
    private static void deleteStockLine(String ticker, int numShare, double buyPrice) throws SQLException {
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "delete from investorStock where ticker=? and numShare=? and buyPrice=?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, ticker);
            ptmt.setInt(2,numShare);
            ptmt.setDouble(3, buyPrice);
            ptmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * used for method investorSellStock in this class
     * @param ticker
     * @param buyPrice
     * @param shareLeft
     * @throws SQLException
     */
    private static void updateStockShare(String ticker, double buyPrice, int shareLeft) throws SQLException {
        try {
            Connection conn = DB.getConnection();
            statement = conn.createStatement();
            String sql = "update investorStock set numShare =? where ticker=? and buyPrice=?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, shareLeft);
            ptmt.setString(2,ticker);
            ptmt.setDouble(3, buyPrice);
            ptmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
