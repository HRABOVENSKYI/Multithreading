package ua.lviv.dataart;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main implements Runnable {
    int counter = 1;

    public static void main(String[] args) {
        Main main = new Main();
        Executor executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.execute(main);
        }

    }

    @Override
    public void run() {
        synchronized (this) {
            System.out.println(counter);
            counter++;
        }
    }
}
