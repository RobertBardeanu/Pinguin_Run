import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.lang.reflect.Method;

public class TestCollision {

    private AppWindow game;

    @BeforeEach
    void setUp() {
        // Instanz des Spiels erstellen
        game = new AppWindow();
    }

    @Test
    void testCheckCollision_TrueWhenOverlapping() throws Exception {
        // Wir holen uns die privaten Felder via Reflexion, um ihre Position zu setzen
        JLabel charakter = (JLabel) getPrivateField(game, "charakter");
        JLabel hinderniss = (JLabel) getPrivateField(game, "hinderniss");

        // Pinguin und Hindernis auf exakt die gleiche Position setzen
        charakter.setBounds(100, 300, 100, 100);
        hinderniss.setBounds(105, 305, 100, 100); // Überlappend

        // Die private Methode 'checkCollision' aufrufen
        boolean result = invokeCheckCollision(game);

        assertTrue(result, "Kollision sollte erkannt werden, wenn sich die Rects überschneiden.");
    }

    @Test
    void testCheckCollision_FalseWhenFarApart() throws Exception {
        JLabel charakter = (JLabel) getPrivateField(game, "charakter");
        JLabel hinderniss = (JLabel) getPrivateField(game, "hinderniss");

        // Weit voneinander entfernt positionieren
        charakter.setBounds(100, 300, 100, 100);
        hinderniss.setBounds(500, 300, 100, 100); // Weit rechts

        boolean result = invokeCheckCollision(game);

        assertFalse(result, "Kollision sollte nicht erkannt werden, wenn Hindernis weit weg ist.");
    }

    // --- Helper Methoden für Reflexion (da Felder/Methoden in AppWindow private sind) ---

    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        var field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private boolean invokeCheckCollision(AppWindow obj) throws Exception {
        Method method = obj.getClass().getDeclaredMethod("checkCollision");
        method.setAccessible(true);
        return (boolean) method.invoke(obj);
    }
}