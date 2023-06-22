package Game.Pause;

import Game.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PausePanel extends JLayeredPane {

    // Graphics
    private int width;
    private int height;
    private Graphics2D g2;
    private BufferedImage image;

    // Components
    private JButton menuButton;
    private JButton resumeButton;
    private JButton restartButton;

    // init
    public void start() {
        width = Main.WIDTH;
        height = Main.HEIGHT;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Components
        createComponents();
    }

    public void end() {
        this.removeAll();
    }

    private void createComponents() {
        System.out.println();

        // Panel properties
        this.setBackground(new Color(0xA0313579, true));
        this.setOpaque(true);

        try {
            // Menu button
            ImageIcon menu_icon = new ImageIcon(ImageIO.read(new File("src/Assets/menu_button.png")));
            menuButton = new JButton();
            menuButton.setSize(100, 200);
            menuButton.setLocation(50, 50);
            menuButton.setIcon(menu_icon);
            menuButton.addActionListener(e -> Main.switchGameState(0, 1));

            // Resume button
            ImageIcon resume_icon = new ImageIcon(ImageIO.read(new File("src/Assets/resume_button.png")));
            resumeButton = new JButton();
            resumeButton.setSize(100, 200);
            resumeButton.setLocation(50, 300);
            resumeButton.setIcon(resume_icon);
            resumeButton.addActionListener(e -> Main.switchGameState(2, 1));

            // Restart button
            ImageIcon restart_icon = new ImageIcon(ImageIO.read(new File("src/Assets/restart_button.png")));
            restartButton = new JButton();
            restartButton.setSize(100, 200);
            restartButton.setLocation(50, 550);
            restartButton.setIcon(restart_icon);
            restartButton.addActionListener(e -> Main.switchGameState(2, -1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Adding components to panel
        add(menuButton);
        add(resumeButton);
        add(restartButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Button background
        g2.setColor(new Color(0xFF001C44, true));
        g2.fillRect(0, 0, 200, getHeight());
        g2.fillRect(getWidth() - 200, 0, 200, getHeight());
        g2.setColor(new Color(0x000034));
        g2.fillRect(196, 0, 4, getHeight());
        g2.fillRect(getWidth() - 194, 0, 4, getHeight());
    }
}
