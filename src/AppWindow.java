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
    private int   punkte=0;
    private JLabel Score;
    private JLabel charakter;
    private JLabel hinderniss;
    private JLabel robbenHinderniss;
    private JLabel baumHinderniss;
    private JLabel netzHinderniss;
    private JLabel hintergrund;
    private JLabel hintergrund2,hintergrund3;
    private JButton resetbutton;

    final private JLabel groundlabel,groundlabel2,groundlabel3;

    final ImageIcon penguinOnGround;
    final private ImageIcon penguinJump;

    private final int GROUND_Y = 300;
    private int BACKGROUND_X=0;
    private int GROUND_X = 0;

    private int yPos = GROUND_Y; // Startposition (Y-Achse)
    private int yVelocity = 0; // Aktuelle Sprunggeschwindigkeit
    private final int GRAVITY = 80;
    private final int JUMP_FORCE = -15000;

    private int obstacleX = 1000; // Startposition rechts außerhalb des Fensters
    private int treeSpeed = 500;     // Geschwindigkeit des Baums
    final private Random random = new Random();
    private boolean GameOver = false,Paused = false;

    BackgroundMusic musikPlayer = new BackgroundMusic();


    private Jump_States currentJumpState = Jump_States.ON_GROUND;



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
        resetbutton=new JButton("Reset");

        Score.setFont(new Font(Font.DIALOG,Font.BOLD,20));
        Score.setForeground(Color.red);
        resetbutton.setBackground(Color.red);
        resetbutton.setFont(new Font(Font.DIALOG,Font.BOLD,20));



        robbenHinderniss =new JLabel(Robbe);
        baumHinderniss =new JLabel(tree);
        netzHinderniss =new JLabel(Netz);

        charakter.setBounds(100, yPos, 100, 100);
        hinderniss.setBounds(obstacleX, yPos, 100, 100);
        robbenHinderniss.setBounds(obstacleX +10, yPos, 200, 200);
        hintergrund.setBounds(0, -100, getWidth(),getHeight());
        hintergrund3.setBounds(0, -100, getWidth(),getHeight());
        hintergrund2.setBounds(0, -100, getWidth(),getHeight());
        Score.setBounds(0, 0, 100,20);
        groundlabel.setBounds(0, -20, getWidth(),getHeight());
        groundlabel2.setBounds(0, -20, getWidth(),getHeight());
        resetbutton.setBounds(450, 200,100,100);
            groundlabel3.setBounds(0, -20, getWidth(),getHeight());
        resetbutton.setVisible(false);

        // Label ins Fenster hinzufügen
        this.add(resetbutton);

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


        // Erstes Hindernis festlegen
        hinderniss.setIcon(getRandomObstacleIcon());


        //Springen mit keylistener | Leerzeichen
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


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
        resetbutton.setVisible(true);
        GameOver = true;
        System.out.println("Kollision! Spiel vorbei. Score: " + punkte);
        musikPlayer.stoppeMusik();

        Score.setText("Game Over :(");
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

        if (BACKGROUND_X < -getWidth()) {
            BACKGROUND_X = getWidth();
        }
    }

    private void updateGround(final double deltaTime) {
        final int speed = (int)Math.round(treeSpeed * deltaTime);
        GROUND_X -= speed <= 0 ? 1 : speed; // Hindernis bewegt sich nach links

        groundlabel.setLocation( GROUND_X ,-20);
        groundlabel2.setLocation( GROUND_X +getWidth(),-20);
        groundlabel3.setLocation( GROUND_X -getWidth(),-20);

        if (GROUND_X < -getWidth()) {
            GROUND_X = getWidth();
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
        int maxSpeed = 2000     ;
        if(punkte>=10&&treeSpeed < maxSpeed)
        {
            treeSpeed = treeSpeed+10;
            System.out.println("treeSpeed: " + treeSpeed);
        }
        if(treeSpeed>=1000){
            punkte+=random.nextInt(10,100);
        }
        else{
            punkte+=random.nextInt(1,10);
        }
    }

    //Kollision
    private boolean checkCollision(){
        Rectangle pinguinRect = charakter.getBounds();
        Rectangle hindernisRect = hinderniss.getBounds();

        //hitboxen verkleinern geht iwie mit grow
        // -h, -v zieht an jeder seite soviele pixel ab quasi

        pinguinRect.grow(-15, -10);
        hindernisRect.grow(-15, -10);
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
        if (Paused){
            return;
        }

        updatePhysics(deltaTime);
        updateObstacle(deltaTime);
        updateBackground(deltaTime);
        updateGround(deltaTime);
        Score.setText("Score:"+punkte);
        Dimension d=Score.getPreferredSize();
        Score.setBounds(0, 0, d.width,d.height);


        if(checkCollision()){
            gameOver();
        }

        this.repaint();
    }





    public static void main(String[] args) {
        //windows abrufen
        AppWindow window = new AppWindow();

    }

    //hitbox maler von chati

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        // Zeichne Pinguin-Box (Rot)
        Rectangle p = charakter.getBounds();
        p.grow(-15, -10);
        g2.setColor(Color.RED);
        g2.drawRect(p.x, p.y, p.width, p.height);

        // Zeichne Hindernis-Box (Blau)
        Rectangle h = hinderniss.getBounds();
        h.grow(-15, -15);
        g2.setColor(Color.BLUE);
        g2.drawRect(h.x, h.y, h.width, h.height);
    }

}