import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class AppWindow extends JFrame {

    //variablen
    JLabel charakter;
    JLabel hinderniss;
    JLabel hintergrund;
    JLabel groundlabel;

    int yPos = 250; // Startposition (Y-Achse)
    int yVelocity = 0; // Aktuelle Sprunggeschwindigkeit
    final int GRAVITY = 80;
    final int JUMP_FORCE = -15000;
    int treeX = 1000;      // Startposition rechts außerhalb des Fensters
    int treeSpeed = 300;     // Geschwindigkeit des Baums
    Random rand = new Random();
    int treeh=rand.nextInt(100,200);


    AppWindow()  {

        //Window intialisieren
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pinguin Run");
        this.setSize(1000, 500);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.white);
        this.setResizable(false);


        //Urls
        String urlsprung="src/Media/Bilder/pinguingif2.png";//Testbild
        String urloriginal="src/Media/Bilder/penguin-ohnehintergrund.png";//Hintergrund von craftpix.net
        String urlHintergrund="src/Media/Bilder/BG_02.png";
        String urlGround="src/Media/Bilder/Ground_01.png";
        String urlTree="src/Media/Bilder/tree-ohnehintergrund.png";

        // Bild laden
        ImageIcon originalIconpenguin = new ImageIcon(urloriginal);
        ImageIcon JumpingIconpenguin = new ImageIcon(urlsprung);
        ImageIcon originalIcontree = new ImageIcon(urlTree);
        ImageIcon Hintergrund = new ImageIcon(urlHintergrund);
        ImageIcon originalGround = new ImageIcon(urlGround);


        //Icon
        this.setIconImage(originalIconpenguin.getImage());



        // Bild skalieren (z.B. auf 50x50 Pixel)
        Image scaledImagejump = JumpingIconpenguin.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image scaledImagepenguin = originalIconpenguin.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon penguin = new ImageIcon(scaledImagepenguin);

        Image scaledImagetree = originalIcontree.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon tree = new ImageIcon(scaledImagetree);

        Image scaledImageHintergrund = Hintergrund.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon hintergrundimage = new ImageIcon(scaledImageHintergrund);

        Image scaledImageGround = originalGround.getImage().getScaledInstance(1000, -200, Image.SCALE_SMOOTH);
        ImageIcon ground=new ImageIcon(scaledImageGround);

        // Bild in ein Label setzen
        charakter = new JLabel(penguin);

        hinderniss = new JLabel(tree);

        hintergrund=new JLabel(hintergrundimage);

        groundlabel=new JLabel(ground);

        charakter.setBounds(100, yPos, 200, 200);
        hinderniss.setBounds(treeX, yPos, 200, treeh);
        hintergrund.setBounds(0, -100, getWidth(),getHeight());
        groundlabel.setBounds(0, -20, getWidth(),getHeight());

        // Label ins Fenster hinzufügen
        this.add(charakter);
        this.add(hinderniss);
        this.add(groundlabel);

        this.add(hintergrund);


        //timer für animation







//        Timer timer = new Timer(20, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                updatePhysics() ;
//            }
//        });
//        timer.start();
//        Timer timer2 = new Timer(2, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                updateTree();
//            }
//        });
//        timer2.start();


        //Springen mit keylistener | Leerzeichen
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean sprung = true;


                if (e.getKeyCode() == KeyEvent.VK_SPACE && yPos >= 250 ) {

                    yVelocity = (int)-Math.round(Math.sqrt(JUMP_FORCE * -2.0 * GRAVITY)); // Kraftvoller Sprung nach oben
                    System.out.println("yVel: " + yVelocity);

                }



            }
        });

        GameLoop loop = new GameLoop(this);
        loop.start();

        // Pinguin und alles erst ganz am Ende sichtbar machen um java anzeigefehler zu meiden
        this.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
        this.setVisible(true);


    }
    private void updateTree(final double deltaTime) {
        final int treeVel = (int)Math.round(treeSpeed * deltaTime);
        treeX -= treeVel <= 0 ? 1 : treeVel; // Baum bewegt sich nach links

        // Wenn der Baum links aus dem Bild ist
        if (treeX < -200) {
            treeX = getWidth(); // rechts neu starten
        }

        hinderniss.setLocation(treeX, 260);
    }
    //Physik Methode fürs Springen

    private void updatePhysics(final double deltaTime) {

        //Sprung
        yVelocity += GRAVITY; // Schwerkraft wirkt ständig
        yPos += (int)Math.round(yVelocity * deltaTime);    // Position ändern

        // Boden-Kollision (damit er nicht aus dem Bild fällt)
        if (yPos >= 250) {      //>250, da Koordinatensystem in swing anders ist. Y verläuft nach unten. Oben links ist (0|0)
            yPos = 250;
            yVelocity = 0;

        }

        // Label neu positionieren
        charakter.setLocation(charakter.getX(), yPos);


        //Animation abspielen
        if(yPos == 250 ) {
            charakter.imageUpdate("")
        }
    }

    public void updateAll(final double deltaTime)
    {
        updatePhysics(deltaTime);
        updateTree(deltaTime);
    }


    public static void main(String[] args) {
        //windows abrufen
        AppWindow window = new AppWindow();



    }

}