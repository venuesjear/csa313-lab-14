package model;

import java.time.LocalDate;

public class Book {
    private String isbn;
    private String author;
    private String name;
    private boolean availability;
    private LocalDate issuedDate;

    public Book(String isbn, String name, String author) {
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.availability = true;
        this.issuedDate = null;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }
}