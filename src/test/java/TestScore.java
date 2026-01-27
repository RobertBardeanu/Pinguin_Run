import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class TestScore {

    private static final String FILE_NAME = "Highscore.txt";

    @BeforeEach
    @AfterEach
    void cleanup() {
        // Lösche die Highscore-Datei vor und nach jedem Test,
        // um eine saubere Testumgebung zu haben.
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testInitialHighscoreIsZero() {
        // Wenn keine Datei existiert, sollte der Highscore 0 sein
        int score = HighscoreManager.leseHighscore();
        assertEquals(0, score, "Der initiale Highscore sollte 0 sein, wenn keine Datei existiert.");
    }

    @Test
    void testSchreibeUndLeseHighscore() {
        int neuerScore = 150;
        HighscoreManager.schreibeHighscore(neuerScore);

        int gelesenerScore = HighscoreManager.leseHighscore();
        assertEquals(neuerScore, gelesenerScore, "Der gelesene Score sollte dem geschriebenen Score entsprechen.");
    }

    @Test
    void testOverwriteHighscore() {
        HighscoreManager.schreibeHighscore(100);
        HighscoreManager.schreibeHighscore(250); // Überschreiben

        int gelesenerScore = HighscoreManager.leseHighscore();
        assertEquals(250, gelesenerScore, "Der Highscore sollte mit dem neuesten Wert überschrieben werden.");
    }

    @Test
    void testInvalidFileContent() throws Exception {
        // Simuliere eine korrupte Datei mit Text statt Zahlen
        java.nio.file.Files.writeString(new File(FILE_NAME).toPath(), "KeineZahl");

        int score = HighscoreManager.leseHighscore();
        assertEquals(0, score, "Bei ungültigem Dateiinhalt sollte 0 zurückgegeben werden.");
    }
}