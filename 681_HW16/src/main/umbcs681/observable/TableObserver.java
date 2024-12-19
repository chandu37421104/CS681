package umbcs681.observable;

public class TableObserver implements Observer<StockEvent> {
    @Override
    public void update(StockEvent event) {
        System.out.println("TableObserver: Ticker - " + event.ticker() + ", Quote - " + event.quote());
    }
}
