package umbcs681.prime;

import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    private volatile boolean done = false;
    private final ReentrantLock lock = new ReentrantLock();

    public RunnableCancellablePrimeFactorizer(long dividend, long from, long to) {
        super(dividend, from, to);
    }

    // Step 1: Set the flag to stop the thread
    public void setDone() {
        done = true;
    }

    // Step 2: Check the flag and stop processing if set
    @Override
    public void generatePrimeFactors() {
        long divisor = from;
        while (!done && dividend != 1 && divisor <= to) {
            if (divisor > 2 && isEven(divisor)) {
                divisor++;
                continue;
            }
            if (dividend % divisor == 0) {
                factors.add(divisor);
                dividend /= divisor;
            } else {
                divisor++;
            }
            // Check for termination after each division step
            if (done) {
                System.out.println("Thread termination initiated: Cleaning up resources.");
                factors.clear(); // Clean up resources if necessary
                System.out.println("Prime factorization stopped.");
                break;
            }
        }
    }


    public static void main(String[] args) {
        // Currently the PrimeFactorizer is set to provide prime factors of 84
        RunnableCancellablePrimeFactorizer gen = new RunnableCancellablePrimeFactorizer(84, 2, (long) Math.sqrt(84));
        Thread thread = new Thread(gen);
        thread.start();
    
        try {
            // Simulate some condition after which we want to stop the thread
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
    
        // Output the factors
        gen.getPrimeFactors().forEach((Long factor) -> System.out.print(factor + ", "));
        System.out.println("\n" + gen.getPrimeFactors().size() + " prime factors are found.");
    }
    
}
