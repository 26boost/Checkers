package Game.Game;

import Game.Game.Pva.Board_PvA;
import Game.Game.Pvp.Board_PvP;
import Game.Main;
import Game.Menu.MenuPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JLayeredPane {

    // Graphics
    private int width;
    private int height;
    private Graphics2D g2;
    private BufferedImage image;

    // Timer
    private long timerInitialVal;

    // Components
    public static int whiteRemovedPins;
    public static int whiteRemovedKings;
    public static int redRemovedPins;
    public static int redRemovedKings;
    private JLabel timeLabel;
    private JLabel rWhite;
    private JLabel rRed;
    private JButton pauseButton;

    // Game
    private Board_PvP boardPvP;
    private Board_PvA boardPvA;
    public static String winner;

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
        timerInitialVal = System.nanoTime();

        // Game init
        if (MenuPanel.pvp) {
            boardPvP = new Board_PvP();
            addMouseListener(boardPvP);
            boardPvA = null;
        }
        else {
            boardPvA = new Board_PvA();
            addMouseListener(boardPvA);
            boardPvP = null;
        }
    }

    public void end() {
        if (boardPvP != null)
            boardPvP.fillBoard();
        if (boardPvA != null)
            boardPvA.fillBoard();
        this.removeAll();
    }

    private void createComponents() {

        // Panel properties
        this.setBackground(new Color(0xFF17223D, true));
        this.setOpaque(true);

        GamePanel.whiteRemovedPins = 0;
        GamePanel.redRemovedPins = 0;

        // Pause Button
        try {
            Font font = Font.createFont(Font.PLAIN, new File("src/Assets/QuinqueFive.otf"));

            // Time label
            timeLabel = new JLabel("- 00:00 -");
            timeLabel.setLocation(180, 250);
            timeLabel.setSize(300, 50);
            timeLabel.setFont(font.deriveFont(Font.PLAIN, 25));
            timeLabel.setForeground(Color.BLUE);

            // rWhite
            rWhite = new JLabel("| White captures: " + redRemovedPins);
            rWhite.setLocation(80, 400);
            rWhite.setSize(500, 50);
            rWhite.setFont(font.deriveFont(Font.PLAIN, 18));
            rWhite.setForeground(Color.BLUE);

            // rRed
            rRed = new JLabel("| Red captures: " + whiteRemovedPins);
            rRed.setLocation(80, 550);
            rRed.setSize(500, 50);
            rRed.setFont(font.deriveFont(Font.PLAIN, 18));
            rRed.setForeground(Color.BLUE);

            // Pause Button
            ImageIcon pause_icon = new ImageIcon(ImageIO.read(new File("src/Assets/pause_button.png")));
            pauseButton = new JButton();
            pauseButton.setLocation(50, 50);
            pauseButton.setSize(100, 100);
            pauseButton.setIcon(pause_icon);
            pauseButton.addActionListener(e -> Main.switchGameState(1, 2));

        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        // Adding components to panel
        add(timeLabel);
        add(rWhite);
        add(rRed);
        add(pauseButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        if (MenuPanel.pvp)
            boardPvP.draw(g2);
        else
            boardPvA.draw(g2);

        // Update components
        long timeElapsed = TimeUnit.SECONDS.convert(System.nanoTime() - timerInitialVal, TimeUnit.NANOSECONDS);
        timeLabel.setText(String.format("- %02d:%02d -", (timeElapsed % 3600) / 60, ((timeElapsed % 60))));
        rWhite.setText("| White captures: " + (redRemovedPins + redRemovedKings));
        rRed.setText("| Red captures: " + (whiteRemovedPins + whiteRemovedKings));

        repaint();
        render();
    }

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
}
