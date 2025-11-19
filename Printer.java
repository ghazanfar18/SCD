class Printer {
    private int pagesAvailable;
    public Printer(int initialPages) {
        this.pagesAvailable = initialPages;
    }

    // Called by the printing thread
    public synchronized void printJob(String jobName, int pagesNeeded) {
        System.out.println(jobName + " requested " + pagesNeeded + " pages. (Available: " + pagesAvailable + ")");
        while (pagesNeeded > pagesAvailable) {
            System.out.println(jobName + " waiting - not enough pages. (Available: " + pagesAvailable + ")");
            try {
                wait(); // release lock and wait to be notified when more pages arrive
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(jobName + " interrupted while waiting.");
                return;
            }
        }
        // Enough pages — proceed to print
        pagesAvailable -= pagesNeeded;
        System.out.println(jobName + " printing " + pagesNeeded + " pages. (Remaining: " + pagesAvailable + ")");
    }

    // Called by the refill thread
    public synchronized void addPages(int pages) {
        System.out.println("Refill: Adding " + pages + " pages.");
        pagesAvailable += pages;
        System.out.println("Refill done. New available: " + pagesAvailable);
        notifyAll(); // wake waiting print thread(s)
    }
}

class PrintThread extends Thread {
    private Printer printer;
    private String jobName;
    private int pages;

    public PrintThread(Printer p, String jobName, int pages) {
        this.printer = p;
        this.jobName = jobName;
        this.pages = pages;
    }

    @Override
    public void run() {
        printer.printJob(jobName, pages);
    }
}

class RefillThread extends Thread {
    private Printer printer;
    private int[] refills; // sequence of refills to perform (pages added each time)
    private int delayMs; // delay between refills to simulate time

    public RefillThread(Printer printer, int[] refills, int delayMs) {
        this.printer = printer;
        this.refills = refills;
        this.delayMs = delayMs;
    }

    @Override
    public void run() {
        for (int p : refills) {
            try { Thread.sleep(delayMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            printer.addPages(p);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Printer printer = new Printer(10); // initial pages = 10

        // A print job requesting 15 pages -> will have to wait
        PrintThread job = new PrintThread(printer, "Job-1", 15);

        // Refill sequence: first add 3 pages, then add 5 pages => after both refills, enough (10 + 3 + 5 = 18)
        int[] refills = {3, 5};
        RefillThread refill = new RefillThread(printer, refills, 1000);

        job.start();
        refill.start();

        job.join();
        refill.join();

        System.out.println("All threads finished.");
    }
}