package com.mycompany.warehousedemo;
//2023F-bSE-103
import java.util.concurrent.ThreadLocalRandom;

public class WarehouseDemo {
    public static void main(String[] args) throws Exception {
        Bin binA = new Bin("Bin-A", 1000), binB = new Bin("Bin-B", 200);
        log("Start: %s=%d, %s=%d", binA.id, binA.get(), binB.id, binB.get());

        // --- Deadlock demo: opposite lock order (intentional) ---
        log("DEADLOCK demo...");
        TransferWorker d1 = new TransferWorker("DL-1", binA, binB, true);
        TransferWorker d2 = new TransferWorker("DL-2", binB, binA, true);
        d1.start(); d2.start(); Thread.sleep(1200);
        log("States (expect BLOCKED): %s=%s | %s=%s", d1.getName(), d1.getState(), d2.getName(), d2.getState());
        d1.stopThread(); d2.stopThread(); // flags won't help while stuck

        // --- Safe transfers: consistent lock ordering avoids deadlock ---
        log("SAFE transfer demo...");
        TransferWorker s1 = new TransferWorker("SAFE-1", binA, binB, false);
        TransferWorker s2 = new TransferWorker("SAFE-2", binB, binA, false);
        s1.start(); s2.start();

        // --- Inter-thread communication: monitor waits via wait(), supplier notifies via deposit() ---
        long target = 1500;
        InventoryMonitor monitor = new InventoryMonitor("Monitor", binB, target);
        SupplierWorker supplier = new SupplierWorker("Supplier", binB, new long[]{100,200,150,300,250,500});
        monitor.start(); supplier.start();

        Thread.sleep(2500);
        s1.stopThread(); s2.stopThread(); supplier.stopThread(); monitor.stopThread();
        s1.join(1000); s2.join(1000); supplier.join(1000); monitor.join(1000);
        log("End: %s=%d, %s=%d", binA.id, binA.get(), binB.id, binB.get());
        log("Done. Extend main() as needed.");
    }
    static void log(String fmt, Object... a){ System.out.printf("[MAIN] " + fmt + "%n", a); }
}

/** Base class (inheritance): shared lifecycle, safe stop, logging, sleep helper. */
abstract class Worker implements Runnable {
    protected volatile boolean running = true; private final String name; private Thread t;
    protected Worker(String name){ this.name=name; }
    public void start(){ t=new Thread(this,name); t.start(); log("started"); }
    public void stopThread(){ running=false; log("stop requested"); }
    public void join(long ms) throws InterruptedException { if(t!=null) t.join(ms); }
    public String getName(){ return name; } public Thread.State getState(){ return t==null?Thread.State.NEW:t.getState(); }
    protected void nap(long ms){ try{ Thread.sleep(ms);}catch(InterruptedException e){ Thread.currentThread().interrupt(); running=false; } }
    protected void log(String fmt,Object...a){ System.out.printf("[%s] "+fmt+"%n", name, a); }
}

/** Shared resource with wait/notify for inter-thread communication. */
class Bin {
    final String id; private long stock;
    Bin(String id,long s){ this.id=id; this.stock=s; }
    synchronized long get(){ return stock; }
    synchronized boolean take(long n){ if(stock>=n){ stock-=n; return true; } return false; }
    synchronized void put(long n){ stock+=n; notifyAll(); } // wake up waiters (monitor)
    public String toString(){ return id; }
}

/** Deadlock vs safe ordering (by identity hash). */
class TransferWorker extends Worker {
    private final Bin from,to; private final boolean deadlockMode;
    TransferWorker(String name,Bin f,Bin t,boolean dl){ super(name); from=f; to=t; deadlockMode=dl; }
    public void run(){
        while(running){
            long n = ThreadLocalRandom.current().nextLong(10,101);
            if(deadlockMode){
                synchronized(from){ nap(20); synchronized(to){ move(n); } }
            } else {
                Bin a=from,b=to; if(System.identityHashCode(a)>System.identityHashCode(b)){ a=to; b=from; }
                synchronized(a){ synchronized(b){ move(n); } }
            }
            nap(40);
        }
        log("stopped");
    }
    private void move(long n){
        if(from.take(n)){ to.put(n); log("moved %,d %s->%s | %s=%d, %s=%d", n, from, to, from, from.get(), to, to.get()); }
        else log("insufficient in %s (need %,d, has %d)", from, n, from.get());
    }
}

/** Supplier deposits amounts and triggers notifyAll via Bin.put. */
class SupplierWorker extends Worker {
    private final Bin bin; private final long[] amounts;
    SupplierWorker(String name,Bin b,long[] arr){ super(name); bin=b; amounts=arr.clone(); }
    public void run(){
        for(long x: amounts){ if(!running) break; nap(120); bin.put(x); log("put %,d into %s | stock=%d", x, bin, bin.get()); }
        log("finished supplies");
    }
}

/** Monitor waits until target stock using wait/notify with timeout. */
class InventoryMonitor extends Worker {
    private final Bin bin; private final long target;
    InventoryMonitor(String name,Bin b,long tgt){ super(name); bin=b; target=tgt; }
    public void run(){
        synchronized(bin){
            while(running && bin.get()<target){
                log("waiting: %s stock %,d / target %,d", bin, bin.get(), target);
                try{ bin.wait(300);}catch(InterruptedException e){ Thread.currentThread().interrupt(); running=false; }
            }
            if(bin.get()>=target) log("target reached: %s=%,d", bin, bin.get()); else log("stopped early (%,d)", bin.get());
        }
        log("monitor exit");
    }
}
