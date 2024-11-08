package umbcs681.prime;


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
                System.out.println("Thread termination initiated: Cleaning up resources.");
                this.primes.clear(); 
                System.out.println("Prime generation stopped.");
                break;
            }

            if (isPrime(n)) {
                this.primes.add(n);
            }

            
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread was interrupted during prime generation.");
                break;
            }
        }
    }

    public static void main(String[] args) {
        // Setting up the prime generator to find primes between 1 and 100
        RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator(1, 100);
        Thread thread = new Thread(gen);
        thread.start();
    
        try {
            // Pause for a short time (e.g., 100 ms)
            Thread.sleep(100);
            
            // Step 1: Signal termination by setting done
            gen.setDone();
            
            // Step 2: Interrupt the thread to wake it if waiting or blocked
            thread.interrupt();
            
            // Wait for the thread to finish
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
      
        gen.getPrimes().forEach((Long prime) -> System.out.print(prime + ", "));
        System.out.println("\n" + gen.getPrimes().size() + " primes are found.");
    }
}



