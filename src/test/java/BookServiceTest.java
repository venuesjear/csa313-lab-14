import factory.BookFactory;
import model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private BookService bookService;
    private BookFactory bookFactory;

    @BeforeEach
    public void setup() {
        bookService = new BookService();
        bookFactory = new BookFactory();

        bookService.addBook(bookFactory.create("101", "Pride and Prejudice", "Jane Austen"));
        bookService.addBook(bookFactory.create("102", "The Great Gatsby", "F. Scott Fitzgerald"));
        bookService.addBook(bookFactory.create("103", "Frankenstein", "Mary Shelley"));
    }

    @Test
    public void testAddBookSuccessfully() {
        Book newBook = bookFactory.create("104", "War and Peace", "Leo Tolstoy");
        bookService.addBook(newBook);

        assertEquals(4, bookService.getBooks().size());
        assertTrue(bookService.getBooks().containsKey("104"));
    }

    @Test
    public void testAddBookDuplicateISBN() {
        Book duplicate = bookFactory.create("101", "Fake Book", "Fake Author");

        assertThrows(IllegalArgumentException.class, () ->
                bookService.addBook(duplicate)
        );
    }

    @Test
    public void testGetAllBooksJson() {
        String json = bookService.getAllBooksJson();

        assertTrue(json.contains("Pride and Prejudice"));
        assertTrue(json.contains("The Great Gatsby"));
        assertTrue(json.contains("Frankenstein"));
    }

    @Test
    public void testGetAvailableBooksJson() {
        Book book = bookService.getBooks().get("102");
        book.setAvailability(false);

        String json = bookService.getAvailableBooksJson();

        assertTrue(json.contains("Pride and Prejudice"));
        assertFalse(json.contains("The Great Gatsby"));
        assertTrue(json.contains("Frankenstein"));
    }

    @Test
    public void testSearchByKeywordMatchesName() {
        String json = bookService.searchByKeyword("gatsby");
        assertTrue(json.contains("The Great Gatsby"));
    }

    @Test
    public void testSearchByKeywordMatchesAuthor() {
        String json = bookService.searchByKeyword("austen");
        assertTrue(json.contains("Pride and Prejudice"));
    }

    @Test
    public void testSearchByKeywordMatchesISBN() {
        String json = bookService.searchByKeyword("103");
        assertTrue(json.contains("Frankenstein"));
    }

    @Test
    public void testSearchByKeywordNoMatches() {
        String json = bookService.searchByKeyword("unknown keyword");
        assertEquals("[]", json);
    }

    @Test
    public void testGetBooks() {
        Map<String, Book> books = bookService.getBooks();
        assertEquals(3, books.size());
        assertTrue(books.containsKey("101"));
    }

    @Test
    public void testSearchByKeywordWithNullFields() {
        Book nullBook = new Book(null, null, null);
        bookService.getBooks().put("NULL", nullBook);

        String json = bookService.searchByKeyword("test");

        assertFalse(json.contains("null"));
    }
}
