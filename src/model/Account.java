package model;

/**
 * @description: Parent class of different accounts.
 **/
public abstract class Account {

    private double balance;
    private final double payForWithdraw = 0.02;

    public Account() {
        this.balance = 0;

    }

    public Account(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * The same deposit method for CheckingAccount, SavingAccount and SecurityAccount.
     * @param money
     * @return
     */
    public boolean deposit(double money) {
        balance += money;
        return true;
    }

    /**
     *The same withdraw method for different account.
     * @param money: withdraw amount
     * @return
     */
    public boolean withdraw(double money) {
        double withdrawFee = money * payForWithdraw;
        if (money + withdrawFee > balance) {
            return false;
        }
        balance -= (money + withdrawFee);
        return true;
    }


}
