package ua.lviv.dataart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Vault vault = new Vault(1064);
        List<Thread> threads = new ArrayList<>();
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());
        threads.forEach(Thread::start);
    }

    private static void sleepHandled(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Vault {
        private int password;

        public Vault(int password) {
            this.password = password;
        }

        public void setPassword(int password) {
            this.password = password;
        }

        public boolean isPasswordCorrect(int inputPss) {
            sleepHandled(5);
            return this.password == inputPss;
        }
    }

    private static class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("Hacker " + this.getName() + " started guessing vault's password");
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread {
        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_PASSWORD; i++) {
                if (vault.isPasswordCorrect(i)) {
                    System.out.println(this.getName() + " stole all money");
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHackerThread extends HackerThread {
        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = MAX_PASSWORD; i > 0; i--) {
                if (vault.isPasswordCorrect(i)) {
                    System.out.println(this.getName() + " stole all money");
                    System.exit(0);
                }
            }
        }
    }

    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            System.out.println("Policeman: I'm counting to 10 and I'll kick your asses, hackers");
            for (int i = 10; i > 0; i--) {
                sleepHandled(1000);
                System.out.println(i);
            }
            System.out.println("You have lost, hackers");
            System.exit(0);
        }
    }
}
