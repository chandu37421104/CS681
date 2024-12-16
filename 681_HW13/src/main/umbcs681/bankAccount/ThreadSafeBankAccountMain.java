package umbcs681.bankAccount;

import java.util.ArrayList;

public class ThreadSafeBankAccountMain {
    public static void main(String[] args) throws InterruptedException {
        ThreadSafeBankAccount2 bankAccount = new ThreadSafeBankAccount2();
        ArrayList<Thread> threads = new ArrayList<>();

        System.out.println("Starting experiment with deposit and withdrawal threads...");
        for (int i = 0; i < 1; i++) { // Start with fewer threads for debugging
            Thread depositThread = new Thread(new DepositRunnable(bankAccount));
            Thread withdrawThread = new Thread(new WithdrawRunnable(bankAccount));
            threads.add(depositThread);
            threads.add(withdrawThread);
            depositThread.start();
            withdrawThread.start();
            System.out.println("Started threads: Deposit " + depositThread.getId() + ", Withdraw " + withdrawThread.getId());
        }

        // Let threads run for 2 seconds
        System.out.println("Main thread sleeping for 2 seconds...");
        Thread.sleep(2000);

        // Interrupt all threads
        System.out.println("Main thread: Interrupting all threads...");
        for (Thread thread : threads) {
            thread.interrupt();
        }

        // Join all threads to ensure termination
        for (Thread thread : threads) {
            thread.join();
            System.out.println("Thread " + thread.getId() + " has terminated.");
        }

        System.out.println("Main thread: All threads terminated. Final balance = " + bankAccount.getBalance());
    }
}
