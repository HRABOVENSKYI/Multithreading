package ua.lviv.dataart;

public class Main {

    private static class SharedClass {
        // if we add volatile keyword to x and y, they will be executed in order they are written in methods, and therefore,
        // no race condition will happen
        private int x;
        private int y;

        public void increment() {
            x++;
            y++;
        }

        public void checkDataRace() {
            // if we add volatile keyword to x and y, they will be executed in order they are written in methods, and therefore,
            // no race condition will happen
            if (y > x) {
                System.out.printf("Data race have happened. x=%d, y=%d%n", x, y);
            }
        }
    }

    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();
        Thread incrementingThread = new Thread(() -> {
            while(true) {
                sharedClass.increment();
            }
        });
        Thread checkDataRaceThread = new Thread(() -> {
            while(true) {
                sharedClass.checkDataRace();
            }
        });
        incrementingThread.start();
        checkDataRaceThread.start();
    }
}
