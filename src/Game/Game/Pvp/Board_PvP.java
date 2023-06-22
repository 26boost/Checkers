package Game.Game.Pvp;

import Game.Game.GamePanel;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Board_PvP implements MouseListener {

    // Logic
    private int[][] board;
    private int[] current;
    private int[] multiEat;
    private Rectangle[][] hitBoxes;
    private boolean[][] hintMap;
    private boolean isWhiteTurn;

    // Graphic variables
    private final double scaleFactor = 2.5;
    private final int tileSize = 32;
    private final int xOffset = (int) ((300 + (1236 - 32 * scaleFactor * 8) / 2) / scaleFactor);
    private final int yOffset = (int) (((864 - 32 * scaleFactor * 8) / 2) / scaleFactor);

    // Assets
    private BufferedImage tile_white;
    private BufferedImage tile_red;
    private BufferedImage pin_white_0;
    private BufferedImage pin_white_1;
    private BufferedImage pin_red_0;
    private BufferedImage pin_red_1;
    private BufferedImage highlight;

    // ------------------------------------------- Logic ---------------------------------------------------------------

    public Board_PvP() {
        init();
    }

    private void init() {
        // Creates board
        board = new int[8][8];

        // fills board
        fillBoard();

        // Load Assets
        loadAssets();

        // Creates HitBoxes
        createHitBoxes();

        // start game
        isWhiteTurn = true;
    }

    public void checkWin() {
        boolean isRedTurn = !isWhiteTurn;

        // No more pins of a particular color
        int whitePins = 0;
        int redPins = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1 || board[i][j] == 2)
                    whitePins++;
                else if (board[i][j] == 3 || board[i][j] == 4)
                    redPins++;
            }
        if (redPins == 0) {
            GamePanel.winner = "white";
            Main.switchGameState(3, 2);
            return;
        }
        if (whitePins == 0) {
            GamePanel.winner = "red";
            Main.switchGameState(3, 2);
            return;
        }

        // No more moves for nobody
        whitePins = 0;
        redPins = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] == 1 || board[i][j] == 2) {
                    isWhiteTurn = true;
                    calcMoves(true, i, j);
                    if (hintMap != null)
                        whitePins++;
                    clearHints();
                }
                else if (board[i][j] == 3 || board[i][j] == 4) {
                    isWhiteTurn = false;
                    calcMoves(true, i, j);
                    if (hintMap != null)
                        redPins++;
                    clearHints();
                }
        if (redPins == 0) {
            GamePanel.winner = "white";
            Main.switchGameState(3, 2);
            return;
        }
        if (whitePins == 0) {
            GamePanel.winner = "red";
            Main.switchGameState(3, 2);
            return;
        }

        isWhiteTurn = !isRedTurn;
    }

    public void calcMoves(boolean isFirstMove, int y, int x) {
        hintMap = new boolean[8][8];

        if (isWhiteTurn) {
            // White Pin
            if (board[y][x] == 1) {
                // Base movement
                // Left Up
                if (x > 0 && y > 0 && board[y - 1][x - 1] == 0 && isFirstMove)
                    hintMap[y - 1][x - 1] = true;
                // Right Up
                if (x < 7 && y > 0 && board[y - 1][x + 1] == 0 && isFirstMove)
                    hintMap[y - 1][x + 1] = true;

                // Jump movement
                // Left Up
                if (x > 1 && y > 1 && board[y - 2][x - 2] == 0 && (board[y - 1][x - 1] == 3 || board[y - 1][x - 1] == 4))
                    hintMap[y - 2][x - 2] = true;
                // Right Up
                if (x < 6 && y > 1 && board[y - 2][x + 2] == 0 && (board[y - 1][x + 1] == 3 || board[y - 1][x + 1] == 4))
                    hintMap[y - 2][x + 2] = true;

                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        if (hintMap[i][j])
                            return;
            }
            // White King
            if (board[y][x] == 2) {
                // Base movement
                // Left Up
                if (x > 0 && y > 0 && board[y - 1][x - 1] == 0 && isFirstMove)
                    hintMap[y - 1][x - 1] = true;
                // Right Up
                if (x < 7 && y > 0 && board[y - 1][x + 1] == 0 && isFirstMove)
                    hintMap[y - 1][x + 1] = true;
                // Left Down
                if (x > 0 && y < 7 && board[y + 1][x - 1] == 0 && isFirstMove)
                    hintMap[y + 1][x - 1] = true;
                // Right Down
                if (x < 7 && y < 7 && board[y + 1][x + 1] == 0 && isFirstMove)
                    hintMap[y + 1][x + 1] = true;

                // Jump movement
                // Left Up
                if (x > 1 && y > 1 && board[y - 2][x - 2] == 0 && (board[y - 1][x - 1] == 3 || board[y - 1][x - 1] == 4))
                    hintMap[y - 2][x - 2] = true;
                // Right Up
                if (x < 6 && y > 1 && board[y - 2][x + 2] == 0 && (board[y - 1][x + 1] == 3 || board[y - 1][x + 1] == 4))
                    hintMap[y - 2][x + 2] = true;
                // Left Up
                if (x > 1 && y < 6 && board[y + 2][x - 2] == 0 && (board[y + 1][x - 1] == 3 || board[y + 1][x - 1] == 4))
                    hintMap[y + 2][x - 2] = true;
                // Right Up
                if (x < 6 && y < 6 && board[y + 2][x + 2] == 0 && (board[y + 1][x + 1] == 3 || board[y + 1][x + 1] == 4))
                    hintMap[y + 2][x + 2] = true;

                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        if (hintMap[i][j])
                            return;
            }
        }
        else {
            // Red Pin
            if (board[y][x] == 3) {
                // Base movement
                // Left Down
                if (x > 0 && y < 7 && board[y + 1][x - 1] == 0 && isFirstMove)
                    hintMap[y + 1][x - 1] = true;
                // Right Down
                if (x < 7 && y < 7 && board[y + 1][x + 1] == 0 && isFirstMove)
                    hintMap[y + 1][x + 1] = true;

                // Jump movement
                // Left Up
                if (x > 1 && y < 6 && board[y + 2][x - 2] == 0 && (board[y + 1][x - 1] == 1 || board[y + 1][x - 1] == 2))
                    hintMap[y + 2][x - 2] = true;
                // Right Up
                if (x < 6 && y < 6 && board[y + 2][x + 2] == 0 && (board[y + 1][x + 1] == 1 || board[y + 1][x + 1] == 2))
                    hintMap[y + 2][x + 2] = true;

                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        if (hintMap[i][j])
                            return;
            }
            // Red King
            if (board[y][x] == 4) {
                // Base movement
                // Left Up
                if (x > 0 && y > 0 && board[y - 1][x - 1] == 0 && isFirstMove)
                    hintMap[y - 1][x - 1] = true;
                // Right Up
                if (x < 7 && y > 0 && board[y - 1][x + 1] == 0 && isFirstMove)
                    hintMap[y - 1][x + 1] = true;
                // Left Down
                if (x > 0 && y < 7 && board[y + 1][x - 1] == 0 && isFirstMove)
                    hintMap[y + 1][x - 1] = true;
                // Right Down
                if (x < 7 && y < 7 && board[y + 1][x + 1] == 0 && isFirstMove)
                    hintMap[y + 1][x + 1] = true;

                // Jump movement
                // Left Up
                if (x > 1 && y > 1 && board[y - 2][x - 2] == 0 && (board[y - 1][x - 1] == 1 || board[y - 1][x - 1] == 2))
                    hintMap[y - 2][x - 2] = true;
                // Right Up
                if (x < 6 && y > 1 && board[y - 2][x + 2] == 0 && (board[y - 1][x + 1] == 1 || board[y - 1][x + 1] == 2))
                    hintMap[y - 2][x + 2] = true;
                // Left Up
                if (x > 1 && y < 6 && board[y + 2][x - 2] == 0 && (board[y + 1][x - 1] == 1 || board[y + 1][x - 1] == 2))
                    hintMap[y + 2][x - 2] = true;
                // Right Up
                if (x < 6 && y < 6 && board[y + 2][x + 2] == 0 && (board[y + 1][x + 1] == 1 || board[y + 1][x + 1] == 2))
                    hintMap[y + 2][x + 2] = true;

                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        if (hintMap[i][j])
                            return;
            }
        }
        clearHints();
    }

    private void game_event_0() {
        if (multiEat == null)
            clearHints();
    }

    private void game_event_1(int[] target) {
        calcMoves(true, target[0], target[1]);
        current = new int[]{target[0], target[1]};
    }

    private void game_event_2(int[] target) {
        if (hintMap[target[0]][target[1]]) {

            // create new pin
            board[target[0]][target[1]] = board[current[0]][current[1]];
            // remove old pin
            board[current[0]][current[1]] = 0;

            // eat
            if (Math.abs(target[0] - current[0]) == 2) {

                if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 1)
                    GamePanel.whiteRemovedPins++;
                if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 2)
                    GamePanel.whiteRemovedKings++;
                if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 3)
                    GamePanel.redRemovedPins++;
                if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 4)
                    GamePanel.redRemovedKings++;

                board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] = 0;
                promotePins();
                calcMoves(false, target[0],target[1]);
                if (hintMap != null) {
                    multiEat = new int[]{target[0], target[1]};
                }
                current = null;
            }
            if (multiEat == null) {
                switchTurn();
                clearHints();
                checkWin();
            }
        }
        else {
            calcMoves(true, target[0], target[1]);
            current = new int[]{target[0], target[1]};
        }
    }

    private void game_event_3(int[] target) {
        if (hintMap[target[0]][target[1]]) {
            // create new pin
            board[target[0]][target[1]] = board[multiEat[0]][multiEat[1]];
            // remove old pin
            board[multiEat[0]][multiEat[1]] = 0;
            // eat
            if (board[(target[0] + multiEat[0]) / 2][(target[1] + multiEat[1]) / 2] == 1)
                GamePanel.whiteRemovedPins++;
            if (board[(target[0] + multiEat[0]) / 2][(target[1] + multiEat[1]) / 2] == 2)
                GamePanel.whiteRemovedKings++;
            if (board[(target[0] + multiEat[0]) / 2][(target[1] + multiEat[1]) / 2] == 3)
                GamePanel.redRemovedPins++;
            if (board[(target[0] + multiEat[0]) / 2][(target[1] + multiEat[1]) / 2] == 4)
                GamePanel.redRemovedKings++;
            board[(target[0] + multiEat[0]) / 2][(target[1] + multiEat[1]) / 2] = 0;

            promotePins();
            calcMoves(false, target[0],target[1]);
            if (hintMap != null)
                multiEat = new int[]{target[0], target[1]};
            else {
                multiEat = null;
                System.out.println(Arrays.toString(multiEat));
                switchTurn();
            }
            checkWin();
        }
    }

    // ------------------------------------------- Initialization ------------------------------------------------------

    public void fillBoard() {
        for (int i = 0; i < 8; i++) {
            if (i % 2 != 0) {
                board[0][i] = 3;
                board[2][i] = 3;
                board[6][i] = 1;
            }
            else {
                board[1][i] = 3;
                board[5][i] = 1;
                board[7][i] = 1;
            }
        }
    }

    private void loadAssets() {
        try {
            tile_white = ImageIO.read(new File("src/Assets/tile_white.png"));
            tile_red = ImageIO.read(new File("src/Assets/tile_red.png"));
            pin_white_0 = ImageIO.read(new File("src/Assets/pin_white_0.png"));
            pin_white_1 = ImageIO.read(new File("src/Assets/pin_white_1.png"));
            pin_red_0 = ImageIO.read(new File("src/Assets/pin_red_0.png"));
            pin_red_1 = ImageIO.read(new File("src/Assets/pin_red_1.png"));
            highlight = ImageIO.read(new File("src/Assets/highlight.png"));
        } catch (IOException ex) {
            System.out.println("- Failed to load assets -");
        }
    }

    private void createHitBoxes() {
        hitBoxes = new Rectangle[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                hitBoxes[i][j] = new Rectangle(
                        (int) ((j * tileSize + xOffset) * scaleFactor), (int) ((i * tileSize + yOffset) * scaleFactor),
                        (int) (tileSize * scaleFactor),  (int) (tileSize * scaleFactor));
    }

    // ------------------------------------------- Utilities -----------------------------------------------------------

    private void clearHints() {
        hintMap = null;
    }

    private void promotePins() {
        for (int i = 0; i < 8; i++)
            if (board[0][i] == 1)
                board[0][i] = 2;
        for (int i = 0; i < 8; i++)
            if (board[7][i] == 3)
                board[7][i] = 4;
    }

    private void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
        promotePins();
    }

    private int[] findCell(int x, int y) {
        if (x > hitBoxes[0][0].getX() && x < hitBoxes[7][7].getX() + hitBoxes[7][7].getWidth() &&
                y > hitBoxes[0][0].getY() && y < hitBoxes[7][7].getY() + hitBoxes[7][7].getHeight())

            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)

                    if (x > hitBoxes[i][j].getX() && x < hitBoxes[i][j].getX() + hitBoxes[i][j].getWidth() &&
                            y > hitBoxes[i][j].getY() && y < hitBoxes[i][j].getY() + hitBoxes[i][j].getHeight())

                        return new int[]{i, j};
        return null;
    }

    public void draw(Graphics2D g2) {
        g2.scale(scaleFactor, scaleFactor);
        g2.translate(xOffset, yOffset);

        // Board backGround
        g2.setColor(Color.BLACK);
        g2.fillRect(-3, -3, tileSize * 8 + 6, tileSize * 8 + 6);
        g2.setColor(new Color(0x001642));
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(-4, -4, tileSize * 8 + 8, tileSize * 8 + 8);

        // Board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0)
                    g2.drawImage(tile_red, j * tileSize, i * tileSize, null);
                else
                    g2.drawImage(tile_white, j * tileSize, i * tileSize, null);
            }
        }

        // Pins
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (board[i][j]) {
                    case 1 -> g2.drawImage(pin_white_0, j * tileSize, i * tileSize, null);
                    case 2 -> g2.drawImage(pin_white_1, j * tileSize, i * tileSize, null);
                    case 3 -> g2.drawImage(pin_red_0, j * tileSize, i * tileSize, null);
                    case 4 -> g2.drawImage(pin_red_1, j * tileSize, i * tileSize, null);
                }
            }
        }

        // HintMap
        if (hintMap != null) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (hintMap[i][j])
                        g2.drawImage(highlight, j * tileSize, i * tileSize, null);
        }

        // current / multiEat
        g2.setColor(new Color(0x35C408));
        if (current != null)
            g2.drawRect(current[1] * tileSize, current[0] * tileSize, tileSize, tileSize);
        if (multiEat != null)
            g2.drawRect(multiEat[1] * tileSize, multiEat[0] * tileSize, tileSize, tileSize);

        g2.translate(-xOffset, -yOffset);
        g2.scale(1 / scaleFactor, 1 / scaleFactor);

        // Removed pins & kings
        g2.scale(1.5, 1.5);
        int i = 0;
        while (i < (Math.max(GamePanel.whiteRemovedPins + GamePanel.whiteRemovedKings,
                             GamePanel.redRemovedPins + GamePanel.redRemovedKings))) {

            if (i < GamePanel.whiteRemovedPins)
                g2.drawImage(pin_white_0, (80) + (i * 15), 400, null);
            else if (i - GamePanel.whiteRemovedPins < GamePanel.whiteRemovedKings)
                g2.drawImage(pin_white_1, (80) + (i * 15), 400, null);

            if (i < GamePanel.redRemovedPins)
                g2.drawImage(pin_red_0, (80) + (i * 15), 300, null);
            else if (i - GamePanel.redRemovedPins < GamePanel.redRemovedPins)
                g2.drawImage(pin_red_1, (80) + (i * 15), 300, null);

            i++;
        }
    }

    // ------------------------------------------- Mouse Listener ------------------------------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int[] target = findCell(e.getX(), e.getY());

        if (multiEat != null && target != null) {
            game_event_3(target);
        }
        else if (hintMap != null && target != null) {
            game_event_2(target);
        }
        else if (target != null && board[target[0]][target[1]] != 0) {
            game_event_1(target);
        }
        else {
            game_event_0();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}