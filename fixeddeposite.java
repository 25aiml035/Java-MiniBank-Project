package model;

import java.time.LocalDate;

public class FixedDepositAccount extends Account {
    private static final long serialVersionUID = 1L;
    private LocalDate maturityDate;

    public FixedDepositAccount(String ownerName, long openingBalance, LocalDate maturityDate) {
        super(ownerName, openingBalance);
        this.maturityDate = maturityDate;
    }

    @Override
    public double interestRate() { return 7.0; }

    @Override
    public boolean canWithdraw(long amount) {
        return !LocalDate.now().isBefore(maturityDate);
    }
}
