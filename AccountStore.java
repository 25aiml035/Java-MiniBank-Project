package service;

import model.Account;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {
    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized void addLog(String logEntry) {
        transactionHistory.add(logEntry);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();
        list.sort(new Comparator<Account>() {
            @Override
            public int compare(Account a1, Account a2) {
                return Long.compare(a2.getBalance(), a1.getBalance());
            }
        });
        return list;
    }
    
    public ArrayList<Account> getAccountsWithBalanceAbove(long limit) {
        ArrayList<Account> result = new ArrayList<>();
        for (Account acc : accounts.values()) {
            if (acc.getBalance() > limit) {
                result.add(acc);
            }
        }
        return result;
    }
}
