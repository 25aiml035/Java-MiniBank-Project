package main;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import model.*;
import service.*;
import static util.Validator.isValidMenuChoice;

record BankInfo(String name, String branch) {}

public class MiniBank {
    
    static class ResourceDemo implements AutoCloseable {
        public void use() {
            System.out.println(">> Resource inside try-with-resources is active.");
        }
        @Override
        public void close() {
            System.out.println(">> Resource auto-closed clean.");
        }
    }

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("National Student Bank", "GitHub Virtual Branch");
        System.out.println("==========================================");
        System.out.println("Welcome to " + bank.name());
        System.out.println("Branch: " + bank.branch());
        System.out.println("==========================================");

        try (ResourceDemo res = new ResourceDemo()) {
            res.use();
            
            System.out.println("\n--- Initializing Accounts & Collections Framework ---");
            AccountStore store = new AccountStore();
            
            SavingsAccount sa = new SavingsAccount("Riya", 5000, 1000);
            CurrentAccount ca = new CurrentAccount("Aarav", 10000, 5000);
            FixedDepositAccount fda = new FixedDepositAccount("Kabir", 50000, LocalDate.now().plusDays(5));

            store.add(sa);
            store.add(ca);
            store.add(fda);

            System.out.println("\n--- Testing Run-Time Polymorphism & Interest ---");
            for (Account acc : store.getAll()) {
                System.out.println(acc.getOwnerName() + "'s Base Type: " + acc.toString() + 
                                   " | Interest Rate: " + acc.interestRate() + "%");
                
                if (acc instanceof SavingsAccount sAcc) {
                    System.out.println("   [Note] This is a Savings Account with Strict Minimum Balance Rules.");
                }
            }

            System.out.println("\n--- Executing Concurrent Multi-Threaded Engine ---");
            TransactionProcessor processor = new TransactionProcessor();
            
            for (int i = 0; i < 5; i++) {
                processor.submit(() -> {
                    try {
                        sa.deposit(100);
                        TransactionLog.append(Paths.get("data/logs/tx_day1.log"), "DEPOSIT " + sa.getAccountNumber() + " 100");
                    } catch (Exception ignored) {}
                });
            }
            
            processor.stop();
            System.out.println("Post-Concurrent Balance for Riya: INR " + sa.getBalance());

            System.out.println("\n--- Testing Safe Transaction Processing & Exceptions ---");
            try {
                sa.withdraw(10000);
            } catch (Exception e) {
                System.out.println("Caught Expected Error: " + e.getMessage());
            }

            System.out.println("\n--- Saving & Restoring State Layer ---");
            Account[] arrayToSave = store.getAll().toArray(new Account[0]);
            StatePersister.save(arrayToSave, Paths.get("data/accounts.dat"));
            System.out.println("State successfully flattened and persisted to disk.");

            System.out.println("\n--- Generating End-of-Day Operations Audit Report ---");
            ReportGenerator.generateReport(Paths.get("data/logs"), Paths.get("data/eod_report.txt"));
            System.out.println("Report created at data/eod_report.txt.");
            
            System.out.println("\n--- Sorted Accounts List via Collections Framework ---");
            ArrayList<Account> sorted = store.sortedByBalance();
            for(Account a : sorted) {
                System.out.println("Owner: " + a.getOwnerName() + " -> Balance: " + a.getBalance());
            }

        } catch (Exception e) {
            System.err.println("Fatal Application Error: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            System.out.println("\n1. Open 2. Deposit 3. Withdraw 4. Transfer 5. Exit");
            System.out.print("Select Menu Option: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (isValidMenuChoice(choice, 5)) {
                    if (choice == 5) {
                        System.out.println("Exiting System Shell. Goodbye!");
                        loop = false;
                    } else {
                        System.out.println("Processing System Option " + choice);
                    }
                } else {
                    System.out.println("Error: Option out of bounds.");
                }
            } else {
                System.out.println("Error: Numeric values only.");
                scanner.next();
            }
        }
        scanner.close();
    }
}
