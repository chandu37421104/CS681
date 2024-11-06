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
}

