package com.plac.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomGeneratorUtil {

    public static int generateRandomSixDigitNumber() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(900000) + 10000;
    }
}
