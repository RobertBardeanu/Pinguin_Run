import java.io.*;
/**
 * HIGHSCORE Verwalter
 * @author Robert, Maik, Tobias
 * @version 1.0
 */

public class HighscoreManager {
    private static final String FILE_NAME = "Highscore.txt";

    /**
     * Liest den aktuellen Highscore aus der lokalen Textdatei aus.
     * * @return Der gespeicherte Highscore als Ganzzahl; 0, falls die Datei nicht existiert oder leer ist.
     * @throws Exception Wenn beim Dateizugriff ein schwerwiegender Fehler auftritt.
     */
    public static int leseHighscore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine();
            return (line != null) ? Integer.parseInt(line) : 0;
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Speichert den neuen Highscore in der lokalen Textdatei.
     * @param score Der zu speichernde Punktwert.
     */

    public static void schreibeHighscore(int score) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}