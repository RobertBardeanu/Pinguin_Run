import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Das Hauptfenster des Spiels "Pinguin Run".
 * <p>
 * Diese Klasse verwaltet das GUI, die Spielphysik sowie die
 * Kollisionsabfrage und den Punktestand.
 * </p>
 * * @author Robert, Maik, Tobias
 * @version 1.0
 */

public class AppWindow extends JFrame {





    //Sprites: Tobias //vllt echt auch mit nem extra Thread
    //Objekte: Robert
    //Kollision; Maik
    //Punkte: Maik
    //Menü:
    //Hintergrund: Maik pixeln
    //Highscore:
    //Beleidigungen wenn man verkackt;
    //Sounds:
    //Tests:
    //Javadoc:

    //Optional
    //Powerups:
    //Methoden in extra Klasse, Code aufräumen


    /** Aktueller Punktestand des Spielers. */
    private int punkte = 0;


    private JLabel score;
    private JLabel charakter,hinderniss,robbenHinderniss,baumHinderniss,netzHinderniss;
    private JLabel hintergrund,hintergrund2,hintergrund3;
    final private JLabel groundlabel,groundlabel2,groundlabel3;
    private JButton resetbutton;
    private  JButton startbutton;
    GameLoop loop = new GameLoop(this);




    final private ImageIcon penguinOnGround,penguinJump;

    /** Y-Koordinate des Bodens, auf dem der Pinguin läuft */
    private final int GROUND_Y = 300;
    /** X-Koordinate Hintergrund*/
    private int BACKGROUND_X = 0;
    /** X-Koordinate Boden*/
    private int GROUND_X = 0;
    /** Startposition auf der y-Achse */
    private int yPos = GROUND_Y;
    /** Aktuelle vertikale Geschwindigkeit des Charakters */
    private int yVelocity = 0;
    /** Stärke der Abwärtsbeschleunigung */
    private final int GRAVITY = 80;
    /** Die initiale Kraft, die beim Springen nach oben wirkt */
    private final int JUMP_FORCE = -15000;
    /** Aktuelle horizontale Position des Hindernisses */
    private int obstacleX = 1000;
    /** Geschwindigkeit, mit der sich Hindernisse auf den Spieler zubewegen */
    private int obstacleSpeed = 500;
    final private Random random = new Random();
    /** Statusflag, ob das Spiel beendet ist */
    private boolean GameOver = false, GameStart = false;
    /** Manager für die Audiowiedergabe (Musik und Soundeffekte). */
    Sound musikPlayer = new Sound();


    private Jump_States currentJumpState = Jump_States.ON_GROUND;

    /**
     * Erstellt ein neues Spiel-Fenster und initialisiert alle Spielkomponenten.
     * * Der Konstruktor führt folgende Schritte aus:
     * 1. Konfiguration des JFrame (Größe, Titel, Schließverhalten).
     * 2. Start der Hintergrundmusik über den musikPlayer.
     * 3. Laden und Skalieren aller grafischen Ressourcen (Pinguin, Hindernisse, Hintergründe).
     * 4. Instanziierung der UI-Elemente und des Reset-Buttons.
     * 5. Registrierung des KeyListeners für die Sprung-Steuerung.
     * 6. Start des Game-Loops.
     */

    AppWindow()  {

        // Fenster-Konfiguration
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pinguin Run");
        this.setSize(1000, 500);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.white);
        this.setResizable(false);



        //Musik initialisieren


        // Ressourcen-Management
        // Bilder laden und für die Darstellung skalieren
        ImageIcon originalPenguinOnGround = new ImageIcon("src/Media/Bilder/penguinOnGround.png");
        ImageIcon originalPenguinJump = new ImageIcon("src/Media/Bilder/penguinJump.png");

        ImageIcon originalIcontree = new ImageIcon("src/Media/Bilder/tree.png");
        ImageIcon Hintergrund = new ImageIcon("src/Media/Bilder/background.png"); //Hintergrund von craftpix.net
        ImageIcon originalGround = new ImageIcon("src/Media/Bilder/ground.png");
        ImageIcon originalrobbe = new ImageIcon("src/Media/Bilder/robbe.png");
        ImageIcon originalnetz = new ImageIcon("src/Media/Bilder/fischernetz.png");

        this.setIconImage(originalPenguinOnGround.getImage());


        // Bild skalieren

