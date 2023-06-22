package Game.End;

import Game.Game.GamePanel;
import Game.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EndPanel extends JLayeredPane {

    // Graphics
    private int width;
    private int height;
    private Graphics2D g2;
    private BufferedImage image;

    // Components
    private JLabel winnerText;
    private JButton menuButton;
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

        // Panel properties
        this.setBackground(new Color(0xCB404381, true));
        this.setOpaque(true);

        try {
            Font font = Font.createFont(Font.PLAIN, new File("src/Assets/QuinqueFive.otf"));

            // Title
            if (Objects.equals(GamePanel.winner, "white")) {
                winnerText = new JLabel("WHITE WINS", SwingConstants.CENTER);
                winnerText.setForeground(Color.WHITE);
            }
            else {
                winnerText = new JLabel("RED WINS", SwingConstants.CENTER);
                winnerText.setForeground(Color.RED);
            }
            winnerText.setFont(font.deriveFont(Font.PLAIN, 70));
            winnerText.setSize(1200, 300);
            winnerText.setLocation((Main.WIDTH / 2) - (winnerText.getWidth() / 2), 100);


            // Menu button
            ImageIcon menu_icon = new ImageIcon(ImageIO.read(new File("src/Assets/mainMenu_button.png")));
            menuButton = new JButton();
            menuButton.setLocation(getWidth() / 2 - 330, 450);
            menuButton.setSize(300, 100);
            menuButton.setIcon(menu_icon);
            menuButton.addActionListener(e -> Main.switchGameState(0, 1));

            // Restart button
            ImageIcon restart_icon = new ImageIcon(ImageIO.read(new File("src/Assets/playAgain_button.png")));
            restartButton = new JButton();
            restartButton.setLocation(getWidth() / 2 + 30, 450);
            restartButton.setSize(300, 100);
            restartButton.setIcon(restart_icon);
            restartButton.addActionListener(e -> Main.switchGameState(2, -1));

        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        // Adding components to panel
        add(winnerText);
        add(menuButton);
        add(restartButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // background
        g2.setColor(new Color(0xFF00162C, true));
        g2.fillRect(0, 0, 200, getHeight());
        g2.fillRect(getWidth() - 200, 0, 200, getHeight());

        if (Objects.equals(GamePanel.winner, "white"))
            g2.setColor(new Color(0xCBCBCB));
        else
            g2.setColor(new Color(0xFF4646));
        g2.fillRect(196, 0, 8, getHeight());
        g2.fillRect(getWidth() - 204, 0, 8, getHeight());
    }
}
