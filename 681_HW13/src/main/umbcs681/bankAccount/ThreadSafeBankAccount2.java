package umbcs681.bankAccount;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ThreadSafeBankAccount2 implements BankAccount {
    private double balance = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition sufficientFundsCondition = lock.newCondition();
    private final Condition belowUpperLimitFundsCondition = lock.newCondition();

    public void deposit(double amount) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId() + " (d): Entering deposit()...");
            while (balance >= 300) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getId() + " (d): Interrupted, exiting...");
                    return;
                }
                System.out.println(Thread.currentThread().getId() + " (d): Awaiting condition...");
                belowUpperLimitFundsCondition.await();
            }
            balance += amount;
            System.out.println(Thread.currentThread().getId() + " (d): Deposited, balance = " + balance);
            sufficientFundsCondition.signalAll();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getId() + " (d): Deposit interrupted, exiting...");
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getId() + " (d): Exiting deposit()...");
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId() + " (w): Entering withdraw()...");
            while (balance <= 0) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getId() + " (w): Interrupted, exiting...");
                    return; // Graceful exit
                }
                System.out.println(Thread.currentThread().getId() + " (w): Awaiting condition...");
                sufficientFundsCondition.await();
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getId() + " (w): Withdrawn, balance = " + balance);
            belowUpperLimitFundsCondition.signalAll();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getId() + " (w): Withdraw interrupted, exiting...");
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
