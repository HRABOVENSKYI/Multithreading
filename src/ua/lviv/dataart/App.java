package ua.lviv.dataart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class App {

    public static class Person extends Thread {
        private Semaphore semaphore;

        public Person(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " is waiting");
            try {
                semaphore.acquire();
                System.out.println(this.getName() + " is at the table");
                this.sleep(1000);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args) {
        Semaphore table = new Semaphore(2); // we've created two tables
        List<Person> persons = new ArrayList<>();
        IntStream stream = IntStream.range(0, 8);
        stream.forEach(i -> persons.add(new Person(table))); // created 8 peoples
        persons.forEach(Thread::start); // 2 persons are eating, others are waiting
    }
}
