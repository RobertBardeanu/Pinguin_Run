import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
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
    private int   punkte=0;
    private JLabel Score;
    private JLabel charakter;
    private JLabel hinderniss;
    private JLabel robbenHinderniss;
    private JLabel baumHinderniss;
    private JLabel netzHinderniss;
    private JLabel hintergrund;
    private JLabel hintergrund2,hintergrund3;

    private JLabel groundlabel,groundlabel2,groundlabel3;

    private ImageIcon penguinOnGround;
    private ImageIcon penguinJump;

    private final int GROUND_Y = 250;
    private final int BACKGROUND_Y=0;
    private int BACKGROUND_X=0;

    private int yPos = GROUND_Y; // Startposition (Y-Achse)
    private int yVelocity = 0; // Aktuelle Sprunggeschwindigkeit
    private final int GRAVITY = 80;
    private final int JUMP_FORCE = -15000;

    private int obstacleX = 1000; // Startposition rechts außerhalb des Fensters
    private int treeSpeed = 400;     // Geschwindigkeit des Baums
    private Random random = new Random();
    private boolean GameOver = false;
    BackgroundMusic musikPlayer = new BackgroundMusic();


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

        //Musik initialisieren

        musikPlayer.starteMusik();

        // Bild laden
        ImageIcon originalPenguinOnGround = new ImageIcon("src/Media/Bilder/penguin-ohnehintergrund.png");
        ImageIcon originalPenguinJump = new ImageIcon("src/Media/Bilder/pinguin.jump2-removebg-preview.png");

        ImageIcon originalIcontree = new ImageIcon("src/Media/Bilder/tree-ohnehintergrund.png");
        ImageIcon Hintergrund = new ImageIcon("src/Media/Bilder/BG_02.png"); //Hintergrund von craftpix.net
        ImageIcon originalGround = new ImageIcon("src/Media/Bilder/Ground_01.png");
        ImageIcon originalrobbe = new ImageIcon("src/Media/Bilder/Robbe.png");
        ImageIcon originalnetz = new ImageIcon("src/Media/Bilder/fischernetz.png");

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

        Image scaledImageRobbe = originalrobbe.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon Robbe=new ImageIcon(scaledImageRobbe);

        Image scaledImageNetz = originalnetz.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon Netz=new ImageIcon(scaledImageNetz);

        // Bild in ein Label setzen
        charakter = new JLabel(penguinOnGround);

        hinderniss = new JLabel();

        hintergrund=new JLabel(hintergrundimage);
        hintergrund2=new JLabel(hintergrundimage);
        hintergrund3=new JLabel(hintergrundimage);

        groundlabel=new JLabel(ground);
        groundlabel2=new JLabel(ground);
        groundlabel3=new JLabel(ground);

        Score=new JLabel("");

        Dimension d=Score.getPreferredSize();
        Score.setFont(new Font(Font.DIALOG,Font.BOLD,20));
        Score.setForeground(Color.red);



        robbenHinderniss =new JLabel(Robbe);
        baumHinderniss =new JLabel(tree);
        netzHinderniss =new JLabel(Netz);

        charakter.setBounds(100, yPos, 200, 200);
        hinderniss.setBounds(obstacleX, yPos, 200, 200);
        robbenHinderniss.setBounds(obstacleX +10, yPos, 200, 200);
        hintergrund.setBounds(0, -100, getWidth(),getHeight());
        hintergrund3.setBounds(0, -100, getWidth(),getHeight());
        hintergrund2.setBounds(0, -100, getWidth(),getHeight());
        Score.setBounds(0, 0, 100,20);
        groundlabel.setBounds(0, -20, getWidth(),getHeight());
        groundlabel2.setBounds(0, -20, getWidth(),getHeight());
        groundlabel3.setBounds(0, -20, getWidth(),getHeight());


        // Label ins Fenster hinzufügen
        this.add(charakter);

        this.add(Score);
        this.add(robbenHinderniss);

        this.add(hinderniss);

        this.add(groundlabel);
        this.add(groundlabel2);
        this.add(groundlabel3);

        this.add(hintergrund2);
        this.add(hintergrund);
        this.add(hintergrund3);
        Score.setText("Score:"+0);


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

    private void gameOver(){
        GameOver = true;
        System.out.println("Kollision! Spiel vorbei. Score: " + punkte);
        musikPlayer.stoppeMusik();


        Score.setText("Game Over :(");
        Score.revalidate();
        Dimension d=Score.getPreferredSize();
        Score.setBounds(0, 0, d.width,d.height);

    }

    private void updateObstacle(final double deltaTime) {
        final int treeVel = (int)Math.round(treeSpeed * deltaTime);
        obstacleX -= treeVel <= 0 ? 1 : treeVel; // Hindernis bewegt sich nach links

        hinderniss.setLocation( obstacleX, GROUND_Y);

        if (obstacleX < -200) {
            obstacleX = getWidth();
            hinderniss.setIcon(getRandomObstacleIcon());
            score();
        }


    }


    private void updateBackground(final double deltaTime) {
        final int speed = (int)Math.round(100 * deltaTime);
        BACKGROUND_X -= speed <= 0 ? 1 : speed; // Hindernis bewegt sich nach links

        hintergrund.setLocation( BACKGROUND_X, 0);
        hintergrund2.setLocation( BACKGROUND_X +getWidth(),0);
        hintergrund3.setLocation( BACKGROUND_X -getWidth(),0);
        groundlabel.setLocation( BACKGROUND_X ,-20);
        groundlabel2.setLocation( BACKGROUND_X +getWidth(),-20);
        groundlabel3.setLocation( BACKGROUND_X -getWidth(),-20);


        if (BACKGROUND_X < -getWidth()) {
            BACKGROUND_X = getWidth();

        }
    }

    private Icon getRandomObstacleIcon() {
        return switch (random.nextInt(1, 4)) {
            case 1 -> robbenHinderniss.getIcon();
            case 2 -> baumHinderniss.getIcon();
            case 3 -> netzHinderniss.getIcon();
            default -> null;
        };
    }
    private void score(){
        punkte+=random.nextInt(1,10);
        Score.setText("Score:"+String.valueOf(punkte));
        Score.revalidate();
        Dimension d=Score.getPreferredSize();
        Score.setBounds(0, 0, d.width,d.height);
    }

    //Kollision
    private boolean checkCollision(){
        Rectangle pinguinRect = charakter.getBounds();
        Rectangle hindernisRect = hinderniss.getBounds();

        //hitboxen verkleinern geht iwie mit grow
        // -h, -v zieht an jeder seite soviele pixel ab quasi

        pinguinRect.grow(-70, -65);
        hindernisRect.grow(-70, -90);
        return pinguinRect.intersects(hindernisRect);
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


        //
    public void updateAll(final double deltaTime)
    {

        if (GameOver) {
            return;
        }

        updatePhysics(deltaTime);
        updateObstacle(deltaTime);
        updateBackground(deltaTime);

        if(checkCollision()){
            gameOver();
        }
    }





    public static void main(String[] args) {
        //windows abrufen
        AppWindow window = new AppWindow();

    }


}