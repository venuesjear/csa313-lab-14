package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BookService {
    private final Map<String, Book> books = new HashMap<>();
    private final Gson gson;

    public BookService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    public void addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Book with this ISBN already exists.");
        }
        books.put(book.getIsbn(), book);
    }

    public String getAllBooksJson() {
        return gson.toJson(books.values());
    }

    public String getAvailableBooksJson() {
        return gson.toJson(
                books.values().stream()
                        .filter(Book::isAvailable)
                        .toList()
        );
    }

    public String searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return gson.toJson(
                books.values().stream()
                        .filter(book ->
                                (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(lowerKeyword)) ||
                                        (book.getName() != null && book.getName().toLowerCase().contains(lowerKeyword)) ||
                                        (book.getIsbn() != null && book.getIsbn().toLowerCase().contains(lowerKeyword))
                        )
                        .toList()
        );
    }

    public Map<String, Book> getBooks() {
        return books;
    }
}
