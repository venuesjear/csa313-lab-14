package model;

public class Librarian {
    private static Librarian instance;
    private String name;
    private String password;

    private Librarian() {}

    public static Librarian getInstance() {
        if (instance == null) instance = new Librarian();
        return instance;
    }

    public void register(String name, String password) {
        if (this.name != null) {
            throw new IllegalStateException("Already registered.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.name = name;
        this.password = password;
    }
}
