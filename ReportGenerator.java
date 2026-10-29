package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportGenerator {
    public static void generateReport(Path logsDir, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir)) {
            for (Path entry : stream) {
                if (Files.size(entry) == 0 || !entry.toString().endsWith(".log")) {
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(entry)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] tokens = line.split(" ");
                        if (tokens.length >= 3) {
                            String type = tokens[0];
                            long amount = Long.parseLong(tokens[2]);
                            if (type.equalsIgnoreCase("DEPOSIT")) {
                                totalDeposits += amount;
                            } else if (type.equalsIgnoreCase("WITHDRAW")) {
                                totalWithdrawals += amount;
                            }
                        }
                    }
                }
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(reportFile)) {
            writer.write("========================================\n");
            writer.write("        END-OF-DAY BANK REPORT          \n");
            writer.write("========================================\n");
            writer.write("Total Deposits   : INR " + totalDeposits + "\n");
            writer.write("Total Withdrawals: INR " + totalWithdrawals + "\n");
            writer.write("Net Capital Flow : INR " + (totalDeposits - totalWithdrawals) + "\n");
            writer.write("========================================\n");
        }
    }
}
