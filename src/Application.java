import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Application {
    public static void main(String[] args){
        JFrame tracker = AppWindow();

        TrackingPanel trackingPanel = new TrackingPanel();

        tracker.add(new JScrollPane(trackingPanel), BorderLayout.CENTER);
        tracker.add(new ButtonPanel(trackingPanel), BorderLayout.SOUTH);
        tracker.setVisible(true);

    }

    static private JFrame AppWindow(){
        JFrame window = new JFrame("DnD Combat Tracker");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setContentPane(new JPanel(new BorderLayout()));
        window.setSize(900, 700);
        window.setResizable(false);

        try {
            String iconPath = "resources/Icon.png";
            InputStream iconStream = Application.class.getResourceAsStream(iconPath);
            BufferedImage appIcon = ImageIO.read(iconStream);
            window.setIconImage(appIcon);
        } catch (IOException e) {
            System.out.println("App Icon couldn't be loaded");
        }

        return window;
    }

}
