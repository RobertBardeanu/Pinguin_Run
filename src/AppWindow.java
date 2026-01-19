import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

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


    //variablen
    private JLabel charakter;
    private JLabel hinderniss;
    private JLabel robbenHinderniss;
    private JLabel baumHinderniss;
    private JLabel hintergrund;
    private JLabel groundlabel;

    private ImageIcon penguinOnGround;
    private ImageIcon penguinJump;

    private final int GROUND_Y = 250;

    private int yPos = GROUND_Y; // Startposition (Y-Achse)
    private int yVelocity = 0; // Aktuelle Sprunggeschwindigkeit
    private final int GRAVITY = 80;
    private final int JUMP_FORCE = -15000;

    private int obstacleX = 1000; // Startposition rechts außerhalb des Fensters
    private int treeSpeed = 300;     // Geschwindigkeit des Baums
    private Random random = new Random();
    private int treeh= random.nextInt(100,200);
    private boolean GameOver;

    private Jump_States currentJumpState = Jump_States.ON_GROUND;
    private final int MAX_JUMP_HEIGHT = 400;



    AppWindow()  {

        //Window intialisieren
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pinguin Run");
        this.setSize(1000, 500);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.white);
        this.setResizable(false);

        // Bild laden
        ImageIcon originalPenguinOnGround = new ImageIcon("src/Media/Bilder/penguin-ohnehintergrund.png");
        ImageIcon originalPenguinJump = new ImageIcon("src/Media/Bilder/pinguin.jump2-removebg-preview.png");

        ImageIcon originalIcontree = new ImageIcon("src/Media/Bilder/tree-ohnehintergrund.png");
        ImageIcon Hintergrund = new ImageIcon("src/Media/Bilder/BG_02.png"); //Hintergrund von craftpix.net
        ImageIcon originalGround = new ImageIcon("src/Media/Bilder/Ground_01.png");
        ImageIcon originalRobbe = new ImageIcon("src/Media/Bilder/Robbe.png");


        //Icon
        this.setIconImage(originalPenguinOnGround.getImage());



        // Bild skalieren (z.B. auf 50x50 Pixel)

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

        Image scaledImageRobbe = originalRobbe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon Robbe=new ImageIcon(scaledImageRobbe);

        // Bild in ein Label setzen
        charakter = new JLabel(penguinOnGround);

        hinderniss = new JLabel();

        hintergrund=new JLabel(hintergrundimage);

        groundlabel=new JLabel(ground);

        robbenHinderniss =new JLabel(Robbe);
        baumHinderniss =new JLabel(tree);

        charakter.setBounds(100, yPos, 200, 200);
        hinderniss.setBounds(obstacleX, yPos, 200, 200);
        robbenHinderniss.setBounds(obstacleX +10, yPos, 200, 200);
        hintergrund.setBounds(0, -100, getWidth(),getHeight());
        groundlabel.setBounds(0, -20, getWidth(),getHeight());

        // Label ins Fenster hinzufügen
        this.add(charakter);
        this.add(robbenHinderniss);
        this.add(hinderniss);

        this.add(groundlabel);

        this.add(hintergrund);

        // Erstes Hindernis festlegen
        hinderniss.setIcon(getRandomObstacleIcon());

        //Springen mit keylistener | Leerzeichen
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean sprung = true;


                if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && yPos >= GROUND_Y) {

                    yVelocity = (int)-Math.round(Math.sqrt(JUMP_FORCE * -2.0 * GRAVITY)); // Kraftvoller Sprung nach oben
                    System.out.println("yVel: " + yVelocity);

                }
            }
        });

        GameLoop loop = new GameLoop(this);
        loop.start();

        // Pinguin und alles erst ganz am Ende sichtbar machen, um java anzeigefehler zu meiden
        this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
        this.setVisible(true);
    }

    private void updateObstacle(final double deltaTime) {
        final int treeVel = (int)Math.round(treeSpeed * deltaTime);
        obstacleX -= treeVel <= 0 ? 1 : treeVel; // Hindernis bewegt sich nach links

        hinderniss.setLocation(obstacleX, GROUND_Y);

        if (obstacleX < -200) {
            obstacleX = getWidth();

            hinderniss.setIcon(getRandomObstacleIcon());
        }
    }

    private Icon getRandomObstacleIcon() {
        return switch (random.nextInt(1, 3)) {
            case 1 -> robbenHinderniss.getIcon();
            case 2 -> baumHinderniss.getIcon();
            default -> null;
        };
    }

    //Physik Methode fürs Springen
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

    private void updateSprite() {
        switch (currentJumpState) {
            case ON_GROUND -> charakter.setIcon(penguinOnGround);
            case JUMP -> charakter.setIcon(penguinJump);
        }
    }

    public void updateAll(final double deltaTime)
    {
        updatePhysics(deltaTime);
        updateObstacle(deltaTime);
    }


    public static void main(String[] args) {
        //windows abrufen
        AppWindow window = new AppWindow();

    }

}