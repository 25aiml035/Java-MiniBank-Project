package model;

import java.io.Serializable;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public abstract class Account implements Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;

    private final String accountNumber;
    private String ownerName;
    protected long balance;
    private boolean active;
    private transient String temporarySessionToken;

    private static int accountCounter = 0;

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance >= 0 ? openingBalance : 0;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Given: " + amount);
        }
        this.balance += amount;
    }

    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Given: " + amount);
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException("Withdrawal violates account type constraints or limits.", amount - this.balance);
        }
        if (amount > this.balance) {
            throw new InsufficientFundsException("Insufficient funds in account " + accountNumber, amount - this.balance);
        }
        this.balance -= amount;
        return true;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: INR " + balance + " | Active: " + active;
    }

    public String getStatement() {
        return "----------------------------------------\n" +
               "Account Statement for: " + accountNumber + "\n" +
               "Owner Name     : " + ownerName + "\n" +
               "Current Balance: INR " + balance + "\n" +
               "Account Status : " + (active ? "ACTIVE" : "INACTIVE") + "\n" +
               "Interest Rate  : " + interestRate() + "%\n" +
               "----------------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public long getBalance() { return balance; }
    public boolean isActive() { return active; }
}
