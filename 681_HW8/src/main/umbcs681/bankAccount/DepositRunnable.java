package umbcs681.bankAccount;

import java.time.Duration;

public class DepositRunnable implements Runnable {
    private BankAccount account;
    private volatile boolean done = false;

    public DepositRunnable(BankAccount account) {
        this.account = account;
    }

    public void run() {
        try {
            while (!done) {
                account.deposit(100);
                Thread.sleep(Duration.ofSeconds(2));
            }
        } catch (InterruptedException exception) {}
    }

    public void stop() {
        done = true;
    }
}
