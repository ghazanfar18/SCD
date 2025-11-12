package Thread;
public class AlphabetThread extends Thread {
    private volatile boolean running = true;

    public void run() {
        for (int i = 0; i < 26 && running; i++) {
            try {
                char ch = (char) ('A' + i);
                long sleepTime = (long) (Math.random() * 900 + 100);
                Thread.sleep(sleepTime);
                System.out.println("Character: " + ch);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e);
            }
        }
    }

    public void stopThread() {
        running = false;
    }

    public static void main(String[] args) {
        AlphabetThread t1 = new AlphabetThread();
        AlphabetThread t2 = new AlphabetThread();

        t1.start();
        t2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        t2.stopThread(); // Custom stop method
        System.out.println("Thread t2 stopped safely");
    }
}