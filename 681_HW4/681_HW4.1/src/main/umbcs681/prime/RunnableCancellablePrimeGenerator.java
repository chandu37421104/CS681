package umbcs681.prime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private boolean done = false;
    private final ReentrantLock lock = new ReentrantLock();

    public RunnableCancellablePrimeGenerator(long from, long to) {
        super(from, to);
        this.primes = new LinkedList<>();
    }

    public void setDone() {
        lock.lock();
        try {
            done = true;
        } finally {
            lock.unlock();
        }
    }

    public void generatePrimes() {
        for (long n = from; n <= to; n++) {
            lock.lock();
            try {
                if (done) {
                    System.out.println("Stopped generating prime numbers.");
                    this.primes.clear(); 
                    break;
                }
            } finally {
                lock.unlock();
            }

            if (isPrime(n)) {
                lock.lock();
                try {
                    if (!done) { 
                        this.primes.add(n);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        // Define a range for generating prime numbers
        RunnableCancellablePrimeGenerator generator = new RunnableCancellablePrimeGenerator(1, 100000);

        
        Thread thread = new Thread(generator);
        thread.start();

        
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       
        generator.setDone();

        
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Output the results
        System.out.println("Prime numbers generated:");
        generator.getPrimes().forEach((Long prime) -> System.out.print(prime + ", "));
        System.out.println("\nTotal primes generated: " + generator.getPrimes().size());
    }
}
