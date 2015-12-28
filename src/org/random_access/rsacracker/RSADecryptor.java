package org.random_access.rsacracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.LinkedList;

/**
 * This class can be used for decrypting numbers in a text file which are encrypted with RSA.
 * To keep the code compact, there is only minimal Exception handling (no handling of
 * numeric overflows, division by zero,...)
 *
 * Created by Monika Schrenk on 17.12.15.
 */
public class RSADecryptor {

    private long n; // prime product
    private LinkedList<Long> binaryCoeffsOfD; // coefficients of d as binary sum
    private long[] encrypted; // encrypted numbers
    private long[] decrypted; // decrypted numbers

    /**
     *
     * Initializes an RSADecryptor.
     * @param n product of 2 primes
     * @param d decryption key
     * @param pathToFile path to file with encrypted content
     * @throws IOException if the file doesn't exist or cannot be read
     * @throws ParseException if the file contains invalid characters
     */
    public RSADecryptor(long n, long d, String pathToFile) throws IOException, ParseException {
        this.n = n;
        encrypted = importFile(pathToFile);
        binaryCoeffsOfD = calculateBinarySum(d);
        decrypted = decryptNumbers();
    }

    /**
     * Get the encrypted numbers that were read from file
     * @return an array holding encrypted numbers
     */
    public long[] getEncrypted() {
        return encrypted;
    }

    /**
     * Get the decrypted numbers
     * @return an array holding decrypted numbers
     */
    public long[] getDecrypted() {
        return decrypted;
    }

    /**
     * Calculates the coefficients of z as a binary sum, resulting in
     * z = sum of i from 0 to k (b_i * 2^i), where k is LinkedList.size() - 1
     * @param z an integer
     * @return a LinkedList with the coefficients of z as binary sum in descending order
     */
    private LinkedList<Long> calculateBinarySum(long z) {
        LinkedList<Long> sum = new LinkedList<>();
        while (z > 0) {
            sum.add(z % 2);
            z = z / 2;
        }
        return sum;
    }

    /**
     * Reads a sequence of numbers from a file
     * @param pathToFile path in file system
     * @return an array containing the numbers that were read from file
     * @throws IOException if the file doesn't exist or cannot be read
     * @throws ParseException if the file contains invalid characters
     */
    private long[] importFile(String pathToFile) throws IOException, ParseException{
        String contentAsString = new String(Files.readAllBytes(Paths.get(pathToFile)));
        // numbers could be separated either by blank(s) or by line break
        String[] content = contentAsString.split("[\\s+\\n\\r\\s]+");
        long[] numbers = new long[content.length];
        try {
            for (int i = 0; i < content.length; i++) {
                numbers[i] = Integer.parseInt(content[i]);
            }
            return numbers;
        } catch (NumberFormatException e){
            throw new ParseException
                    ("Invalid file content, file must only contain numbers, spaces and line breaks!", 0);
        }
    }

    /**
     * Decrypts the RSA encrypted numbers in an array.
     * @return an array that contains the decrypted numbers
     */
    private long[] decryptNumbers() {
        decrypted = new long[encrypted.length];
        for (int i = 0; i < encrypted.length; i++) {
            decrypted[i] = decryptNumber(encrypted[i]);
        }
        return decrypted;
    }

    /**
     * Decrypts a number x that is encrypted with RSA using Square-and-Multiply algorithm, returning
     * z = x^d mod n
     * @param x the encrypted number
     * @return the decrypted number
     */
    private long decryptNumber(long x) {
        long z = 1;
        for (int i = binaryCoeffsOfD.size()-1; i >= 0; i--) {
            z = (z * z) % n;
            if (binaryCoeffsOfD.get(i) == 1) {
                z = (z * x) % n;
            }
        }
        return z;
    }
}
