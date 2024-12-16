package umbcs681.bankAccount;

public class WireTransferRunnable implements Runnable {
    private ThreadSafeBankAccount2 fromAccount;
    private ThreadSafeBankAccount2 toAccount;

    public WireTransferRunnable(ThreadSafeBankAccount2 fromAccount, ThreadSafeBankAccount2 toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                WireTransferHandler.transfer(fromAccount, toAccount, 50);
                Thread.sleep(2000); // Simulating delay
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}