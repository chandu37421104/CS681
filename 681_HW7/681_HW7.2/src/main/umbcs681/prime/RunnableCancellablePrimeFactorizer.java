package umbcs681.prime;

import java.util.LinkedList;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    private volatile boolean done = false;

    public RunnableCancellablePrimeFactorizer(long dividend, long from, long to) {
        super(dividend, from, to);
        factors = new LinkedList<Long>(); 
    }

    
    public void setDone() {
        done = true;
    }

    @Override
    public void generatePrimeFactors() {
        long divisor = from;
        
        while (!done && dividend != 1 && divisor <= to) {
            
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread was interrupted. Exiting gracefully.");
                break;
            }

            if (divisor > 2 && isEven(divisor)) {
                divisor++;
                continue;
            }

            if (dividend % divisor == 0) {
                factors.add(divisor); 
                dividend /= divisor;
            } else {
                divisor = (divisor == 2) ? 3 : divisor + 2;
            }
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
        // Instantiate the factorizer
        RunnableCancellablePrimeFactorizer gen = new RunnableCancellablePrimeFactorizer(84, 2, (long) Math.sqrt(84));
        Thread thread = new Thread(gen);
        thread.start();
    
        try {
            Thread.sleep(100); // Short pause
            gen.setDone();     // Step 1: Signal termination
            thread.interrupt(); // Step 2: Interrupt the thread
            thread.join();      // Wait for the thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Output the prime factors found
        gen.getPrimeFactors().forEach((Long factor) -> System.out.print(factor + ", "));
        System.out.println("\n" + gen.getPrimeFactors().size() + " prime factors are found.");
    }
}
