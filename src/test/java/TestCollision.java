import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.lang.reflect.Method;

public class TestCollision {

    private AppWindow game;

    @BeforeEach
    void setUp() {
        // Instanz des Spiels erstellen vor test
        game = new AppWindow();
    }

    @Test
    void testCheckCollision_TrueWhenOverlapping() throws Exception {
        // Labels via "Reflexionen"
        JLabel charakter = (JLabel) getPrivateField(game, "charakter");
        JLabel hinderniss = (JLabel) getPrivateField(game, "hinderniss");

        //überlappen, also eig true
        charakter.setBounds(100, 300, 100, 100);
        hinderniss.setBounds(105, 305, 100, 100); // Überlappend

        // Die private Methode 'checkCollision' aufrufen über invoke
        boolean result = invokeCheckCollision(game);

        assertTrue(result, "Kollision sollte erkannt werden, wenn sich die Rects überschneiden.");
    }

    @Test
    void testCheckCollision_FalseWhenFarApart() throws Exception {
        JLabel charakter = (JLabel) getPrivateField(game, "charakter");
        JLabel hinderniss = (JLabel) getPrivateField(game, "hinderniss");

        // auseinander
        charakter.setBounds(100, 300, 100, 100);
        hinderniss.setBounds(500, 300, 100, 100); // Weit rechts

        boolean result = invokeCheckCollision(game);

        assertFalse(result, "Kollision sollte nicht erkannt werden, wenn Hindernis weit weg ist.");
    }

    // Helper Methoden für Reflexion (da Felder/Methoden in AppWindow private sind)
    // über invoke kann man drauf zu greifen, lest javadoc, kp, voll wild

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