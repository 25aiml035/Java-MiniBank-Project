package exception;

public class DailyLimitExceededException extends InsufficientFundsException {
    public DailyLimitExceededException(String message) {
        super(message, 0);
    }
}
