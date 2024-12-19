package umbcs681.observable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void addObserver(Observer<T> observer) {
        lock.lock();
        try {
            observers.add(observer);
        } finally {
            lock.unlock();
        }
    }

    public void removeObserver(Observer<T> observer) {
        lock.lock();
        try {
            observers.remove(observer);
        } finally {
            lock.unlock();
        }
    }

    public void notifyObservers(T event) {
        List<Observer<T>> observersSnapshot;
        lock.lock();
        try {
            observersSnapshot = new ArrayList<>(observers); // Snapshot for thread-safety
        } finally {
            lock.unlock();
        }

        // Notify outside lock (open call)
        for (Observer<T> observer : observersSnapshot) {
            observer.update(event);
        }
    }
}
