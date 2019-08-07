package model;

import Database.DB;
import Database.DBHelper;

import java.sql.SQLException;
import java.util.Date;


/**
 * @description: This is the class of security account which investor can buy/sell stocks.
 **/

///unfinished
///database looks like
///   Ticker  companyName  numShare  buyDate  buyPrice  sellDate  sellPrice  benefit

public class SecurityAccount extends Account {
    private double unrealizedBenefit;

    public SecurityAccount() {
        super();
        this.unrealizedBenefit = 0;
    }

    public SecurityAccount(double balance) {
        super(balance);
        this.unrealizedBenefit = 0;
    }

    /**
     * Allow investor buy one stock through the ticker, and this record will be written in database.
     * @param ticker: like AAPL.
     * @param numShare: number of share the investor will buy
     * @param date: know the date
     * @return: if the investor buy a stock successfully.
     *              (If the investor do not have enough balance to buy the stock, it will return false)
     */
    public boolean buyStock(String ticker, int numShare, Date date) throws SQLException{
        ///1. get the market price of this stock.(and companyName)
        DBHelper helper = new DBHelper();
        Date date1= new java.sql.Date(date.getTime());

        double stockPrice;
        String companyName ;
        stockPrice = helper.getMarketPrice(ticker, (java.sql.Date) date1);
        companyName = helper.getCompanyName(ticker);

        ///2.update balance
        double totalPrice = stockPrice *numShare;
        if (totalPrice > this.getBalance()) {
            return false;
        } else {
            this.setBalance(getBalance() - totalPrice);
        }

        ///3. write in table investorStock.
        ///write in table investorStock first
        helper.addInvestorStock(ticker, "APPLE", numShare, stockPrice);

        ///write in table stockTransaction
        helper.addInvestorTransaction("buy", ticker, companyName, stockPrice, numShare, (java.sql.Date) date1, 0);

        return true;
    }

    /**
     * Allow investor sell one stock through ticker and numshare, and update database.
     * @param ticker
     * @param numShare
     * @param date
     * @return: realized benefit of this stock
     */
    public double sellStock(String ticker, int numShare, Date date)  throws SQLException{
        DBHelper helper = new DBHelper();
        ///1.check if you have that much numshare in your account.
        if (!helper.checkSellShare(ticker, numShare)) return Double.MIN_VALUE;

        ///2. get the buyprice in database, calculate benefit, update table investorStock and stockTransaction.
        Date date1 = new java.sql.Date(date.getTime());
        double realizedBenefit = helper.investorSellStock(ticker, numShare, (java.sql.Date) date1);
        this.setBalance(getBalance() + realizedBenefit);

        return realizedBenefit;
    }

    public double getUnrealizedBenefit(Date date) throws SQLException {
        DBHelper helper = new DBHelper();
        Date date1= new java.sql.Date(date.getTime());
        return helper.getUnrealizedBenefit((java.sql.Date) date1);

    }

    /**
     * get all stock information of a investor.
     * @return a string of all stock investor has.
     */
    public String getAllStock() {
        DBHelper helper = new DBHelper();
        String str = helper.getAllStock();
        return str;
    }


    /**
     *get all transactions of the investor
     * @return: string (if there's 5 transactions, return a string of 5 rows)
     */
    public String getAllTransaction() {
        DBHelper helper = new DBHelper();
        String str = helper.getAllTransaction();
        System.out.println(str);
        return str;
    }


//    private boolean buyBond(Bond bond, Date date) {
//    }

    public boolean sellBond

    public void deleteDatabaseIfo() throws SQLException{
        DBHelper helper = new DBHelper();
        helper.deleteDatabaseIfo();
    }
}
