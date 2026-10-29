package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Account;

public class TransactionProcessor {
    private final ExecutorService pool;

    public TransactionProcessor() {
        this.pool = Executors.newFixedThreadPool(4);
    }

    public void submit(Runnable task) {
        pool.execute(task);
    }

    public void stop() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void safeTransfer(Account from, Account to, long amount) throws Exception {
        Account firstLock = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account secondLock = firstLock == from ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                }
            }
        }
    }
}
