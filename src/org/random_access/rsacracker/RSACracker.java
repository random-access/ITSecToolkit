package org.random_access.rsacracker;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class starts RSACracker and is responsible for user interaction.
 *
 * Created by Monika Schrenk on 18.12.15.
 */
public class RSACracker {

    private static int n;
    private static int d;

    /**
     * Entry point of the program
     * @param args program arguments, not used here
     */
    public static void main(String[] args) {
        System.out.println("*** Welcome to RSACracker! *** \n" +
                "Choose one of the following options: \n" +
                "1 = get secret key, 2 = decrypt text, 3 = get secret key & decrypt text");
        try (Scanner sc = new Scanner(System.in)) {
            int mode = sc.nextInt();
            switch (mode) {
                case 1:
                    startSecretCalculation(sc);
                    break;
                case 2:
                    startDecryption(sc, false);
                    break;
                case 3:
                    startSecretCalculation(sc);
                    startDecryption(sc, true);
                    break;
                default:
                    System.out.println("Invalid option! Please enter a number between 1 and 3");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
        } catch(IOException e) {
            System.out.println("File not found!");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Asks user to enter n and encryption key, then starts calculation of decryption key d
     * @param sc scanner for getting user input
     */
    private static void startSecretCalculation(Scanner sc) {
        System.out.print("Enter n (prime product): ");
        n = sc.nextInt();
        System.out.print("Enter e (encryption key): ");
        int e = sc.nextInt();
        RSASecretCalculator calculator = new RSASecretCalculator(n, e);
        System.out.println("Calculating secret for n = " + n + " and e = " + e + "...");
        if (calculator.calculate()) {
            int p = calculator.getP();
            int q = calculator.getQ();
            d = calculator.getD();
            System.out.println("Secret found! ");
            System.out.println("p = " + p);
            System.out.println("q = " + q);
            System.out.println("d = " + d);
        } else {
            System.out.println("n is not a product of 2 primes.");
        }
    }

    /**
     * Starts decryption of numbers in a file. Calculates secret key if not calculated before via StartSecretCalculation.
     * @param sc scanner for getting user input
     * @param hasKey true if executed after startSecretCalculation, else false
     * @throws IOException if file with encrypted numbers cannot be read or doesn't exist
     * @throws ParseException if file contains invalid characters (anything except numbers, blanks and line breaks)
     */
    private static void startDecryption(Scanner sc, boolean hasKey) throws IOException, ParseException {

        // read decryption parameters if they were not calculated before
        if (!hasKey) {
            System.out.print("Enter n (prime product): ");
            n = sc.nextInt();
            System.out.print("Enter d (decryption key): ");
            d = sc.nextInt();
        }

        // consume the rest of input leftover by the last scan
        sc.nextLine();

        System.out.println("Enter file path: ");
        String filePath = sc.nextLine();
        RSADecryptor dec = new RSADecryptor(n, d, filePath);
        System.out.println("Parsing encrypted numbers from file .... ");
        System.out.println(Arrays.toString(dec.getEncrypted()) + "\n");
        System.out.println("Parsing decrypted numbers from file... ");
        System.out.println(Arrays.toString(dec.getDecrypted()) + "\n");

        // convert decrypted numbers to plain text
        RSAConverter converter = new RSAConverter(dec.getDecrypted());
        System.out.println("Decrypted text: ");
        System.out.println(converter.getPlainText());
    }
}
