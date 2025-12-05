import observer.BookObserver;
import observer.BookSubject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookSubjectTest {

    private static class TestObserver implements BookObserver {
        String lastNotifiedIsbn = null;

        @Override
        public void notifyAvailable(String isbn) {
            this.lastNotifiedIsbn = isbn;
        }
    }

    @Test
    public void testNotifyNextObserver_WhenQueueNotEmpty() {
        BookSubject subject = new BookSubject("12345");
        TestObserver observer = new TestObserver();

        subject.addObserver(observer);
        assertTrue(subject.hasObservers());

        subject.notifyNextObserver();

        assertEquals("12345", observer.lastNotifiedIsbn);
        assertFalse(subject.hasObservers());
    }

    @Test
    public void testNotifyNextObserver_WhenQueueEmpty() {
        BookSubject subject = new BookSubject("67890");

        assertFalse(subject.hasObservers());

        subject.notifyNextObserver();

        assertFalse(subject.hasObservers());
    }
}