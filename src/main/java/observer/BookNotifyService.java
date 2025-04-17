package observer;

import java.util.HashMap;
import java.util.Map;

public class BookNotifyService {
    private final Map<String, BookSubject> waitList = new HashMap<>();

    public void addWaitRequest(String isbn, BookObserver observer) {
        waitList.computeIfAbsent(isbn, BookSubject::new).addObserver(observer);
    }

    public void notifyNext(String isbn) {
        if (waitList.containsKey(isbn)) {
            waitList.get(isbn).notifyNextObserver();
        }
    }

    public boolean hasWaiters(String isbn) {
        return waitList.containsKey(isbn) && waitList.get(isbn).hasObservers();
    }
}