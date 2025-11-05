package Thread;
class RollNumberTable extends Thread {
    public void run() {
        System.out.println("Table for Roll Number: 103");
        for (int i = 1; i <= 10; i++) {
            System.out.println("103 x " + i + " = " + (103 * i));
        }
    }
}

class DOBTable extends Thread {
    public void run() {
        System.out.println("Table for Date of Birth: 12");
        for (int i = 1; i <= 10; i++) {
            System.out.println("12 x " + i + " = " + (12 * i));
        }
    }
}

public class Main {
    public static void main(String[] args) {
        RollNumberTable rollTable = new RollNumberTable();
        DOBTable dobTable = new DOBTable();

        rollTable.start();
        dobTable.start();
    }
}