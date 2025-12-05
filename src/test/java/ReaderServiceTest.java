import factory.ReaderFactory;
import model.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ReaderService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderServiceTest {

    private ReaderService readerService;
    private ReaderFactory readerFactory;

    @BeforeEach
    public void setup() {
        readerService = new ReaderService();
        readerFactory = new ReaderFactory();
    }

    @Test
    public void testRegisterReaderSuccessfully() {
        Reader reader = readerFactory.create("1", "Alice", null);
        readerService.registerReader(reader);

        Map<String, Reader> readers = readerService.getReaders();
        assertEquals(1, readers.size());
        assertTrue(readers.containsKey("1"));
    }

    @Test
    public void testRegisterReaderWithDuplicateIdThrowsException() {
        Reader reader1 = readerFactory.create("1", "Alice", null);
        Reader reader2 = readerFactory.create("1", "Bob", null);

        readerService.registerReader(reader1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            readerService.registerReader(reader2);
        });

        assertEquals("Reader with this ID already exists.", exception.getMessage());
        assertEquals(1, readerService.getReaders().size());
    }

    @Test
    public void testGetAllReadersJson() {
        Reader reader1 = readerFactory.create("1", "Alice", null);
        Reader reader2 = readerFactory.create("2", "Bob", null);

        readerService.registerReader(reader1);
        readerService.registerReader(reader2);

        String json = readerService.getAllReadersJson();
        assertTrue(json.contains("Alice"));
        assertTrue(json.contains("Bob"));
        assertTrue(json.contains("1"));
        assertTrue(json.contains("2"));
    }

    @Test
    public void testGetReadersReturnsMap() {
        Reader reader = readerFactory.create("1", "Alice", null);
        readerService.registerReader(reader);

        Map<String, Reader> readers = readerService.getReaders();
        assertNotNull(readers);
        assertEquals(1, readers.size());
        assertEquals(reader, readers.get("1"));
    }
}