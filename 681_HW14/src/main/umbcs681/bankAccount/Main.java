package umbcs681.bankAccount;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Create two bank accounts
        ThreadSafeBankAccount2 account1 = new ThreadSafeBankAccount2();
        ThreadSafeBankAccount2 account2 = new ThreadSafeBankAccount2();

        // Create lists to hold the threads
        ArrayList<Thread> threads = new ArrayList<>();

        // Start deposit threads
        for (int i = 0; i < 2; i++) {
            Thread depositThread = new Thread(new DepositRunnable(account1), "DepositThread-" + i);
            threads.add(depositThread);
            depositThread.start();
        }

        // Start withdrawal threads
        for (int i = 0; i < 2; i++) {
            Thread withdrawThread = new Thread(new WithdrawRunnable(account1), "WithdrawThread-" + i);
            threads.add(withdrawThread);
            withdrawThread.start();
        }

        // Start wire transfer threads
        Thread wireTransferThread1 = new Thread(new WireTransferRunnable(account1, account2), "WireTransferThread-1");
        Thread wireTransferThread2 = new Thread(new WireTransferRunnable(account2, account1), "WireTransferThread-2");
        threads.add(wireTransferThread1);
        threads.add(wireTransferThread2);

        wireTransferThread1.start();
        wireTransferThread2.start();

        // Let the threads run for some time
        try {
            Thread.sleep(20000); // Allow 20 seconds for threads to execute
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted while sleeping");
        }

        // 2-step thread termination
        System.out.println("Main thread: Initiating thread termination...");
        for (Thread thread : threads) {
            thread.interrupt(); // Step 1: Interrupt all threads
        }

        for (Thread thread : threads) {
            try {
                thread.join(); // Step 2: Wait for all threads to finish
                System.out.println(thread.getName() + " terminated.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main thread interrupted during join");
            }
        }

        System.out.println("Main thread: All threads terminated. Final balances:");
        System.out.println("Account 1 Balance: " + account1.getBalance());
        System.out.println("Account 2 Balance: " + account2.getBalance());
    }
}
