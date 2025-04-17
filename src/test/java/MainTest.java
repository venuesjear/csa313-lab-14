import factory.BookFactory;
import factory.ReaderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookService;
import service.BorrowService;
import service.ReaderService;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private BookService bookService;
    private ReaderService readerService;
    private BorrowService borrowService;
    private BookFactory bookFactory;
    private ReaderFactory readerFactory;

    @BeforeEach
    public void setup() {
        bookService = new BookService();
        readerService = new ReaderService();
        borrowService = new BorrowService(readerService.getReaders(), bookService.getBooks());
        bookFactory = new BookFactory();
        readerFactory = new ReaderFactory();

        bookService.addBook(bookFactory.create("101", "Pride and Prejudice", "Jane Austen"));
        bookService.addBook(bookFactory.create("102", "The Great Gatsby", "F.Scott Fitzgerald"));
        bookService.addBook(bookFactory.create("103", "Frankenstein", "Mary Shelley"));
        bookService.addBook(bookFactory.create("104", "War and Peace", "Leo Tolstoy"));  // Fixed author/title order
        bookService.addBook(bookFactory.create("105", "1984", "George Orwell"));      // Fixed author/title order
        bookService.addBook(bookFactory.create("106", "The Catcher in the Rye", "J.D.Salinger")); // Fixed author/title order

        readerService.registerReader(readerFactory.create("11", "Sugar", null));
        readerService.registerReader(readerFactory.create("12", "Beomgyu", null));
    }

    @Test
    public void testAddBookSuccessfully() {
        assertEquals(6, bookService.getBooks().size());
    }

    @Test
    public void testRegisterReaderSuccessfully() {
        assertEquals(2, readerService.getReaders().size());
    }

    @Test
    public void testBorrowBookSuccessfully() {
        String result = borrowService.borrowBook("11", "101");
        assertEquals("Book borrowed successfully", result);
        assertFalse(bookService.getBooks().get("101").isAvailable());
    }

    @Test
    public void testBorrowUnavailableBook() {
        borrowService.borrowBook("11", "101");
        String result = borrowService.borrowBook("12", "101");
        assertEquals("Book is already borrowed", result);
    }

    @Test
    public void testBorrowBookNonExistentReader() {
        String result = borrowService.borrowBook("99", "101");
        assertEquals("Reader not found", result);
    }

    @Test
    public void testBorrowBookNonExistentBook() {
        String result = borrowService.borrowBook("11", "999");
        assertEquals("Book not found", result);
    }

    @Test
    public void testReturnBookSuccessfully() {
        borrowService.borrowBook("11", "101");
        String result = borrowService.returnBook("11", "101");
        assertEquals("Book returned successfully", result);
        assertTrue(bookService.getBooks().get("101").isAvailable());
    }

    @Test
    public void testReturnBookNotBorrowed() {
        String result = borrowService.returnBook("11", "101");
        assertEquals("Reader did not borrow this book", result);
    }

    @Test
    public void testSearchBooks() {
        String result = bookService.searchByKeyword("gatsby");
        assertTrue(result.contains("The Great Gatsby"));
    }

    @Test
    public void testReaderHistory() {
        borrowService.borrowBook("11", "101");
        String history = borrowService.getHistory("11");
        assertTrue(history.contains("Borrowed: Pride and Prejudice"));
    }
}