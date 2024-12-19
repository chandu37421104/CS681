package umbcs681.observable;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StockQuoteObservable stockObservable = new StockQuoteObservable();

        // Add observers
        LineChartObserver lineChartObserver = new LineChartObserver();
        TableObserver tableObserver = new TableObserver();
        stockObservable.addObserver(lineChartObserver);
        stockObservable.addObserver(tableObserver);

        // Create and start 10+ data handler threads
        List<DataHandlerThread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String ticker = "Ticker" + i;
            double initialQuote = 100 + i * 10.0;
            DataHandlerThread thread = new DataHandlerThread(stockObservable, ticker, initialQuote);
            threads.add(thread);
            thread.start();
        }

        // Let the threads run for a few seconds
        try {
            Thread.sleep(5000); // Main thread waits for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 1: Signal threads to terminate
        for (DataHandlerThread thread : threads) {
            thread.terminate();
        }

        // Step 2: Wait for threads to finish
        for (DataHandlerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have terminated. Main thread exiting.");
    }
}
