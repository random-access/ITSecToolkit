package org.random_access.rsacracker;

/**
 * This class converts numbers into plain text under the following conditions:
 * <ul>
 *     <li>each number contains 3 characters</li>
 *     <li>all characters are lowercase letters from a to z</li>
 *     <li>the letter code a=1, b=2, ..., z=26 is used</li>
 *     <li>The number s is defined as s = a_2 * 27^2 + a_1 * 27 + a_0,
 *     and a_i are the resulting characters</li>
 * </ul>
 *
 * Created by Monika Schrenk on 18.12.15.
 */
public class RSAConverter {

    private int[] decrypted; // decrypted numbers

    /**
     * Instantiates an RSAConverter
     * @param decrypted an array holding the numbers which are already decrypted
     */
    public RSAConverter(int[] decrypted) {
        this.decrypted = decrypted;
    }


    /**
     * Converts decrypted numbers to plain text
     * @return a String holding the plain text
     */
    public String getPlainText() {
        StringBuilder sb = new StringBuilder();
        for (int number : decrypted) {
            sb.append(convertToChars(number));
        }
        return sb.toString();
    }

    /**
     * Converts a number to 3 chars, if number = c_1 * 27^2 + c_2 * 27 + c_3,
     * where c_i is the letter code (a=1, b=2, ..., z=26)
     * @param number the number that encodes 3 chars
     * @return a String of length 3
     */
    private String convertToChars(int number) {
        int[] charNumbers = new int[3];
        int [] factors = {27*27, 27, 1};
        for (int i = 0; i < charNumbers.length; i++) {
            charNumbers[i] = number / factors[i];
            number = number % factors[i];
        }
        return getChars(charNumbers);
    }

    /**
     * Converts an array of numbers in letter code to a String
     * by adding 96 to every char and constructing a String.
     * We need to add 96 because small letters start in ASCII with 97
     * @param charNumbers an array holding numbers in letter code
     * @return a String with the converted letters
     */
    private String getChars(int[] charNumbers) {
        StringBuilder sb = new StringBuilder();
        for (int number : charNumbers) {
            sb.append((char) (number + 96));
        }
        return sb.toString();
    }
}
