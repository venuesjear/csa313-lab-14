import factory.BookFactory;
import factory.ReaderFactory;
import model.Book;
import model.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookService;
import service.BorrowService;
import service.ReaderService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowServiceTest {

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
        bookService.addBook(bookFactory.create("104", "War and Peace", "Leo Tolstoy"));
        bookService.addBook(bookFactory.create("105", "1984", "George Orwell"));
        bookService.addBook(bookFactory.create("106", "The Catcher in the Rye", "J.D.Salinger"));

        readerService.registerReader(readerFactory.create("11", "Sugar", null));
        readerService.registerReader(readerFactory.create("12", "Beomgyu", null));
    }

    @Test
    public void testBorrowBookSuccessfully() {
        String result = borrowService.borrowBook("11", "101");
        assertEquals("Book borrowed successfully", result);
        assertFalse(bookService.getBooks().get("101").isAvailable());

        Reader reader = readerService.getReaders().get("11");
        assertEquals(1, reader.getBorrowedBooks().size());
        assertFalse(reader.getHistory().contains("Borrowed: Pride and Prejudice"));
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
    public void testBorrowUnavailableBook() {
        borrowService.borrowBook("11", "101");
        String result = borrowService.borrowBook("12", "101");
        assertEquals("Book is already borrowed", result);
    }

    @Test
    public void testBorrowBookReaderCannotBorrowMoreThan5() {
        Reader reader = readerService.getReaders().get("11");

        for (int i = 1; i <= 5; i++) {
            Book book = bookFactory.create("20" + i, "Book " + i, "Author " + i);
            book.setAvailability(false);
            reader.getBorrowedBooks().add(book);
        }

        String result = borrowService.borrowBook("11", "101");
        assertEquals("Reader cannot borrow more than 5 books", result);
    }

    @Test
    public void testReturnBookSuccessfully() {
        borrowService.borrowBook("11", "101");
        String result = borrowService.returnBook("11", "101");
        assertEquals("Book returned successfully", result);
        assertTrue(bookService.getBooks().get("101").isAvailable());

        Reader reader = readerService.getReaders().get("11");
        assertEquals(0, reader.getBorrowedBooks().size());
        assertFalse(reader.getHistory().contains("Returned: Pride and Prejudice"));
    }

    @Test
    public void testReturnBookNotBorrowed() {
        String result = borrowService.returnBook("11", "101");
        assertEquals("Reader did not borrow this book", result);
    }

    @Test
    public void testReturnBookNonExistentReader() {
        String result = borrowService.returnBook("99", "101");
        assertEquals("Reader not found", result);
    }

    @Test
    public void testReturnBookNonExistentBook() {
        String result = borrowService.returnBook("11", "999");
        assertEquals("Book not found", result);
    }

    @Test
    public void testReturnBookOverdue() {
        borrowService.borrowBook("11", "101");

        Book book = bookService.getBooks().get("101");
        book.setIssuedDate(LocalDate.now().minusDays(10));

        String result = borrowService.returnBook("11", "101");
        assertEquals("Returned, but overdue!", result);
        assertTrue(bookService.getBooks().get("101").isAvailable());

        Reader reader = readerService.getReaders().get("11");
        assertFalse(reader.getHistory().contains("Returned late: Pride and Prejudice"));
    }

    @Test
    public void testReturnBookExactlyOnDueDate() {
        borrowService.borrowBook("11", "101");

        Book book = bookService.getBooks().get("101");
        book.setIssuedDate(LocalDate.now().minusDays(7));

        String result = borrowService.returnBook("11", "101");
        assertEquals("Book returned successfully", result);
        assertTrue(bookService.getBooks().get("101").isAvailable());
    }

    @Test
    public void testReturnBookWithNullIssuedDate() {
        borrowService.borrowBook("11", "101");

        Book book = bookService.getBooks().get("101");
        book.setIssuedDate(null);

        String result = borrowService.returnBook("11", "101");
        assertEquals("Book returned successfully", result);
        assertTrue(bookService.getBooks().get("101").isAvailable());
    }

    @Test
    public void testGetHistorySuccessfully() {
        borrowService.borrowBook("11", "101");
        borrowService.borrowBook("11", "102");
        borrowService.returnBook("11", "101");

        String history = borrowService.getHistory("11");
        assertTrue(history.contains("Borrowed: Pride and Prejudice"));
        assertTrue(history.contains("Borrowed: The Great Gatsby"));
        assertTrue(history.contains("Returned: Pride and Prejudice"));
    }

    @Test
    public void testGetHistoryNonExistentReader() {
        String result = borrowService.getHistory("99");
        assertEquals("Reader not found", result);
    }

    @Test
    public void testGetHistoryEmpty() {
        String history = borrowService.getHistory("11");
        assertEquals("", history);
    }

    @Test
    public void testGetHistoryWithMultipleEntries() {
        borrowService.borrowBook("11", "101");
        borrowService.returnBook("11", "101");
        borrowService.borrowBook("11", "102");
        borrowService.borrowBook("11", "103");
        borrowService.returnBook("11", "103");

        String history = borrowService.getHistory("11");
        String[] lines = history.split("\n");
        assertEquals(5, lines.length);
    }

    @Test
    public void testBorrowBookAlreadyBorrowedBySameReader() {
        borrowService.borrowBook("11", "101");

        String result = borrowService.borrowBook("11", "101");
        assertEquals("Book is already borrowed", result);
    }

    @Test
    public void testReturnBookReaderDidNotBorrow() {
        borrowService.borrowBook("11", "101");

        String result = borrowService.returnBook("12", "101");
        assertEquals("Reader did not borrow this book", result);
    }

    @Test
    public void testBorrowAndReturnMultipleBooks() {
        assertEquals("Book borrowed successfully", borrowService.borrowBook("11", "101"));
        assertEquals("Book borrowed successfully", borrowService.borrowBook("11", "102"));
        assertEquals("Book borrowed successfully", borrowService.borrowBook("11", "103"));

        Reader reader = readerService.getReaders().get("11");
        assertEquals(3, reader.getBorrowedBooks().size());

        assertEquals("Book returned successfully", borrowService.returnBook("11", "101"));
        assertEquals(2, reader.getBorrowedBooks().size());

        assertTrue(bookService.getBooks().get("101").isAvailable());
        assertFalse(bookService.getBooks().get("102").isAvailable());
        assertFalse(bookService.getBooks().get("103").isAvailable());
    }

    @Test
    public void testCanBorrowAfterReturning() {
        borrowService.borrowBook("11", "101");
        borrowService.returnBook("11", "101");

        String result = borrowService.borrowBook("11", "101");
        assertEquals("Book borrowed successfully", result);
        assertFalse(bookService.getBooks().get("101").isAvailable());
    }
}