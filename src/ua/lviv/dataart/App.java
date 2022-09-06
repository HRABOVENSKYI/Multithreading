package ua.lviv.dataart;

public class App {

    public static class T1 extends Thread {
        private Deadlock deadlock;

        public T1(Deadlock deadlock) {
            this.deadlock = deadlock;
        }

        @Override
        public void run() {
            deadlock.method1();
        }
    }

    public static class T2 extends Thread {
        private Deadlock deadlock;

        public T2(Deadlock deadlock) {
            this.deadlock = deadlock;
        }

        @Override
        public void run() {
            deadlock.method2();
        }
    }

    public static class Deadlock {
        private Object m1 = new Object();
        private Object m2 = new Object();

        public void method1() {
            synchronized (m1) {
                System.out.println("m1 is locked by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (m2) {
                    System.out.println("m2 is locked by " + Thread.currentThread().getName());
                }
            }
        }

        public void method2() {
            synchronized (m2) {
                System.out.println("m2 is locked by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (m1) {
                    System.out.println("m1 is locked by " + Thread.currentThread().getName());
                }
            }
        }
    }

    public static void main(String[] args) {
        Deadlock deadlock = new Deadlock();
        T1 t1 = new T1(deadlock);
        T2 t2 = new T2(deadlock);
        t1.start();
        t2.start();
    }


}
