import observer.BookNotifyService;
import observer.BookObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookNotifyServiceTest {

    private static class TestObserver implements BookObserver {
        String lastNotifiedIsbn = null;

        @Override
        public void notifyAvailable(String isbn) {
            this.lastNotifiedIsbn = isbn;
        }
    }

    @Test
    public void testAddWaitRequest_NewIsbnCreatesSubject() {
        BookNotifyService service = new BookNotifyService();
        TestObserver observer = new TestObserver();

        service.addWaitRequest("111", observer);

        assertTrue(service.hasWaiters("111"));
    }

    @Test
    public void testAddWaitRequest_ExistingIsbnAddsToSameSubject() {
        BookNotifyService service = new BookNotifyService();
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();

        service.addWaitRequest("222", observer1);
        service.addWaitRequest("222", observer2);

        assertTrue(service.hasWaiters("222"));
    }

    @Test
    public void testNotifyNext_WhenIsbnExists() {
        BookNotifyService service = new BookNotifyService();
        TestObserver observer = new TestObserver();

        service.addWaitRequest("333", observer);
        service.notifyNext("333");

        assertEquals("333", observer.lastNotifiedIsbn);
        assertFalse(service.hasWaiters("333"));
    }

    @Test
    public void testNotifyNext_WhenIsbnDoesNotExist() {
        BookNotifyService service = new BookNotifyService();

        service.notifyNext("999");

        assertFalse(service.hasWaiters("999"));
    }
}
