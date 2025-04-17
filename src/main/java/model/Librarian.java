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
        if (this.name != null) throw new IllegalStateException("Already registered");
        this.name = name;
        this.password = password;
    }
}
