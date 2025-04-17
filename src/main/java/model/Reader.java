package model;

import java.util.ArrayList;
import java.util.List;

public class Reader {
    private String id;
    private String name;
    private final List<Book> borrowedBooks = new ArrayList<>();
    private final List<String> history = new ArrayList<>();
    public Reader(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    public List<String> getHistory() {
        return history;
    }
    public boolean canBorrow() {
        return borrowedBooks.size() < 5;
    }
}