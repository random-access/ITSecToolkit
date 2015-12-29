package org.random_access.rsacracker;

/**
 * This class retrieves the decryption key d from a given
 * prime product n and encryption key e.
 * To keep the code compact, there is only minimal Exception handling (no handling of
 * numeric overflows, division by zero,...)
 *
 * Created by Monika Schrenk on 16.12.15.
 */
public class RSASecretCalculator {

    private int n; // prime product
    private int e; // encryption key
    private int p; // first prime number
    private int q; // second prime number
    private int d; // decryption key

    /**
     * Instantiates an RSASecretCalculator
     * @param n product of 2 primes
     * @param e encryption key
     */
    public RSASecretCalculator(int n, int e) {
        this.n = n;
        this.e = e;
    }

    /**
     * Calculates the secret key d, if n is a product of 2 primes
     * @return true, if conversion was successful, else false
     */
    public boolean calculate() {
        return calculateFactors() && calculateSecret();
    }

    /**
     * Get the first prime factor
     * @return p, the first prime factor
     */
    public int getP() {
        return p;
    }

    /**
     * Get the second prime factor
     * @return q, the second prime factor
     */
    public int getQ() {
        return q;
    }

    /**
     * Get the secret key d
     * @return d, the secret key
     */
    public int getD() {
        return d;
    }

    /**
     * Calculates the secret key d by searching the minimal k with
     * k * (p-1) + (q-1) + 1 can be divided by e.
     * d is set to the result of this division.
     * @return true, if calculation was successful, else false
     */
    private boolean calculateSecret() {
        for (int k = 1; k < n; k++) {
            if ((k * (p-1) * (q-1) + 1) % e == 0) {
                d = (k * (p-1) * (q-1) + 1) / e;
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to calculate 2 primes p and q with their product p * q being equal to n.
     * Takes into account that one of those primes is smaller than the
     * square root of n.
     * @return true if primes could be found, else false.
     */
    private boolean calculateFactors() {
        int squareRoot = (int) Math.floor(Math.sqrt(n));
        for (int i = squareRoot; i >= 2; i--) {
            if (isPrime(i)) {
                q = i;
                if (findLargerFactor()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tries to find the larger factor p to a given prime number q
     * with p * q = n
     * @return true, if q could be found, else false
     */
    private boolean findLargerFactor() {
        int i = 2;
        while (i <= n) {
            if (isPrime(i) && q * i == n) {
                p = i;
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Tests if number is prime by trying to divide it through every number
     * up to the square root of number.
     * @param number the number to be tested
     * @return true if number is a prime, else false
     */
    private boolean isPrime(long number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i < Math.floor(Math.sqrt(number)); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

}
