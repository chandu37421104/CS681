package umbcs681.observable;


public class LineChartObserver implements Observer<StockEvent> {
    @Override
    public void update(StockEvent event) {
        System.out.println("LineChartObserver: Ticker - " + event.ticker() + ", Quote - " + event.quote());
    }
}