        Image scaledImagePenguinOnGround = originalPenguinOnGround.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        penguinOnGround = new ImageIcon(scaledImagePenguinOnGround);

        Image scaledImagePenguinJump = originalPenguinJump.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        penguinJump = new ImageIcon(scaledImagePenguinJump);

        Image scaledImagetree = originalIcontree.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon tree = new ImageIcon(scaledImagetree);

        Image scaledImageHintergrund = Hintergrund.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon hintergrundimage = new ImageIcon(scaledImageHintergrund);

        Image scaledImageGround = originalGround.getImage().getScaledInstance(1000, -200, Image.SCALE_SMOOTH);
        ImageIcon ground=new ImageIcon(scaledImageGround);

        Image scaledImageRobbe = originalrobbe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon Robbe=new ImageIcon(scaledImageRobbe);

        Image scaledImageNetz = originalnetz.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon Netz=new ImageIcon(scaledImageNetz);

        // UI-Komponenten
        // Initialisierung von Labels für Score, Charakter und Hindernisse
        charakter = new JLabel(penguinOnGround);

        hinderniss = new JLabel();
        robbenHinderniss =new JLabel(Robbe);
        baumHinderniss =new JLabel(tree);
        netzHinderniss =new JLabel(Netz);

        hintergrund=new JLabel(hintergrundimage);
        hintergrund2=new JLabel(hintergrundimage);
        hintergrund3=new JLabel(hintergrundimage);

        groundlabel=new JLabel(ground);
        groundlabel2=new JLabel(ground);
        groundlabel3=new JLabel(ground);

        score = new JLabel("");


        charakter.setBounds(100, yPos, 100, 100);
        hinderniss.setBounds(obstacleX, yPos, 100, 100);
        robbenHinderniss.setBounds(obstacleX +10, yPos, 200, 200);
        hintergrund.setBounds(0, 0, getWidth(),getHeight());
        hintergrund3.setBounds(0, 0, getWidth(),getHeight());
        hintergrund2.setBounds(0, 0, getWidth(),getHeight());

        groundlabel.setBounds(0, -20, getWidth(),getHeight());
        groundlabel2.setBounds(0, -20, getWidth(),getHeight());
        groundlabel3.setBounds(0, -20, getWidth(),getHeight());



        //Resetbutton und ActionListener zum zurücksetzen
        resetbutton=new JButton("Reset");
        startbutton=new JButton("Start");

        resetbutton.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score.setForeground(Color.decode("#F5EADD"));

                punkte = 0;
                obstacleX = 1000;
                obstacleSpeed = 500;
                yPos = GROUND_Y;
                yVelocity = 0;
                GameOver = false;

                // 2. UI-Elemente zurücksetzen
                score.setText("Score: 0");
                charakter.setLocation(100, GROUND_Y);
                hinderniss.setLocation(obstacleX, GROUND_Y);
                resetbutton.setVisible(false);

                musikPlayer.resetMusik();
                musikPlayer.starteMusik();

