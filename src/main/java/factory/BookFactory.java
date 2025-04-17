package factory;

import dto.BookRequest;
import model.Book;

public class BookFactory extends BaseFactory<Book> {
    @Override
    public Book create(String isbn, String title, String author) {
        return new Book(isbn, title, author);
    }

    public Book createFromRequest(BookRequest request) {
        return create(request.isbn, request.title, request.author);
    }
}