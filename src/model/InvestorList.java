package model;

import java.util.ArrayList;

/**
 * The class using ArrayList to store the customer info
 */
public class InvestorList {
    private ArrayList<Investor> investorList;

    public Investor createInvestor(String username, String password) {
        Investor investor = new Investor(username, password);
        investorList.add(investor);

        return investor;
    }

    /**
     * this method is used for user login
     * @param username
     * @param password
     * @return
     */
    public Investor checkInvestorIdentity(String username, String password) {
        for (Investor investor : investorList) {
            if (investor.getId().equals(username) && investor.getPassWord().equals(password)) {
                return investor;
            }
        }

        return null;
    }



}
