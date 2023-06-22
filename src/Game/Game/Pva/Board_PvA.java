package Game.Game.Pva;

import Game.Game.GamePanel;
import Game.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board_PvA implements MouseListener {

    // Logic
    private final int[][] board = new int[8][8];
    private Rectangle[][] hitBoxes;
    private boolean[][] hintMap;
    private int[] current;
    private int[] multiEat;
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

    // ---------------------------------------- Logic ------------------------------------------------------------------

    public Board_PvA() {

        fillBoard();

        // load assets
        loadAssets();

        // creates hitBoxes
        createHitBoxes();

        // start game
        isWhiteTurn = true;
	}

    public Board_PvA(int[][] board) {
        for(int i=0; i<8; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, 8);
        }
    }

    public int checkWin(boolean turno) {
        //turno è false per il nero, true, per il bianco
        //0: nessuno ha ancora vinto
        //-1: ha vinto il nero
        //1: ha vinto il bianco
        int blackPieces=0;
        int whitePieces=0;
        //count pieces
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(board[i][j] == 1 || board[i][j] == 2) {
                    whitePieces++;
                }
                if(board[i][j] == 3 || board[i][j] == 4) {
                    blackPieces++;
                }
            }
        }
        if(whitePieces==0) {
            return -1;
        }
        if(blackPieces==0) {
            return 1;
        }

        //check if there is a possible move
        boolean possibleMove=false;
        boolean [][] possibleMoves;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(turno) {
                    if(board[i][j] == 1 || board[i][j] == 2) {
                        possibleMoves= calcolaMosse(true, i, j);
                        for(int ii=0; ii<8; ii++) {
                            for(int jj=0; jj<8; jj++) {
                                if (possibleMoves[ii][jj]) {
                                    possibleMove = true;
                                    break;
                                }
                            }
                            if(possibleMove) {
                                break;
                            }
                        }
                        if(possibleMove) {
                            break;
                        }
                    }
                }
                if(!turno) {
                    if(board[i][j] == 3 || board[i][j] == 4) {
                        possibleMoves= calcolaMosse(true, i, j);
                        for(int ii=0; ii<8; ii++) {
                            for(int jj=0; jj<8; jj++) {
                                if(possibleMoves[ii][jj]) {
                                    possibleMove=true;
                                    break;
                                }
                            }
                            if(possibleMove) {
                                break;
                            }
                        }
                        if(possibleMove) {
                            break;
                        }
                    }
                }
            }
            if(possibleMove) {
                break;
            }
        }
        if(!possibleMove) {
            if(turno) {
                return -1;
            }
            else {
                return 1;
            }
        }

        //default
        return 0;
    }

    public boolean[][] calcolaMosse(boolean primaMossa, int y, int x) {
        boolean[][] answer= new boolean[8][8];
        for(int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                answer[i][j]=false;
            }
        }
        //controllo delle mosse: quale mossa può fare un pezzo dipende:
        //dalla sua posizione, dal suo colore, dal suo rango e da se è la sua prima mossa

        //mossa normale avanti a destra
        if(x!=7 && y!=0)
            if((board[y][x]==1 || board[y][x]==2 || board[y][x]==4)
                    && board[y-1][x+1]==0
                    && primaMossa) {
                answer[y-1][x+1]=true;
            }
        //mossa normale avanti a sinistra
        if(x!=0 && y!=0)
            if((board[y][x]==1 || board[y][x]==2 || board[y][x]==4)
                    && board[y-1][x-1]==0
                    && primaMossa) {
                answer[y-1][x-1]=true;
            }
        //mossa normale indietro a destra
        if(x!=7 && y!=7)
            if((board[y][x]==3 || board[y][x]==2 || board[y][x]==4)
                    && board[y+1][x+1]==0
                    && primaMossa) {
                answer[y+1][x+1]=true;
            }
        //mossa normale indietro a sinistra
        if(x!=0 && y!=7)
            if((board[y][x]==3 || board[y][x]==2 || board[y][x]==4)
                    && board[y+1][x-1]==0
                    && primaMossa) {
                answer[y+1][x-1]=true;
            }

        //presa avanti a destra
        if(x<6 && y>1)
            if((board[y][x]==1 || board[y][x]==2 || board[y][x]==4)
                    && (((board[y-1][x+1]==1 || board[y-1][x+1]==2) && (board[y][x]==3 || board[y][x]==4))
                    ||(board[y-1][x+1]==3 || board[y-1][x+1]==4) && (board[y][x]==1 || board[y][x]==2))
                    &&(board[y-2][x+2]==0)){
                answer[y-2][x+2]=true;
            }
        //presa avanti a sinistra
        if(x>1 && y>1)
            if((board[y][x]==1 || board[y][x]==2 || board[y][x]==4)
                    && (((board[y-1][x-1]==1 || board[y-1][x-1]==2) && (board[y][x]==3 || board[y][x]==4))
                    ||(board[y-1][x-1]==3 || board[y-1][x-1]==4) && (board[y][x]==1 || board[y][x]==2))
                    &&(board[y-2][x-2]==0)){
                answer[y-2][x-2]=true;
            }
        //presa indietro a destra
        if(x<6 && y<6)
            if((board[y][x]==3 || board[y][x]==2 || board[y][x]==4)
                    && (((board[y+1][x+1]==1 || board[y+1][x+1]==2) && (board[y][x]==3 || board[y][x]==4))
                    ||(board[y+1][x+1]==3 || board[y+1][x+1]==4) && (board[y][x]==1 || board[y][x]==2))
                    &&(board[y+2][x+2]==0)){
                answer[y+2][x+2]=true;
            }
        //presa indietro a sinistra
        if(x>1 && y<6)
            if((board[y][x]==3 || board[y][x]==2 || board[y][x]==4)
                    && (((board[y+1][x-1]==1 || board[y+1][x-1]==2) && (board[y][x]==3 || board[y][x]==4))
                    ||(board[y+1][x-1]==3 || board[y+1][x-1]==4) && (board[y][x]==1 || board[y][x]==2))
                    &&(board[y+2][x-2]==0)){
                answer[y+2][x-2]=true;
            }

        return answer;
    }

    public int[] bestMove(boolean turn, boolean firstMove) {
        int[] solution;
        //48=12*4
        int [][] possibleMoves = new int [48][4];
        int [] movesEvaluation =new int [48];
        //inizializzo tutti i valori di movesEvaluation a valori che non possono esistere
        for (int i=0; i<48; i++) {
            movesEvaluation[i]=1000;
        }
        int pos=0;
        boolean[][] legalMovesField;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(turn && (board[i][j]==1 || board[i][j]==2)
                        || !turn && (board[i][j]==3 || board[i][j]==4))
                {
                    legalMovesField=calcolaMosse(firstMove, i, j);
                    for(int ii=0; ii<8; ii++) {
                        for(int jj=0; jj<8; jj++) {
                            if(legalMovesField[ii][jj]) {
                                possibleMoves[pos][0]=i;
                                possibleMoves[pos][1]=j;
                                possibleMoves[pos][2]=ii;
                                possibleMoves[pos][3]=jj;
                                pos++;
                            }
                        }
                    }
                }
            }
        }

        for (int i=0; i<48; i++) {
            Board_PvA prova = new Board_PvA(board);
            if(	possibleMoves[i][0]!= 0
                    ||possibleMoves[i][1]!= 0
                    ||possibleMoves[i][2]!= 0
                    ||possibleMoves[i][3]!= 0) {

                //posizionare la nuova pedina
                prova.setCasa(possibleMoves[i][2], possibleMoves[i][3], prova.getCasa(possibleMoves[i][0], possibleMoves[i][1]));
                //rimuovere la vecchia pedina
                prova.setCasa(possibleMoves[i][0], possibleMoves[i][1], 0);
                //rimuovere eventuali pedine catturate
                if(possibleMoves[i][0]-possibleMoves[i][2]==2 || possibleMoves[i][0]-possibleMoves[i][2]==-2) {
                    prova.setCasa((possibleMoves[i][0]+possibleMoves[i][2])/2, (possibleMoves[i][1]+possibleMoves[i][3])/2, 0);
                }

                //promozione (?)
                for (int ii=0; ii<8; ii++) {
                    if(prova.getCasa(0, ii)==1) {
                        prova.setCasa(0, ii, 2);
                    }
                    if(prova.getCasa(7, ii)==2) {
                        prova.setCasa(7, ii, 3);
                    }
                }

                movesEvaluation[i]=prova.evaluatePosition(turn, firstMove, 0, possibleMoves[i][2], possibleMoves[i][3]);
            }
        }

        //ricerca della mossa migliore
        int bestMove=0;
        for(int i=0; i<48; i++) {
            if(turn) {
                if(movesEvaluation[i]>movesEvaluation[bestMove] && movesEvaluation[i]!= 1000) {
                    bestMove=i;
                }
            }
            else {
                if(movesEvaluation[i]<movesEvaluation[bestMove] && movesEvaluation[i]!= 1000) {
                    bestMove=i;
                }

            }
        }

        solution=possibleMoves[bestMove];

        return solution;
    }

    public int evaluatePosition(boolean turn, boolean firstMove, int depth, int yLand, int xLand) {
        int evaluation=0;
        int win = checkWin(turn);
        if(win==1) {
            return 222;
        }
        if(win==-1) {
            return-222;
        }
        if(depth==4) {
            //material
            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    if (board[i][j]==1) {
                        evaluation=evaluation+3;
                    }
                    if (board[i][j]==3) {
                        evaluation=evaluation-3;
                    }
                    if (board[i][j]==2) {
                        evaluation=evaluation+5;
                    }
                    if (board[i][j]==4) {
                        evaluation=evaluation-5;
                    }
                }
            }
            //center control
            if(board[3][3]==1 || board[3][3]==2) {
                evaluation++;
            }
            if(board[3][3]==3 || board[3][3]==4) {
                evaluation--;
            }

            if(board[3][4]==1 || board[3][4]==2) {
                evaluation++;
            }
            if(board[3][4]==3 || board[3][4]==4) {
                evaluation--;
            }

            if(board[4][3]==1 || board[4][3]==2) {
                evaluation++;
            }
            if(board[4][3]==3 || board[4][3]==4) {
                evaluation--;
            }

            if(board[4][4]==1 || board[4][4]==2) {
                evaluation++;
            }
            if(board[4][4]==3 || board[4][4]==4) {
                evaluation--;
            }

            //turn
            if(turn) {
                evaluation++;
            }
            else {
                evaluation--;
            }

        }
        else {
            int [][] possibleMoves = new int [48][4];
            boolean theMoveWasACapture;
            int pos=0;
            boolean[][] legalMovesField;
            for(int i=0; i<8; i++) {
                for(int j=0; j<8; j++) {
                    if(turn && (board[i][j]==1 || board[i][j]==2)
                            || !turn && (board[i][j]==3 || board[i][j]==4))
                    {
                        legalMovesField=calcolaMosse(firstMove, i, j);
                        if(firstMove || i == yLand && j == xLand){
                            for(int ii=0; ii<8; ii++) {
                                for(int jj=0; jj<8; jj++) {
                                    if(legalMovesField[ii][jj]) {
                                        possibleMoves[pos][0]=i;
                                        possibleMoves[pos][1]=j;
                                        possibleMoves[pos][2]=ii;
                                        possibleMoves[pos][3]=jj;
                                        pos++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int i=0; i<48; i++) {
                Board_PvA prova = new Board_PvA(board);
                if(possibleMoves[i][0]!= 0
                        ||possibleMoves[i][1]!= 0
                        ||possibleMoves[i][2]!= 0
                        ||possibleMoves[i][3]!= 0) {

                    theMoveWasACapture=false;
                    xLand=9;
                    yLand=9;
                    //posizionare la nuova pedina
                    prova.setCasa(possibleMoves[i][2], possibleMoves[i][3], prova.getCasa(possibleMoves[i][0], possibleMoves[i][1]));
                    //rimuovere la vecchia pedina
                    prova.setCasa(possibleMoves[i][0], possibleMoves[i][1], 0);
                    //rimuovere eventuali pedine catturate
                    if(possibleMoves[i][0]-possibleMoves[i][2]==2 || possibleMoves[i][0]-possibleMoves[i][2]==-2) {
                        prova.setCasa((possibleMoves[i][0]+possibleMoves[i][2])/2, (possibleMoves[i][1]+possibleMoves[i][3])/2, 0);
                        theMoveWasACapture=true;
                    }

                    //promozione (?)
                    for (int ii=0; ii<8; ii++) {
                        if(prova.getCasa(0, ii)==1) {
                            prova.setCasa(0, ii, 2);
                        }
                        if(prova.getCasa(7, ii)==3) {
                            prova.setCasa(7, ii, 4);
                        }
                    }

                    //se siamo arrivati a questo punto dobbiamo valutare la prossima mossa. firstMove sarà vero o no?
                    //dipende: se abbiamo fatto una cattura e possiamo fare con la stessa pedina un'altra cattura si, se no no.
                    //cambia anche il passaggio del turno

                    if(theMoveWasACapture) {
                        legalMovesField=prova.calcolaMosse(false, possibleMoves[i][2], possibleMoves[i][3]);
                        boolean atLeastOneMove=false;
                        for(int ii=0; ii<8; ii++) {
                            for(int jj=0; jj<8; jj++) {
                                if (legalMovesField[ii][jj]) {
                                    atLeastOneMove = true;
                                    break;
                                }
                            }
                        }
                        if(atLeastOneMove) {
                            firstMove=false;
                            yLand=possibleMoves[i][2];
                            xLand=possibleMoves[i][3];
                        }
                        else {
                            firstMove=true;
                            turn=!turn;
                        }
                    }



                    if(i==0) {
                        evaluation=prova.evaluatePosition(!turn, firstMove, depth+1, yLand, xLand);
                    }
                    //la valutazione dipende dal turno (a seconda bisogna cercare il minimo o il massimo)
                    if(turn) {
                        if(prova.evaluatePosition(false, firstMove, depth+1, yLand, xLand)>evaluation) {
                            evaluation=prova.evaluatePosition(true, firstMove, depth+1, yLand, xLand);
                        }

                    }
                    else {
                        if(prova.evaluatePosition(true, firstMove, depth+1, yLand, xLand)<evaluation) {
                            evaluation=prova.evaluatePosition(false,firstMove, depth+1, yLand, xLand);
                        }

                    }
                }

            }
        }
        return evaluation;
    }

    // ---------------------------------------- Initialization ---------------------------------------------------------

    public void fillBoard() {

        //tutto vuoto
        for(int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                board[i][j] = 0;
            }
        }

        //bianchi
        board[7][0] = 1;
        board[7][2] = 1;
        board[7][4] = 1;
        board[7][6] = 1;

        board[6][1] = 1;
        board[6][3] = 1;
        board[6][5] = 1;
        board[6][7] = 1;

        board[5][0] = 1;
        board[5][2] = 1;
        board[5][4] = 1;
        board[5][6] = 1;

        //rossi
        board[0][1] = 3;
        board[0][3] = 3;
        board[0][5] = 3;
        board[0][7] = 3;

        board[2][1] = 3;
        board[2][3] = 3;
        board[2][5] = 3;
        board[2][7] = 3;

        board[1][0] = 3;
        board[1][2] = 3;
        board[1][4] = 3;
        board[1][6] = 3;
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
        hitBoxes = new Rectangle[9][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                hitBoxes[i][j] = new Rectangle(
                        (int) ((j * tileSize + xOffset) * scaleFactor), (int) ((i * tileSize + yOffset) * scaleFactor),
                        (int) (tileSize * scaleFactor),  (int) (tileSize * scaleFactor));
    }

    // ---------------------------------------- Utilities --------------------------------------------------------------

    public int getCasa(int i, int j) {
        return board[i][j];
    }

    public void setCasa(int i, int j, int value) {
        board[i][j] = value;
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

    // ---------------------------------------- Mouse Listener ---------------------------------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int[] target = findCell(e.getX(), e.getY());

        if (isWhiteTurn) {
            if (multiEat != null && target != null) {
                if (hintMap[target[0]][target[1]]) {
                    // create new pin
                    board[target[0]][target[1]] = board[multiEat[0]][multiEat[1]];
                    // remove old pin
                    board[multiEat[0]][multiEat[1]] = 0;
                    // eat
                    if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 1)
                        GamePanel.whiteRemovedPins++;
                    if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 2)
                        GamePanel.whiteRemovedKings++;
                    if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 3)
                        GamePanel.redRemovedPins++;
                    if (board[(target[0] + current[0]) / 2][(target[1] + current[1]) / 2] == 4)
                        GamePanel.redRemovedKings++;
                    board[(target[0] + multiEat[0]) / 2][(target[1] + multiEat[1]) / 2] = 0;

                    for (int i = 0; i < 8; i++)
                        if (board[0][i] == 1)
                            board[0][i] = 2;
                    for (int i = 0; i < 8; i++)
                        if (board[7][i] == 3)
                            board[7][i] = 4;

                    hintMap = calcolaMosse(false, target[0],target[1]);
                    int possibleMoves = 0;
                    for (int i = 0; i < 8; i++)
                        for (int j = 0; j < 8; j++)
                            if (hintMap[i][j])
                                possibleMoves++;

                    if (possibleMoves > 0)
                        multiEat = new int[]{target[0], target[1]};
                    else {
                        multiEat = null;
                        int win = checkWin(isWhiteTurn);
                        isWhiteTurn = !isWhiteTurn;

                        if (win == 1) {
                            GamePanel.winner = "white";
                            Main.switchGameState(3, 2);
                        }
                        if (win == -1) {
                            GamePanel.winner = "red";
                            Main.switchGameState(3, 2);
                        }
                    }
                }
            }
            else if (hintMap != null && target != null) {
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
                        for (int i = 0; i < 8; i++)
                            if (board[0][i] == 1)
                                board[0][i] = 2;
                        for (int i = 0; i < 8; i++)
                            if (board[7][i] == 3)
                                board[7][i] = 4;

                        hintMap = calcolaMosse(false, target[0],target[1]);
                        int possibleMoves = 0;
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++)
                                if (hintMap[i][j])
                                    possibleMoves++;

                        if (possibleMoves > 0) {
                            multiEat = new int[]{target[0], target[1]};
                        }
                    }

                    if (multiEat == null) {
                        int win = checkWin(isWhiteTurn);
                        isWhiteTurn = !isWhiteTurn;
                        hintMap = null;

                        if (win == 1) {
                            GamePanel.winner = "white";
                            Main.switchGameState(3, 2);
                        }
                        if (win == -1) {
                            GamePanel.winner = "red";
                            Main.switchGameState(3, 2);
                        }
                    }
                }
                else {
                    if (board[target[0]][target[1]] == 1 || board[target[0]][target[1]] == 2) {
                        hintMap = calcolaMosse(true, target[0], target[1]);
                        current = new int[]{target[0], target[1]};
                    }
                }
            }
            else if (target != null && board[target[0]][target[1]] != 0) {
                if (board[target[0]][target[1]] == 1 || board[target[0]][target[1]] == 2) {
                    hintMap = calcolaMosse(true, target[0], target[1]);
                    current = new int[]{target[0], target[1]};
                }
            }
            else {
                if (multiEat == null)
                    hintMap = null;
            }
        }
        if (!isWhiteTurn && multiEat == null) {
            int[] move = bestMove(false, true);
            //posizionare la nuova pedina
            setCasa(move[2], move[3], getCasa(move[0], move[1]));
            //rimuovere la vecchia pedina
            setCasa(move[0], move[1], 0);
            //rimuovere eventuali pedine catturate
            if(move[0]- move[2]==2 || move[0]- move[2]==-2) {
                if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 1)
                    GamePanel.whiteRemovedPins++;
                if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 2)
                    GamePanel.whiteRemovedKings++;
                if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 3)
                    GamePanel.redRemovedPins++;
                if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 4)
                    GamePanel.redRemovedKings++;
                setCasa((move[2]+ move[0])/2, (move[3]+ move[1])/2, 0);
            }

            //promozione (?)
            for (int i=0; i<8; i++) {
                if(getCasa(0, i)==1) {
                    setCasa(0, i, 2);
                }
                if(getCasa(7, i)==3) {
                    setCasa(7, i, 4);
                }
            }


            //catture a catena
            while(move[0]- move[2]==2 || move[0]- move[2]==-2) {
                move[0]= move[2];
                move[1]= move[3];

                hintMap = calcolaMosse(false, move[0], move[1]);
                boolean onePossibleMove=false;
                for(int i=0; i<8; i++) {
                    for (int j=0; j<8; j++) {
                        if (hintMap[i][j]) {
                            onePossibleMove = true;
                            break;
                        }
                    }
                }
                if(onePossibleMove) {
                    move =bestMove(isWhiteTurn, false);

                    //posizionare la nuova pedina
                    setCasa(move[2], move[3], getCasa(move[0], move[1]));
                    //rimuovere la vecchia pedina
                    setCasa(move[0], move[1], 0);
                    //rimuovere eventuali pedine catturate
                    if(move[0]- move[2]==2 || move[0]- move[2]==-2) {
                        if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 1)
                            GamePanel.whiteRemovedPins++;
                        if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 2)
                            GamePanel.whiteRemovedKings++;
                        if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 3)
                            GamePanel.redRemovedPins++;
                        if (board[(move[2]+ move[0])/2][(move[3]+ move[1])/2] == 4)
                            GamePanel.redRemovedKings++;

                        setCasa((move[2]+ move[0])/2, (move[3]+ move[1])/2, 0);
                    }
                    //promozione (?)
                    for (int i=0; i<8; i++) {
                        if(getCasa(0, i)==1) {
                            setCasa(0, i, 2);
                        }
                        if(getCasa(7, i)==3) {
                            setCasa(7, i, 4);
                        }
                    }
                }
                else {
                    break;
                }

            }

            int win = checkWin(isWhiteTurn);

            if (win == 1) {
                GamePanel.winner = "white";
                Main.switchGameState(3, 2);
            }
            if (win == -1) {
                GamePanel.winner = "red";
                Main.switchGameState(3, 2);
            }

            isWhiteTurn = !isWhiteTurn;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}