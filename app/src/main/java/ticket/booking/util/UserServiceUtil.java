package ticket.booking.util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainPassword The raw user-entered password.
     * @return The hashed password.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Verifies if the entered plain password matches the hashed one.
     *
     * @param plainPassword  The raw user-entered password.
     * @param hashedPassword The previously stored BCrypt-hashed password.
     * @return true if password matches, false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
