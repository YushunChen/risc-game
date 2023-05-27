package edu.duke.ece651.team13.server.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * This class generates a secret key using a SecureRandom object and the Base64 encoder.
 */
public class SecretKeyGenerator {
    /**
     * This method generates a 64-byte secret key using a SecureRandom object and the Base64 encoder.
     * prints out The generated secret key as a string.
     */
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        String secretKey = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Generated Secret Key: " + secretKey);
    }
}