package umbcs681.prime;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    private boolean done = false;
    private final ReentrantLock lock = new ReentrantLock();

    public RunnableCancellablePrimeFactorizer(long dividend, long from, long to) {
        super(dividend, from, to);
    }

    public void setDone() {
        lock.lock();
        try {
            done = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void generatePrimeFactors() {
        long divisor = from;

        while (true) {
            lock.lock();
            try {
                if (done || dividend == 1 || divisor > to) break; 
                
                if (divisor > 2 && isEven(divisor)) {
                    divisor++;
                    continue;
                }
                if (dividend % divisor == 0) {
                    factors.add(divisor);
                    dividend /= divisor;
                } else {
                    divisor = (divisor == 2) ? divisor + 1 : divisor + 2;
                }
            } finally {
                lock.unlock(); // Unlock after each iteration to allow other threads access
            }
        }
    }

    @Override
    public void run() {
        generatePrimeFactors();
        
        lock.lock();
        try {
            if (!done) {
                System.out.println("Thread #" + Thread.currentThread().getId() + " generated " + factors);
            } else {
                System.out.println("Thread #" + Thread.currentThread().getId() + " was cancelled.");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        System.out.println("Factorization of 84 with cancellable threads");

        long numberToFactorize = 84;
        RunnableCancellablePrimeFactorizer factorizer1 = new RunnableCancellablePrimeFactorizer(numberToFactorize, 2, (long) Math.sqrt(numberToFactorize) / 2);
        RunnableCancellablePrimeFactorizer factorizer2 = new RunnableCancellablePrimeFactorizer(numberToFactorize, 1 + (long) Math.sqrt(numberToFactorize) / 2, (long) Math.sqrt(numberToFactorize));

        Thread thread1 = new Thread(factorizer1);
        Thread thread2 = new Thread(factorizer2);

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(50);
            factorizer1.setDone();
            System.out.println("Requested cancellation of Thread #" + thread1.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LinkedList<Long> factors = new LinkedList<>();
        factors.addAll(factorizer1.getPrimeFactors());
        factors.addAll(factorizer2.getPrimeFactors());

        System.out.println("Prime factors of " + numberToFactorize + ": " + factors);
    }
}
