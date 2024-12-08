package umbcs681.bankAccount;


import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadSafeBankAccountMain {
    private static AtomicBoolean done = new AtomicBoolean(false);

    public static void setDone(boolean value) {
        done.set(value);
    }

    public static boolean isDone() {
        return done.get();
    }

    public static void main(String[] args) throws InterruptedException {
        int[] readerThreadCounts = {1, 5, 10, 20}; // Experiment with different numbers of reader threads

        for (int readerCount : readerThreadCounts) {
            System.out.println("\nExperiment with " + readerCount + " reader threads:");

            BankAccount bankAccount = new ThreadSafeOptimisticBankAccount();
            Thread[] threads = new Thread[readerCount + 10]; // 10 threads for deposits and withdrawals

            // Create deposit and withdrawal threads
            for (int i = 0; i < 5; i++) {
                threads[i] = new Thread(new DepositRunnable(bankAccount));
                threads[i + 5] = new Thread(new WithdrawRunnable(bankAccount));
            }

            // Create reader threads
            for (int i = 0; i < readerCount; i++) {
                threads[i + 10] = new Thread(new ReadRunnable(bankAccount));
            }

            // Start threads and measure execution time
            long startTime = System.currentTimeMillis();

            for (Thread t : threads) {
                t.start();
            }

            // Let threads run for 20 seconds
            Thread.sleep(20000);

            // Signal threads to terminate
            setDone(true);

            // Wait for threads to finish
            for (Thread t : threads) {
                t.join();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        }

        System.out.println("Experiment completed.");
    }
}
