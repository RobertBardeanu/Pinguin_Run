import  javax.swing.*;
public class Fenster {
    public static void main(String[] args) {
new  Fenster();


    }
    public Fenster() {
        int breite=750;
        int hoehe=250;
        JFrame fenster=new JFrame("Pinguin Run");
        fenster.setVisible(true);
        fenster.setSize(breite,hoehe);
        fenster.setLocationRelativeTo(null);
        fenster.setResizable(false);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("src\\Media\\Bilder\\Icon.png");
        fenster.setIconImage(icon.getImage());
    }

    //hallo
}
