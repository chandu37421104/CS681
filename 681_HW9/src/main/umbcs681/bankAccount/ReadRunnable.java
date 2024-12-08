package umbcs681.bankAccount;

public class ReadRunnable implements Runnable {
    private BankAccount account;

    public ReadRunnable(BankAccount account) {
        this.account = account;
    }

    @Override
    public void run() {
        while (!ThreadSafeBankAccountMain.isDone()) {
            System.out.println(Thread.currentThread().getName() + " (r): Balance: " + account.getBalance());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Read thread interrupted");
            }
        }
    }
}