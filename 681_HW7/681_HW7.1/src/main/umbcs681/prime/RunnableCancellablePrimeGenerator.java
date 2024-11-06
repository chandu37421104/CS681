package umbcs681.prime;

import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private volatile boolean done = false;

    public RunnableCancellablePrimeGenerator(long from, long to) {
        super(from, to);
    }

    // Step 1: Set the flag to stop the thread
    public void setDone() {
        done = true;
    }

    // Step 2: Check the flag and stop processing if set
    public void generatePrimes() {
        for (long n = from; n <= to; n++) {
            if (done) {
                System.out.println("Thread termination initiated: Cleaning up resources.");
                this.primes.clear(); // Clean up resources if necessary
                System.out.println("Prime generation stopped.");
                break;
            }

            if (isPrime(n)) {
                this.primes.add(n);
            }
        }
    }

    public static void main(String[] args) {
        // Setting up the prime generator to find primes between 1 and 100
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator(1, 100);
        Thread thread = new Thread(gen);
        thread.start();
    
        try {
            
            Thread.sleep(100); // Pause for a short time (e.g., 100 ms)
            
            // Step 1: Signal termination by setting done
            gen.setDone();
            
            // Step 2: Interrupt the thread
            thread.interrupt();
            
            // Wait for the thread to join
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Output the primes found
        gen.getPrimes().forEach((Long prime) -> System.out.print(prime + ", "));
        System.out.println("\n" + gen.getPrimes().size() + " primes are found.");
    }
    
}


