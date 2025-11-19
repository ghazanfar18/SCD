class JointAccount {
    private int balance;

    public JointAccount(int initial) {
        this.balance = initial;
    }

    // synchronized withdraw method => only one thread can execute at a time
    public synchronized boolean withdraw(String user, int amount) {
        System.out.println(user + " trying to withdraw: " + amount);
        if (amount <= balance) {
            // simulate some processing time
            try { Thread.sleep(200); } catch (InterruptedException e) {}
            balance -= amount;
            System.out.println(user + " completed withdraw of " + amount + ". Remaining balance: " + balance);
            return true;
        } else {
            System.out.println(user + " cannot withdraw " + amount + ". Insufficient funds. Current balance: " + balance);
            return false;
        }
    }

    public synchronized int getBalance() {
        return balance;
    }
}

class WithdrawThread extends Thread {
    private JointAccount account;
    private String user;
    private int amount;

    public WithdrawThread(JointAccount account, String user, int amount) {
        this.account = account;
        this.user = user;
        this.amount = amount;
    }

    @Override
    public void run() {
        account.withdraw(user, amount);
    }
}

public class Bank {
    public static void main(String[] args) throws InterruptedException {
        JointAccount account = new JointAccount(50000);

        WithdrawThread userA = new WithdrawThread(account, "User A", 45000);
        WithdrawThread userB = new WithdrawThread(account, "User B", 20000);

        // Start both threads almost simultaneously
        userA.start();
        userB.start();

        // Wait for both to finish
        userA.join();
        userB.join();

        System.out.println("Final balance: " + account.getBalance());
    }
}