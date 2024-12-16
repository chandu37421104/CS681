package umbcs681.bankAccount;

import java.time.Duration;

public class DepositRunnable implements Runnable {
    private BankAccount account;

    public DepositRunnable(BankAccount account) {
        this.account = account;
    }

    public void run() {
        System.out.println(Thread.currentThread().getId() + " (d): Starting DepositRunnable...");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                account.deposit(100);
                Thread.sleep(Duration.ofMillis(500));
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getId() + " (d): Interrupted, exiting...");
        }
        System.out.println(Thread.currentThread().getId() + " (d): DepositRunnable terminating...");
    }
    
}

