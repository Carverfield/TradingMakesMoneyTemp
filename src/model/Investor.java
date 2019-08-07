package model;

/**
 * @description: A investor has four attributes:
 *                                      1. Checking Account
 *                                      2. Saving Account
 *                                      3. Loan
 *                                      4. Security Account
 **/
public class Investor extends Person {

    private SavingAccount savingAccount;
    private CheckingAccount checkingAccount;
    private SecurityAccount securityAccount;
    private Loan loan;

    /**
     * Connstructor
     * @param id
     * @param passWord
     */
    public Investor(String id, String passWord) {
        super(id, passWord);
        this.savingAccount = new SavingAccount();
        this.checkingAccount = new CheckingAccount();
        this.securityAccount = new SecurityAccount();
        this.loan = new Loan();
    }

    /**
     * If true = change values from checking account
     * Else return false
     * @param amount
     * @return
     */
    public boolean Transfer(double amount) {
        double checkingBalance = this.getCheckingAccount().getBalance();
        double securityBalance = this.getSecurityAccount().getBalance();
        //return false if transfer too much
        if (amount > checkingBalance) {
            return false;
        }
        this.getCheckingAccount().setBalance(checkingBalance - amount);
        this.getSecurityAccount().setBalance(securityBalance + amount);

        return true;
    }

    /**
     * Methods below are getter and setter method for different account
     */
    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public CheckingAccount getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(CheckingAccount checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public SecurityAccount getSecurityAccount() {
        return securityAccount;
    }

    public void setSecurityAccount(SecurityAccount securityAccount) {
        this.securityAccount = securityAccount;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
