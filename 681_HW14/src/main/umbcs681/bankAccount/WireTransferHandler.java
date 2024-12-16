package umbcs681.bankAccount;

import java.util.concurrent.locks.Lock;

public class WireTransferHandler {

    public static void transfer(ThreadSafeBankAccount2 from, ThreadSafeBankAccount2 to, double amount) {
        // Avoid locking the same account
        if (from == to) {
            System.out.println(Thread.currentThread().getName() + ": Cannot transfer to the same account.");
            return;
        }

        ThreadSafeBankAccount2 first = from;
        ThreadSafeBankAccount2 second = to;

        // Enforce globally-fixed lock order
        if (System.identityHashCode(from) > System.identityHashCode(to)) {
            first = to;
            second = from;
        }

        Lock lock1 = first.getLock();
        Lock lock2 = second.getLock();

        lock1.lock();
        try {
            lock2.lock();
            try {
                if (from.getBalance() >= amount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                    System.out.println(Thread.currentThread().getName() +
                            ": Transferred " + amount +
                            " from " + from +
                            " to " + to);
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            ": Insufficient balance in " + from + " for transfer.");
                }
            } finally {
                lock2.unlock();
            }
        } finally {
            lock1.unlock();
        }
    }
}
