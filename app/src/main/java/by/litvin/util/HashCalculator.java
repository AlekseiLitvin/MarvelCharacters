package by.litvin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCalculator {
    private HashCalculator() {
    }

    public static String calculate(String... args) {
        String input = concatenateArgs(args);
        byte[] digest = generateDigest(input);
        return toHexString(digest);
    }

    private static String concatenateArgs(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg);
        }
        return stringBuilder.toString();
    }

    private static byte[] generateDigest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());

            return md.digest();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return null;
    }

    private static String toHexString(byte[] digest) {
        if (digest == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte digestByte : digest) {
            stringBuilder.append(Integer.toString((digestByte & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }
}
