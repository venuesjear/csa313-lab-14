package service;

import model.Book;
import model.Reader;

import java.time.LocalDate;
import java.util.Map;

public class BorrowService {
    private final Map<String, Reader> readers;
    private final Map<String, Book> books;

    public BorrowService(Map<String, Reader> readers, Map<String, Book> books) {
        this.readers =
                readers;
        this.books = books;
    }

    public String borrowBook(String readerId, String isbn) {
        Reader reader = readers.get(readerId);
        if (reader == null) return "Reader not found";

        Book book = books.get(isbn);
        if (book == null) return "Book not found";

        if (!book.isAvailable()) return "Book is already borrowed";

        if (!reader.canBorrow()) return "Reader cannot borrow more than 5 books";

        book.setAvailability(false);
        book.setIssuedDate(LocalDate.now());
        reader.getBorrowedBooks().add(book);
        reader.getHistory().add("Borrowed: " + book.getName() + " on " + LocalDate.now());
        return "Book borrowed successfully";
    }

    public String returnBook(String readerId, String isbn) {
        Reader reader = readers.get(readerId);
        if (reader == null) return "Reader not found";

        Book book = books.get(isbn);
        if (book == null) return "Book not found";

        if (!reader.getBorrowedBooks().contains(book)) return "Reader did not borrow this book";

        book.setAvailability(true);
        reader.getBorrowedBooks().remove(book);

        if (book.getIssuedDate() != null) {
            LocalDate dueDate = book.getIssuedDate().plusDays(7);
            if (LocalDate.now().isAfter(dueDate)) {
                reader.getHistory().add("Returned late: " + book.getName() + " on " + LocalDate.now());
                return "Returned, but overdue!";
            } else {
                reader.getHistory().add("Returned: " + book.getName() + " on " + LocalDate.now());
            }
        }
        return "Book returned successfully";
    }

    public String getHistory(String readerId) {
        Reader reader = readers.get(readerId);
        if (reader == null) return "Reader not found";

        return String.join("\n", reader.getHistory());
    }
}
