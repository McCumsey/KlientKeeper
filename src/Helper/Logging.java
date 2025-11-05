package Helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZonedDateTime;

public class Logging {

    /**
     * Logs the login activity to a file.
     *
     * @param success   True if the login was successful, false otherwise.
     * @param userName  The username of the user.
     * @throws IOException If an error occurs while writing to the file.
     */
    public static void logLogIn(boolean success, String userName) throws IOException {
        Path filePath = Path.of("login_activity.txt");

        Instant now = ZonedDateTime.now().toInstant();
        String logMessage = success ? "SUCCESS" : "FAIL";
        String logEntry = String.format("User: %s Log in: %s at %s UTC.%n", userName, logMessage, now.toString());

        try (PrintWriter printWriter = new PrintWriter(Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            printWriter.println(logEntry);
        }
    }
}
