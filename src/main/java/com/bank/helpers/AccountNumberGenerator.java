package com.bank.helpers;

import java.util.Random;

public class AccountNumberGenerator {
    public static String generateAccountNumber(){
        Random random = new Random();
        long number = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(number);
    }

}
