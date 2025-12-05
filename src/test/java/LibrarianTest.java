import model.Librarian;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarianTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instanceField = Librarian.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    void testGetInstance_FirstTimeCreatesInstance() {
        Librarian l1 = Librarian.getInstance();
        assertNotNull(l1);
    }

    @Test
    void testGetInstance_ReturnsSameInstance() {
        Librarian l1 = Librarian.getInstance();
        Librarian l2 = Librarian.getInstance();
        assertSame(l1, l2);
    }

    @Test
    void testRegister_FirstTimeSuccess() {
        Librarian librarian = Librarian.getInstance();
        assertDoesNotThrow(() -> librarian.register("Admin", "1234"));
    }

    @Test
    void testRegister_ThrowsIfAlreadyRegistered() {
        Librarian librarian = Librarian.getInstance();
        librarian.register("Admin", "1234");

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> librarian.register("Another", "9999")
        );

        assertEquals("Already registered", ex.getMessage());
    }
}