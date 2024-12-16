package umbcs681.bankAccount;

import java.time.Duration;

public class WithdrawRunnable implements Runnable {
    private BankAccount account;

    public WithdrawRunnable(BankAccount account) {
        this.account = account;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                account.withdraw(100);
                Thread.sleep(Duration.ofSeconds(2));
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}