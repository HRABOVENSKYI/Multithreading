package ua.lviv.dataart;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger logger = Logger.getLogger("AppLogger");

    public static void main(String[] args) throws InterruptedException {
        Counter counter = Counter.getInstance();
        IncrementalThread incrementalThread = new IncrementalThread(counter);
        DecrementalThread decrementalThread = new DecrementalThread(counter);

        incrementalThread.start();
        decrementalThread.start();
        incrementalThread.join();
        decrementalThread.join();
        logger.log(Level.INFO, "The end count is: {0}", counter.getCount()); // result is not 0 !!!
    }

    private static class Counter {
        private static Counter counter;
        private static final int INITIAL_COUNT = 0;
        private int count;

        private Counter(int count) {
            this.count = count;
        }

        public static Counter getInstance() {
            if (counter == null) {
                counter = new Counter(INITIAL_COUNT);
            }
            return counter;
        }

        public void decrement() {
            synchronized (this) {
                this.count--;
            }
        }

        public void increment() {
            synchronized (this) {
                this.count++;
            }
        }

        public int getCount() {
            synchronized (this) {
                return count;
            }
        }
    }

    private static class IncrementalThread extends Thread {
        private Counter counter;

        public IncrementalThread(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        }
    }

    private static class DecrementalThread extends Thread {
        private Counter counter;

        public DecrementalThread(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.decrement();
            }
        }
    }
}
