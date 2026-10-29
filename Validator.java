package util;

public class Validator {
    public static boolean isValidAmount(long amount) {
        return amount > 0;
    }
    
    public static boolean isValidMenuChoice(int choice, int maxOptions) {
        return choice >= 1 && choice <= maxOptions;
    }
}
