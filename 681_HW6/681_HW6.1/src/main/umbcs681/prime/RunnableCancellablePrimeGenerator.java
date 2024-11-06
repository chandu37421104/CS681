package umbcs681.prime;

import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private volatile boolean done = false;

    public RunnableCancellablePrimeGenerator(long from, long to) {
        super(from, to);
    }
    
    public void setDone() {
        done = true;
    }

    public void generatePrimes() {
        for (long n = from; n <= to; n++) {
            if (done) {
                System.out.println("Stopped generating prime numbers.");
                this.primes.clear();
                break;
            }

            if (isPrime(n)) {
                this.primes.add(n);
            }
        }
    }

    public static void main(String[] args) {
        // Create a prime generator to find primes between 1 and 100
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator(1, 100);
        Thread thread = new Thread(gen);
        thread.start();
    
        try {
            // Wait for the thread to complete
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Output the primes found
        gen.getPrimes().forEach((Long prime) -> System.out.print(prime + ", "));
        System.out.println("\n" + gen.getPrimes().size() + " primes are found.");
    }
}

