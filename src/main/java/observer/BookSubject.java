package observer;

import java.util.LinkedList;
import java.util.Queue;

public class BookSubject {
    private final String isbn;
    private final Queue<BookObserver> observers = new LinkedList<>();

    public BookSubject(String isbn) {
        this.isbn = isbn;
    }

    public void addObserver(BookObserver observer) {
        observers.add(observer);
    }

    public void notifyNextObserver() {
        if (!observers.isEmpty()) {
            BookObserver observer = observers.poll();
            observer.notifyAvailable(isbn);
        }
    }

    public boolean hasObservers() {
        return !observers.isEmpty();
    }
}