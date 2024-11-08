package umbcs681.prime;

import java.util.LinkedList;
import java.util.List;


public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {
    private volatile boolean done = false;
    
    public RunnableCancellablePrimeGenerator(long from, long to) {
        super(from, to);
        this.primes = new LinkedList<>();  
    }

    public void setDone() {
        done = true;
    }

    public void generatePrimes() {
        for (long n = from; n <= to; n++) {
            if (done) {
                System.out.println("Stopped generating prime numbers.");
                break; // Avoid clearing primes to maintain consistency
            }

            if (isPrime(n)) {
                primes.add(n);  // Thread-safe addition
            }

            if (done) {  // Double-check after prime check
                System.out.println("Stopped generating prime numbers.");
                break;
            }
        }
    }

    @Override
    public void run() {
        generatePrimes();
    }

    public static void main(String[] args) {
        // Create a prime generator to find primes between 1 and 100
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator(1, 100);
        Thread thread = new Thread(gen);
        thread.start();

        // Set done to true to cancel the generation
        gen.setDone();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Output the primes found
        gen.getPrimes().forEach((Long prime) -> System.out.print(prime + ", "));
        System.out.println("\n" + gen.getPrimes().size() + " primes are found.");
    }
}