                // Fokus zurück auf das Fenster (wichtig für KeyListener!)
                AppWindow.this.requestFocusInWindow();

            }
        });
        startbutton.addActionListener(new  ActionListener()   {
            @Override
            public void actionPerformed(ActionEvent e) {
                musikPlayer.starteMusik();
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException exception) {
                    System.err.println(exception );
                }
                StarteGame();
                AppWindow.this.requestFocusInWindow();


            }
        });

        resetbutton.setBounds(450, 200, 120, 50);
        resetbutton.setFont(new Font("SansSerif", Font.BOLD, 18));
        resetbutton.setBackground(new Color(46, 204, 113));
        resetbutton.setForeground(Color.WHITE);
        resetbutton.setFocusPainted(false);
        resetbutton.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        resetbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetbutton.setVisible(false);

        startbutton.setBounds(450, 200, 120, 50);
        startbutton.setFont(new Font("SansSerif", Font.BOLD, 18));
        startbutton.setBackground(new Color(46, 204, 113));
        startbutton.setForeground(Color.WHITE);
        startbutton.setFocusPainted(false);
        startbutton.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        startbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startbutton.setVisible(true);

        //ScoreSchrift
        score.setFont(new Font(Font.DIALOG,Font.BOLD,20));
        score.setForeground(Color.decode("#F5EADD"));


        // Label ins Fenster hinzufügen
        //interaktiv
        this.add(resetbutton);
        this.add(startbutton);
        this.add(score);

        //Spielelemente
        this.add(charakter);
        this.add(hinderniss);
        this.add(robbenHinderniss);

        //Boden
        this.add(groundlabel);
        this.add(groundlabel2);
        this.add(groundlabel3);

        //Hintergrund
        this.add(hintergrund);
        this.add(hintergrund2);
        this.add(hintergrund3);

        // Erstes Hindernis festlegen
        hinderniss.setIcon(getRandomObstacleIcon());


        // Steuerung
        // KeyListener für die Leertaste/Pfeiltaste oben zum Springen
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && yPos >= GROUND_Y) {
                    if(!GameOver) {
                        musikPlayer.jump();
                    }
                    yVelocity = (int)-Math.round(Math.sqrt(JUMP_FORCE * -2.0 * GRAVITY)); // Kraftvoller Sprung nach oben
                    System.out.println("yVel: " + yVelocity);

                }
            }
        });


        // Spielstart
        this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
        this.setVisible(true);



    }





    /**
     * Beendet das laufende Spiel, stoppt die Musik und löst den Game-Over-Sound aus.
     * Zeigt den Reset-Button an und prüft, ob der aktuelle Punktestand den Highscore bricht.
     */
    private  void StarteGame(){
        startbutton.setVisible(false);

        loop.start();
    }
    private void gameOver(){
        charakter.setIcon(penguinJump);
        resetbutton.setVisible(true);
        musikPlayer.deadsound();

        GameOver = true;
        System.out.println("Kollision! Spiel vorbei. Score: " + punkte);
        musikPlayer.stoppeMusik();

        try{
            //funktionirt noch nicht richtig
            int gespeicherterScore = HighscoreManager.leseHighscore();
            if (punkte > gespeicherterScore) {
                HighscoreManager.schreibeHighscore(punkte);
                System.out.println("Neuer Highscore!");
            } else {
                System.out.println("Kein neuer Highscore.");
            }
        }
        catch(Exception e){}
        score.setText("<html> Game Over :( <br> Score: " + punkte + "<br>" +
                "Highscore: "
                        + HighscoreManager.leseHighscore() +
                        "</html>"
        );        Dimension d= score.getPreferredSize();
        score.setForeground(new Color(255, 255, 255));
        score.setBounds(420 ,10, d.width,d.height);


    }

    /**
     * Aktualisiert die horizontale Position des Hindernisses.
     * Setzt das Hindernis zurück auf die Startposition, sobald es den linken Bildschirmrand
     * verlässt, und löst die Punktvergabe aus.
     * @param deltaTime Zeitdifferenz seit dem letzten Update zur Berechnung der Framerate-unabhängigen Bewegung.
     */
    private void updateObstacle(final double deltaTime) {
        final int treeVel = (int)Math.round(obstacleSpeed * deltaTime);
        obstacleX -= treeVel <= 0 ? 1 : treeVel; // Hindernis bewegt sich nach links

        hinderniss.setLocation( obstacleX, GROUND_Y);

        if (obstacleX < -200) {
            obstacleX = getWidth();
            hinderniss.setIcon(getRandomObstacleIcon());
            score();
        }


    }

    /**
     * Berechnet die Bewegung des Hintergrunds, um einen Parallax-Effekt zu erzeugen.
     * @param deltaTime Zeitdifferenz seit dem letzten Update.
     */

    private void updateBackground(final double deltaTime) {
        final int speed = (int)Math.round(100 * deltaTime);
        BACKGROUND_X -= speed <= 0 ? 1 : speed; // Hindernis bewegt sich nach links

        hintergrund.setLocation( BACKGROUND_X, 0);
        hintergrund2.setLocation( BACKGROUND_X +getWidth(),0);
        hintergrund3.setLocation( BACKGROUND_X -getWidth(),0);

        if (BACKGROUND_X < -getWidth()) {
            BACKGROUND_X = getWidth();
        }
    }

    /**
     * Bewegt die Bodentextur synchron zur Spielgeschwindigkeit, um eine realistische Laufanimation zu erzeugen
     * @param deltaTime Zeitdifferenz seit dem letzten Update.
     */

    private void updateGround(final double deltaTime) {
        final int speed = (int)Math.round(obstacleSpeed * deltaTime);
        GROUND_X -= speed <= 0 ? 1 : speed; // Hindernis bewegt sich nach links

        groundlabel.setLocation( GROUND_X ,-20);
        groundlabel2.setLocation( GROUND_X +getWidth(),-20);
        groundlabel3.setLocation( GROUND_X -getWidth(),-20);

        if (GROUND_X < -getWidth()) {
            GROUND_X = getWidth();
        }
    }

    /**
     * Wählt zufällig eines der verfügbaren Hindernis-Icons (Robbe, Baum oder Netz) aus.
     * @return Eines der ImageIcon für das nächste Hindernis.
     */

    private Icon getRandomObstacleIcon() {
        return switch (random.nextInt(1, 4)) {
            case 1 -> robbenHinderniss.getIcon();
            case 2 -> baumHinderniss.getIcon();
            case 3 -> netzHinderniss.getIcon();
            default -> null;
        };
    }

    /**
     * Erhöht den Punktestand und steigert dabei jedes mal die Spielgeschwindigkeit.
     * Ab einer Geschwindigkeit von 1000 wird ein Bonus-Score vergeben.
     */

    private void score(){
        int maxSpeed = 2000     ;
        if(punkte>=10&& obstacleSpeed < maxSpeed)
        {
            obstacleSpeed = obstacleSpeed +10;
            System.out.println("treeSpeed: " + obstacleSpeed);
        }
        if(obstacleSpeed >=1000){
            punkte+=random.nextInt(10,100);

        }
        else{
            punkte+=random.nextInt(1,10);
        }


    }

    /**
     * Prüft auf eine Überschneidung der Hitboxen von Charakter und Hindernis.
     * Die Hitboxen werden leicht verkleinert ("grow"), um eine fairere Kollisionsabfrage zu ermöglichen.
     * @return true, wenn eine Kollision vorliegt, andernfalls false.
     */

    private boolean checkCollision(){
        Rectangle pinguinRect = charakter.getBounds();
        Rectangle hindernisRect = hinderniss.getBounds();

        //hitboxen verkleinern geht iwie mit grow
        // -h, -v zieht an jeder seite soviele pixel ab quasi

        pinguinRect.grow(-15, -10);
        hindernisRect.grow(-15, -10);
        return pinguinRect.intersects(hindernisRect);
    }


    /**
     * Berechnet die vertikale Bewegung des Charakters.
     * Bemerkt zudem ob der Charakter auf dem Boden oder im Sprung ist um eine Animation zu ermöglichen
     * @param deltaTime Zeitdifferenz seit dem letzten Update.
     */

    private void updatePhysics(final double deltaTime) {

        //Sprung
        yVelocity += GRAVITY; // Schwerkraft wirkt ständig
        yPos += (int)Math.round(yVelocity * deltaTime);    // Position ändern

        // Boden-Kollision (damit er nicht aus dem Bild fällt)
        if (yPos >= GROUND_Y) {      //>250, da Koordinatensystem in swing anders ist. Y verläuft nach unten. Oben links ist (0|0)
            yPos = GROUND_Y;
            yVelocity = 0;
        }

        Jump_States newState;

        if (yPos >= GROUND_Y) {
            newState = Jump_States.ON_GROUND;
        }
        else {
            newState = Jump_States.JUMP;



        }

        if (newState != currentJumpState) {
            currentJumpState = newState;
            updateSprite();
        }

        // Label neu positionieren
        charakter.setLocation(charakter.getX(), yPos);

    }


    /**
     * <p>   Wechselt das Icon des Charakters basierend auf dem aktuellen Jump_State</p>
     */
    private void updateSprite() {
        switch (currentJumpState) {
            case ON_GROUND -> charakter.setIcon(penguinOnGround);
            case JUMP -> charakter.setIcon(penguinJump);
        }
    }


    /**
     * Haupt-Update-Methode, die alle Teilbereiche (Physik, Hindernisse, Boden, Hintergrund)
     * aktualisiert, sofern das Spiel nicht pausiert oder beendet ist.
     * @param deltaTime Zeitdifferenz seit dem letzten Update.
     */

    public void updateAll(final double deltaTime)
    {

        if (GameOver) {
            return;
        }


        updatePhysics(deltaTime);
        updateObstacle(deltaTime);
        updateGround(deltaTime);
        updateBackground(deltaTime);

        score.setText("Score: " + punkte);
        Dimension d= score.getPreferredSize();
        int  center = 450;
        score.setBounds(center, 10, d.width,d.height);


        if(checkCollision()){
            gameOver();
        }

        this.repaint();
    }





    public static void main(String[] args) {

        AppWindow window = new AppWindow();

    }



}