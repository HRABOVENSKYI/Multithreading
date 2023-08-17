package ua.lviv.dataart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Long> numbers = List.of(1L, 123L, 3456676L, 4534L, 23L);

        List<FactorialThread> threads = new ArrayList<>();

        numbers.forEach(n -> threads.add(new FactorialThread(n)));
        threads.forEach(t -> {
            t.setDaemon(true);
            t.start();
        });
        try {
            for (FactorialThread t : threads) {
                t.join(2000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threads.forEach(t -> {
            if (t.isFinished) {
                System.out.println("Thread is finished and the result is: " + t.result);
            } else {
                System.out.println("Thread have not finished");
            }
        });
    }

    private static class FactorialThread extends Thread {
        private final long number;
        private BigInteger result;
        private boolean isFinished;

        public FactorialThread(long number) {
            this.number = number;
            result = BigInteger.ONE;
            isFinished = false;
        }

        @Override
        public void run() {
            result = factorial(number);
            isFinished = true;
        }

        private static BigInteger factorial(long number) {
            BigInteger result = BigInteger.ONE;
            for (long i = 2; i <= number; i++) {
                result = result.multiply(BigInteger.valueOf(i));
            }
            return result;
        }
    }
}
