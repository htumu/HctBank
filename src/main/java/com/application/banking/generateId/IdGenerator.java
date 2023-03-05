package com.application.banking.generateId;

import java.util.Random;

public class IdGenerator {
    public static long generateRandom(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(random.nextInt(9) + 1);
        for (int i = 0; i < length-1; i++) {
            sb.append(random.nextInt(10));
        }
        return Long.valueOf(sb.toString()).longValue();
    }
}
