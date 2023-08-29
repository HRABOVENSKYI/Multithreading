package ua.lviv.dataart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class MyMain {

    private static class Node<T> {
        public T value;
        public Node<T> next;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }
    }

    private static class LockingStack<T> {
        private Node<T> head;
        private volatile long operationsCounter;

        public synchronized void push(T value) {
            Node<T> newHead = new Node<>(value);
            newHead.next = head;
            head = newHead;
            operationsCounter++;
        }

        public synchronized T pop() {
            if (head == null) {
                return null;
            }
            T value = head.value;
            head = head.next;
            operationsCounter++;
            return value;
        }

        public synchronized void printAll() {
            Node<T> pointer = head;
            while (pointer != null) {
                System.out.println(pointer.value);
                pointer = pointer.next;
            }
        }

        public void printOperationsCounter() {
            System.out.println("Operations count: " + operationsCounter);
        }
    }

    private static class LockFreeStack<T> {
        private AtomicReference<Node<T>> head = new AtomicReference<>();
        private final AtomicLong operationsCounter = new AtomicLong();

        public void push(T value) {
            Node<T> newHead = new Node<>(value);

            while (true) {
                Node<T> currentHead = head.get();
                newHead.next = currentHead;
                if (head.compareAndSet(currentHead, newHead)) {
                    operationsCounter.incrementAndGet();
                    break;
                }
            }
        }

        public T pop() {
            Node<T> currentValue = head.get();
            while (currentValue != null) {
                if (head.compareAndSet(currentValue, currentValue.next)) {
                    operationsCounter.incrementAndGet();
                    return currentValue.value;
                }
                currentValue = head.get();
            }
            return null;
        }

        public synchronized void printAll() {
            Node<T> pointer = head.get();
            while (pointer != null) {
                System.out.println(pointer.value);
                pointer = pointer.next;
            }
        }

        public void printOperationsCounter() {
            System.out.println("Operations count: " + operationsCounter);
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
//        LockingStack<Integer> stack = new LockingStack<>();
        LockFreeStack<Integer> stack = new LockFreeStack<>();

        // LockingStack: pushThreadNum = 2; popThreadNum = 2; Operations count: 80M~
        // LockFreeStack: pushThreadNum = 2; popThreadNum = 2; Operations count: 63M~

        for (int i = 0; i < 100000; i++) { // init stack
            stack.push(random.nextInt());
        }

        int pushThreadNum = 2;
        int popThreadNum = 2;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < pushThreadNum; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (int i = 0; i < popThreadNum; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        stack.printOperationsCounter();
    }
}
