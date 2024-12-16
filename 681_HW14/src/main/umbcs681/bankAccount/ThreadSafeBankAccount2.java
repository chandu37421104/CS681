package umbcs681.bankAccount;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ThreadSafeBankAccount2 implements BankAccount {
    private double balance = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition sufficientFundsCondition = lock.newCondition();
    private Condition belowUpperLimitFundsCondition = lock.newCondition();

    public void deposit(double amount) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " (d): Lock obtained for deposit");
            while (balance >= 300) {
                belowUpperLimitFundsCondition.await();
            }
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " (d): New balance: " + balance);
            sufficientFundsCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " (d): Lock released for deposit");
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " (w): Lock obtained for withdraw");
            while (balance <= 0) {
                sufficientFundsCondition.await();
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " (w): New balance: " + balance);
            belowUpperLimitFundsCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " (w): Lock released for withdraw");
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public ReentrantLock getLock() {
        return this.lock;
    }
}
