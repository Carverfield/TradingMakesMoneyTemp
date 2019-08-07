package model;

/**
 * @description: Saving account, the bank will pay interest(10%) to customer each month.
 **/
public class SavingAccount extends Account {

    private final double interestRate = 1.1; ///new balance = old balance * interest rate.

    public SavingAccount() {
        super();
    }

    public SavingAccount(double balance) {
        super(balance);
    }

    public void payInterest() {
        this.setBalance(getBalance() * interestRate);
    }
}
