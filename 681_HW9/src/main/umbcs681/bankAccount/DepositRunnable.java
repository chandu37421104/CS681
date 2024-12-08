package umbcs681.bankAccount;

public class DepositRunnable implements Runnable {
    private BankAccount account;

    public DepositRunnable(BankAccount account) {
        this.account = account;
    }

    public void run() {
        try {
            for (int i = 0; i < 10 && !ThreadSafeBankAccountMain.isDone(); i++) {
                account.deposit(100);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.out.println("Deposit thread interrupted");
        }
    }
}

