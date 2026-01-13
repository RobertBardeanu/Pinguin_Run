import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AppWindow extends JFrame {

    //variablen
    JLabel charakter;
    JLabel hinderniss;
    int yPos = 250; // Startposition (Y-Achse)
    int yVelocity = 0; // Aktuelle Sprunggeschwindigkeit
    final int GRAVITY = 1;



    AppWindow() {

        //Window intialisieren
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SuperDino");
        this.setSize(1000, 500);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.white);


        // Bild laden
        ImageIcon originalIconpenguin = new ImageIcon("penguin.png");

        ImageIcon originalIcontree = new ImageIcon("tree.png");



        // Bild skalieren (z.B. auf 50x50 Pixel)
        Image scaledImagepenguin = originalIconpenguin.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon penguin = new ImageIcon(scaledImagepenguin);

        Image scaledImagetree = originalIcontree.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon tree = new ImageIcon(scaledImagetree);

        // Bild in ein Label setzen
        charakter = new JLabel(penguin);

        hinderniss = new JLabel(tree);


        charakter.setBounds(100, yPos, 200, 200);
        hinderniss.setBounds(400, yPos, 200, 200);

        // Label ins Fenster hinzufügen
        this.add(charakter);
        this.add(hinderniss);


        //timer für animation

        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePhysics();
            }
        });
        timer.start();

        //Springen mit keylistener | Leerzeichen

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && yPos >= 250) {
                    yVelocity = -20; // Kraftvoller Sprung nach oben
                }
            }
        });


        // Pinguin und alles erst ganz am Ende sichtbar machen um java anzeigefehler zu meiden
        this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
        this.setVisible(true);


    }

    //Physik Methode fürs Springen

    private void updatePhysics() {
        yVelocity += GRAVITY; // Schwerkraft wirkt ständig
        yPos += yVelocity;    // Position ändern

        // Boden-Kollision (damit er nicht aus dem Bild fällt)
        if (yPos >= 250) {      //>250, da Koordinatensystem in swing anders ist. Y verläuft nach unten. Oben links ist (0|0)
            yPos = 250;
            yVelocity = 0;
        }

        // Label neu positionieren
        charakter.setLocation(charakter.getX(), yPos);
    }


    public static void main(String[] args) {
        //windows abrufen
        AppWindow window = new AppWindow();



    }

}