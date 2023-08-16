package ua.lviv.dataart;

import java.math.BigInteger;

public class Main2 {

    public static void main(String[] args) {
        LongComputationTask longComputationTask = new LongComputationTask(BigInteger.valueOf(9999), BigInteger.valueOf(99999));
        longComputationTask.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (longComputationTask.getState().equals(Thread.State.RUNNABLE)) {
            longComputationTask.interrupt();
        }
    }

    private static class LongComputationTask extends Thread {
        private final BigInteger base;
        private final BigInteger pow;

        public LongComputationTask(BigInteger base, BigInteger pow) {
            this.base = base;
            this.pow = pow;
        }

        @Override
        public void run() {
            System.out.println("Thread started");
            BigInteger powResult = pow(base, pow);
            System.out.println("Result:" + base + "^" + pow + "=" + powResult);
        }

        private static BigInteger pow(BigInteger base, BigInteger pow) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(pow) < 0; i = i.add(BigInteger.ONE)) {
                if (Thread.interrupted()) {
                    System.out.println("Thread was interrupted");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
