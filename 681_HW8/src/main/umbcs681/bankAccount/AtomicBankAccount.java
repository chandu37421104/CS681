package umbcs681.bankAccount;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicBankAccount implements BankAccount {
    private AtomicReference<Double> balance = new AtomicReference<>(0.0);

    public void deposit(double amount) {
        balance.accumulateAndGet(amount, Double::sum);
        System.out.println("Deposited: " + amount + ", New balance: " + balance.get());
    }

    public void withdraw(double amount) {
        balance.accumulateAndGet(-amount, Double::sum);
        System.out.println("Withdrew: " + amount + ", New balance: " + balance.get());
    }

    public double getBalance() {
        return balance.get();
    }

    public static void main(String[] args) {
        AtomicBankAccount bankAccount = new AtomicBankAccount();
        DepositRunnable depositRunnable = new DepositRunnable(bankAccount);
        WithdrawRunnable withdrawRunnable = new WithdrawRunnable(bankAccount);
    
        Thread depositThread = new Thread(depositRunnable);
        Thread withdrawThread = new Thread(withdrawRunnable);
    
        depositThread.start();
        withdrawThread.start();
    
        // Let the threads run for a while
        try {
            Thread.sleep(Duration.ofSeconds(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Stop threads after execution
        depositRunnable.stop();
        withdrawRunnable.stop();
    
        // Wait for threads to finish
        try {
            depositThread.join();
            withdrawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        System.out.println("Final balance: " + bankAccount.getBalance());
    }
    
}

