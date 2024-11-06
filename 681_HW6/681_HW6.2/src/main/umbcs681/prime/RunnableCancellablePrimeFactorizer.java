package umbcs681.prime;

import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    private volatile boolean done = false;
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
        lock.lock();
        try {
            while (!done && dividend != 1 && divisor <= to) {
                if (divisor > 2 && isEven(divisor)) {
                    divisor++;
                    continue;
                }
                if (dividend % divisor == 0) {
                    factors.add(divisor);
                    dividend /= divisor;
                } else {
                    if (divisor == 2) {
                        divisor++;
                    } else {
                        divisor += 2;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        generatePrimeFactors();
        if (!done) {
            System.out.println("Thread #" + Thread.currentThread().getId() + " generated " + factors);
        } else {
            System.out.println("Thread #" + Thread.currentThread().getId() + " was cancelled.");
        }
    }

    public static void main(String[] args) {
        // Create a prime factorizer for the number 84 within a specific range
        RunnableCancellablePrimeFactorizer gen = new RunnableCancellablePrimeFactorizer(84, 2, (long) Math.sqrt(84));
        Thread thread = new Thread(gen);
        thread.start();
    
        try {
            // Wait for the thread to complete
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Output the prime factors found
        gen.getPrimeFactors().forEach((Long factor) -> System.out.print(factor + ", "));
        System.out.println("\n" + gen.getPrimeFactors().size() + " prime factors are found.");
    }
    
}
