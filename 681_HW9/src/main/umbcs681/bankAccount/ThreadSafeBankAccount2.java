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
            while (balance >= 300) {
                belowUpperLimitFundsCondition.await();
            }
            balance += amount;
            sufficientFundsCondition.signalAll();
        } catch (InterruptedException e) {
            System.out.println("Deposit interrupted");
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            while (balance <= 0) {
                sufficientFundsCondition.await();
            }
            balance -= amount;
            belowUpperLimitFundsCondition.signalAll();
        } catch (InterruptedException e) {
            System.out.println("Withdraw interrupted");
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
