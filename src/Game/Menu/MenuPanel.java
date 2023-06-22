package Game.Menu;

import Game.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JLayeredPane {

    // Graphics
    private Graphics2D g2;
    private BufferedImage image;

    // init
    public static boolean pvp = true;
    public void start() {

        image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Components
        createComponents();
    }

    public void end() {
        this.removeAll();
    }

    public void createComponents() {

        // Panel properties
        setBackground(new Color(0x012F67));
        setOpaque(true);

        // Components
        JLabel title;
        JLabel pvpText;
        JLabel pvaText;
        JButton exitButton;
        JButton pvpButton;
        JButton pvaButton;

        try {
            Font font = Font.createFont(Font.PLAIN, new File("src/Assets/QuinqueFive.otf"));

            // Title
            title = new JLabel("CHECKERS", SwingConstants.CENTER);
            title.setFont(font.deriveFont(Font.PLAIN, 100));
            title.setForeground(Color.BLUE);
            title.setSize(1200, 300);
            title.setLocation((Main.WIDTH / 2) - (title.getWidth() / 2), 100);

            // Play pvp Button
            ImageIcon pvp_icon = new ImageIcon(ImageIO.read(new File("src/Assets/pvp_button.png")));
            pvpButton = new JButton();
            pvpButton.setLocation(getWidth() / 2 - 330, 450);
            pvpButton.setSize(300, 100);
            pvpButton.setIcon(pvp_icon);
            pvpButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pvp = true;
                    Main.switchGameState(2, 0);
                }
            });

            // pvpText
            pvpText = new JLabel("- Player VS Player -", SwingConstants.CENTER);
            pvpText.setFont(font.deriveFont(Font.PLAIN, 10));
            pvpText.setForeground(Color.BLUE);
            pvpText.setSize(300, 50);
            pvpText.setLocation(pvpButton.getX(), pvpButton.getY() - 50);

            // Play pva Button
            ImageIcon pva_icon = new ImageIcon(ImageIO.read(new File("src/Assets/pva_button.png")));
            pvaButton = new JButton();
            pvaButton.setLocation(getWidth() / 2 + 30, 450);
            pvaButton.setSize(300, 100);
            pvaButton.setIcon(pva_icon);
            pvaButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pvp = false;
                    Main.switchGameState(2, 0);
                }
            });

            // pvpText
            pvaText = new JLabel("- Player VS Computer -", SwingConstants.CENTER);
            pvaText.setFont(font.deriveFont(Font.PLAIN, 10));
            pvaText.setForeground(Color.BLUE);
            pvaText.setSize(300, 50);
            pvaText.setLocation(pvaButton.getX(), pvaButton.getY() - 50);

            // Exit button
            ImageIcon exit_icon = new ImageIcon(ImageIO.read(new File("src/Assets/exit_button.png")));
            exitButton = new JButton();
            exitButton.setLocation(getWidth() / 2 - 126, 600);
            exitButton.setIcon(exit_icon);
            exitButton.setSize(252, 84);
            exitButton.addActionListener(e -> System.exit(69));
        }
        catch (IOException | FontFormatException ex){
            throw new RuntimeException(ex);
        }

        // Adding components to panel
        add(title);
        add(pvpText);
        add(pvaText);
        add(pvpButton);
        add(pvaButton);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        repaint();
        render();
    }

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
}
