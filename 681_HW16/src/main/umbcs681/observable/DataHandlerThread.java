package umbcs681.observable;

public class DataHandlerThread extends Thread {
    private final StockQuoteObservable observable;
    private final String ticker;
    private final double initialQuote;
    private volatile boolean running = true; // Termination signal

    public DataHandlerThread(StockQuoteObservable observable, String ticker, double initialQuote) {
        this.observable = observable;
        this.ticker = ticker;
        this.initialQuote = initialQuote;
    }

    public void terminate() {
        running = false; // Set the termination signal
    }

    @Override
    public void run() {
        double quote = initialQuote;
        while (running) {
            observable.changeQuote(ticker, quote);
            quote += Math.random() * 10 - 5; // Random fluctuation
            try {
                Thread.sleep(500); // Simulate delay
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + getName());
                running = false; // Exit if interrupted
            }
        }
        System.out.println("Terminating thread: " + getName());
    }
}
