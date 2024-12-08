package umbcs681.bankAccount;

public class WithdrawRunnable implements Runnable {
    private BankAccount account;

    public WithdrawRunnable(BankAccount account) {
        this.account = account;
    }

    public void run() {
        try {
            for (int i = 0; i < 10 && !ThreadSafeBankAccountMain.isDone(); i++) {
                account.withdraw(100);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.out.println("Withdraw thread interrupted");
        }
    }
}

