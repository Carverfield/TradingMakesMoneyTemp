package Database;

import java.sql.*;

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

    public static void addMarketStock(Date date, String companyName, String ticker, int price) throws SQLException{
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
                
                //calculate days in between old date and new date
                //loop that many times and update prices
                
                //update price in table
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBondMarket() throws SQLException {
        StringBuilder sb = new StringBuilder();
        Date newDate = date;
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
                buildID = rs.getString("buildID");

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
                //missing, needs to be finished

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
