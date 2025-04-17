import com.google.gson.Gson;
import dto.BookRequest;
import dto.ReaderRequest;
import factory.BookFactory;
import factory.ReaderFactory;
import model.Book;
import model.Librarian;
import model.Reader;
import observer.BookNotifyService;
import observer.ReaderNotifier;
import service.BookService;
import service.BorrowService;
import service.ReaderService;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        BookService bookService = new BookService();
        ReaderService readerService = new ReaderService();
        BorrowService borrowService = new BorrowService(readerService.getReaders(), bookService.getBooks());
        BookNotifyService notifyService = new BookNotifyService();
        Gson gson = new Gson();
        Librarian librarian = Librarian.getInstance();
        librarian.register("Admin", "admin123");

        post("/book", (req, res) -> {
            try {
                BookRequest data = gson.fromJson(req.body(), BookRequest.class);
                if (data.isbn == null || data.title == null || data.author == null) {
                    res.status(400);
                    return "Missing required fields.";
                }
                BookFactory bookFactory = new BookFactory();
                Book book = bookFactory.createFromRequest(data);
                bookService.addBook(book);
                res.status(201);
                return "Book added";
            } catch (IllegalArgumentException e) {
                res.status(400);
                return e.getMessage();
            } catch (Exception e) {
                res.status(500);
                return "An error occurred: " + e.getMessage();
            }
        });
        get("/books", (req, res) -> {
            res.type("application/json");
            return bookService.getAllBooksJson();
        });
        get("/books/available", (req, res) -> {
            res.type("application/json");
            return bookService.getAvailableBooksJson();
        });


        post("/reader", (req, res) -> {
            try {
                ReaderRequest data = gson.fromJson(req.body(), ReaderRequest.class);
                if (data.id == null || data.name == null) {
                    res.status(400);
                    return "Missing required fields.";
                }
                ReaderFactory readerFactory = new ReaderFactory();
                Reader reader = readerFactory.createFromRequest(data);
                readerService.registerReader(reader);
                res.status(201);
                return "Reader registered";
            } catch (IllegalArgumentException e) {
                res.status(400);
                return e.getMessage();
            } catch (Exception e) {
                res.status(500);
                return "An error occurred: " + e.getMessage();
            }
        });
        get("/readers", (req, res) -> {
            res.type("application/json");
            return readerService.getAllReadersJson();
        });
        get("/books/search", (req, res) -> {
            res.type("application/json");

            String rawQuery = req.queryString();

            if (rawQuery == null || rawQuery.isBlank()) {
                return bookService.getAllBooksJson();
            }

            String searchTerm = java.net.URLDecoder.decode(rawQuery, java.nio.charset.StandardCharsets.UTF_8);

            return bookService.searchByKeyword(searchTerm);
        });
        post("/borrow", (req, res) -> {
            String readerId = req.queryParams("readerId");
            String isbn = req.queryParams("isbn");

            if (readerId == null || isbn == null) {
                res.status(400);
                return "Missing readerId or isbn";
            }

            String result = borrowService.borrowBook(readerId, isbn);
            if (result.equals("Book borrowed successfully")) res.status(200);
            else res.status(400);
            return result;
        });

        post("/return", (req, res) -> {
            String readerId = req.queryParams("readerId");
            String isbn = req.queryParams("isbn");

            if (readerId == null || isbn == null) {
                res.status(400);
                return "Missing readerId or isbn";
            }

            String result = borrowService.returnBook(readerId, isbn);
            if (result.equals("Book returned successfully")) {
                res.status(200);
                if (notifyService.hasWaiters(isbn)) {
                    notifyService.notifyNext(isbn);
                }
            } else res.status(400);
            return result;
        });

        get("/reader/history", (req, res) -> {
            String readerId = req.queryParams("readerId");

            if (readerId == null) {
                res.status(400);
                return "Missing readerId";
            }

            res.type("text/plain");
            return borrowService.getHistory(readerId);
        });

        post("/wait", (req, res) -> {
            String readerId = req.queryParams("readerId");
            String isbn = req.queryParams("isbn");
            String host = req.queryParams("host");
            int port = Integer.parseInt(req.queryParams("port"));

            ReaderNotifier notifier = new ReaderNotifier(readerId, host, port);
            notifyService.addWaitRequest(isbn, notifier);

            return "You are added to wait list of [" + isbn + "].";
        });
    }
}