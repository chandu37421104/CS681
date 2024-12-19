package umbcs681.observable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class StockQuoteObservable extends Observable<StockEvent> {
    private final ConcurrentHashMap<String, Double> stockQuotes = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void changeQuote(String ticker, double quote) {
        lock.lock();
        try {
            stockQuotes.put(ticker, quote);
            notifyObservers(new StockEvent(ticker, quote));
        } finally {
            lock.unlock();
        }
    }

    public Double getQuote(String ticker) {
        return stockQuotes.get(ticker);
    }
}
