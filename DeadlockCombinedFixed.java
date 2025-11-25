package com.mycompany.deadlockcombinedfixed;

public class DeadlockCombinedFixed {
    // Locks for deadlock demo
    public static Object DLock1 = new Object();
    public static Object DLock2 = new Object();
    public static Object DLock3 = new Object();

    // Locks for solution demo
    public static Object SLock1 = new Object();
    public static Object SLock2 = new Object();
    public static Object SLock3 = new Object();

    public static void main(String[] args) {
        System.out.println("=== Demonstrating Deadlock ===");
        runDeadlockDemo();

        System.out.println("\n=== Solving Deadlock by Consistent Lock Order ===");
        runSolutionDemo();
    }

    // ---------------- DEADLOCK DEMO ----------------
    private static void runDeadlockDemo() {
        Thread t1 = new Thread(new DeadlockTask1(), "Deadlock-Thread-1");
        Thread t2 = new Thread(new DeadlockTask2(), "Deadlock-Thread-2");
        Thread t3 = new Thread(new DeadlockTask3(), "Deadlock-Thread-3");

        t1.start();
        t2.start();
        t3.start();

        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("Deadlock occurred! Threads are stuck.\n");
    }

    // ---------------- SOLUTION DEMO ----------------
    private static void runSolutionDemo() {
        Thread s1 = new Thread(new SolutionTask(), "Solution-Thread-1");
        Thread s2 = new Thread(new SolutionTask(), "Solution-Thread-2");
        Thread s3 = new Thread(new SolutionTask(), "Solution-Thread-3");

        s1.start();
        s2.start();
        s3.start();
    }

    // Deadlock tasks (different lock order)
    static class DeadlockTask1 implements Runnable {
        public void run() {
            synchronized (DLock1) {
                System.out.println(Thread.currentThread().getName() + ": Holding DLock1...");
                try { Thread.sleep(10); } catch (InterruptedException e) {}
                synchronized (DLock2) {
                    System.out.println(Thread.currentThread().getName() + ": Holding DLock1 & DLock2...");
                    synchronized (DLock3) {
                        System.out.println(Thread.currentThread().getName() + ": Holding all deadlock locks...");
                    }
                }
            }
        }
    }

    static class DeadlockTask2 implements Runnable {
        public void run() {
            synchronized (DLock2) {
                System.out.println(Thread.currentThread().getName() + ": Holding DLock2...");
                try { Thread.sleep(10); } catch (InterruptedException e) {}
                synchronized (DLock3) {
                    System.out.println(Thread.currentThread().getName() + ": Holding DLock2 & DLock3...");
                    synchronized (DLock1) {
                        System.out.println(Thread.currentThread().getName() + ": Holding all deadlock locks...");
                    }
                }
            }
        }
    }

    static class DeadlockTask3 implements Runnable {
        public void run() {
            synchronized (DLock3) {
                System.out.println(Thread.currentThread().getName() + ": Holding DLock3...");
                try { Thread.sleep(10); } catch (InterruptedException e) {}
                synchronized (DLock1) {
                    System.out.println(Thread.currentThread().getName() + ": Holding DLock3 & DLock1...");
                    synchronized (DLock2) {
                        System.out.println(Thread.currentThread().getName() + ": Holding all deadlock locks...");
                    }
                }
            }
        }
    }

    // Solution tasks (same lock order)
    static class SolutionTask implements Runnable {
        public void run() {
            synchronized (SLock1) {
                synchronized (SLock2) {
                    synchronized (SLock3) {
                        System.out.println(Thread.currentThread().getName() + ": Holding all solution locks in order...");
                    }
                }
            }
        }
    }
}
