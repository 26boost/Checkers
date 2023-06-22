package Game;

import Game.End.EndPanel;
import Game.Game.GamePanel;
import Game.Menu.MenuPanel;
import Game.Pause.PausePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    // Window
    public static CardLayout cardLayout;
    private static JPanel cards;
    public static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    public static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());

    // Panels
    private static MenuPanel menuPanel;        // layer_0
    private static PausePanel pausePanel;      // layer_1
    private static GamePanel gamePanel;        // layer_2
    private static EndPanel endPanel;          // layer_3

    public Main() {
        init();
    }

    private void init() {

        // Frame
        setSize(WIDTH, HEIGHT - 4);
        setLocationRelativeTo(null);
        setUndecorated(true);
        cardLayout = new CardLayout();
        cards = new JPanel();
        cards.setLayout(cardLayout);

        // menu Panel
        menuPanel = new MenuPanel();
        cards.add(menuPanel, "layer_0");

        // pause Panel
        pausePanel = new PausePanel();
        cards.add(pausePanel, "layer_1");

        // game Panel
        gamePanel = new GamePanel();
        cards.add(gamePanel, "layer_2");

        // end Panel WW
        endPanel = new EndPanel();
        cards.add(endPanel, "layer_3");

        add(cards);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                switchGameState(0, 0);
            }
        });
    }

    public static void switchGameState(int gameState, int currentGameState) {
        switch (gameState) {
            case 0 -> {
                menuPanel.start();
                endPanel.end();
                pausePanel.end();
                gamePanel.end();


                cardLayout.show(cards, "layer_0");
            }
            case 1 -> {
                pausePanel.start();

                cardLayout.show(cards, "layer_1");
            }
            case 2 -> {
                if (currentGameState == -1) {
                    gamePanel.end();
                    gamePanel.start();
                }
                if (currentGameState == 0 || currentGameState == 3) {
                    gamePanel.start();
                    menuPanel.end();
                    endPanel.end();
                }
                pausePanel.end();

                cardLayout.show(cards, "layer_2");
            }
            case 3 -> {
                gamePanel.end();
                endPanel.start();

                cardLayout.show(cards, "layer_3");
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}