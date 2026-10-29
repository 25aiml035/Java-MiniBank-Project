package model;

import exception.DailyLimitExceededException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private long minBalance;
    private long dailyWithdrawnSoFar = 0;
    private static final long DAILY_LIMIT = 25000;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() { return 4.0; }

    @Override
    public boolean canWithdraw(long amount) {
        return (this.balance - amount) >= minBalance;
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (dailyWithdrawnSoFar + amount > DAILY_LIMIT) {
            throw new DailyLimitExceededException("Daily withdrawal limit of INR " + DAILY_LIMIT + " exceeded.");
        }
        boolean success = super.withdraw(amount);
        if (success) {
            dailyWithdrawnSoFar += amount;
        }
        return success;
    }
}
