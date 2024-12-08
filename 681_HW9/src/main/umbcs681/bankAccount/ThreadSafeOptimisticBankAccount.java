package umbcs681.bankAccount;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeOptimisticBankAccount extends ThreadSafeBankAccount2 {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void deposit(double amount) {
        lock.writeLock().lock();
        try {
            super.deposit(amount);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void withdraw(double amount) {
        lock.writeLock().lock();
        try {
            super.withdraw(amount);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public double getBalance() {
        lock.readLock().lock();
        try {
            return super.getBalance();
        } finally {
            lock.readLock().unlock();
        }
    }
}
