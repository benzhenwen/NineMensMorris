package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Global.DISPLAYMODE;
import main.Global.SPRITE;
import main.Global.TOKENTYPE;
import main.Global.NEXTACTIONTYPE;

/* handles the game and display allowing communication between the two */

public class Handler {

    private DISPLAYMODE displayMode;

    private GameHandler gameHandler;
    private Frame frame;
    private int selectedTokenLocation = -1;


    private NEXTACTIONTYPE nextAction;
    private int totalPlacedTokens = 0;


    private boolean showMills, showPossibleMoves;
    private int highlightMillWidth, highlightMovesWidth;
    private Color highlightMillColor, highlightMovesColor;


    public Handler() {

        this.gameHandler = new GameHandler(SPRITE.TOKEN_GREEN, SPRITE.TOKEN_BLACK, TOKENTYPE.A); // eventually replace

        frame = new Frame(this);
        displayMode = DISPLAYMODE.GAME;
        nextAction = NEXTACTIONTYPE.PLACE_TOKEN;

        showMills = true;
        showPossibleMoves = true;
        highlightMillWidth = 8;
        highlightMillColor = Color.green;
        highlightMovesWidth = 5;
        highlightMovesColor = Color.cyan;

    }

    // on mouse pressed
    public void mousePressed(MouseEvent e) {
        int clickedTile = getClickTile(e);
        if (displayMode == DISPLAYMODE.GAME) {
            switch(nextAction) {
                case PLACE_TOKEN:
                    if(clickedTile != -1 && gameHandler.canPlaceToken(clickedTile)) {
                        gameHandler.placeToken(clickedTile);
                        totalPlacedTokens++;
                        if(totalPlacedTokens >= 18) {
                            if(gameHandler.hasWon()) {
                                System.out.println("win");
                                displayMode = DISPLAYMODE.WIN;
                                break;
                            }
                            nextAction = NEXTACTIONTYPE.SELECT_TOKEN;
                        }
                        if(gameHandler.getRecentChain()) nextAction = NEXTACTIONTYPE.REMOVE_TOKEN;
                        else gameHandler.togglePlayer();
                    }
                    break;

                case REMOVE_TOKEN:
                    if(clickedTile != -1 && gameHandler.canRemoveToken(clickedTile)) {
                        gameHandler.removeToken(clickedTile);
                        if(totalPlacedTokens >= 18) {
                            if(gameHandler.hasWon()) {
                                displayMode = DISPLAYMODE.WIN;
                                break;
                            }
                            nextAction = NEXTACTIONTYPE.SELECT_TOKEN;
                        }
                        else nextAction = NEXTACTIONTYPE.PLACE_TOKEN;
                        gameHandler.togglePlayer();
                    }
                    break;

                case SELECT_TOKEN:
                    if(clickedTile != -1 && gameHandler.canMoveToken(clickedTile)) {
                        selectedTokenLocation = clickedTile;
                        nextAction = NEXTACTIONTYPE.SELECT_MOVE_LOCATION;
                    }
                    break;

                case SELECT_MOVE_LOCATION:
                    if(clickedTile != -1 && gameHandler.isMovePossible(selectedTokenLocation, clickedTile)) {
                        gameHandler.makeMove(selectedTokenLocation, clickedTile);
                        selectedTokenLocation = -1;
                        if(gameHandler.hasWon()) {
                            displayMode = DISPLAYMODE.WIN;
                            break;
                        }
                        if (gameHandler.getRecentChain()) nextAction = NEXTACTIONTYPE.REMOVE_TOKEN;
                        else {
                            nextAction = NEXTACTIONTYPE.SELECT_TOKEN;
                            gameHandler.togglePlayer();
                        }
                    }
                    else {
                        selectedTokenLocation = -1;
                        nextAction = NEXTACTIONTYPE.SELECT_TOKEN;
                    }
                    break;

                default:
                    System.out.println("oh something has probably gone very wrong. end switch statement of mousePressed in Handler");
            }
        }

        if(displayMode == DISPLAYMODE.WIN) {
            if(clickedTile == 0) {
                // go to start screen eventually
                System.exit(0);
            }
        }

        draw(); // update frame
    }

    private int getClickTile(MouseEvent e) {
        int width = frame.getPanel().getWidth();
        int height = frame.getPanel().getHeight();
        int widthMargin = Math.max(width - (int) (height*1.4), 0);
        int heightMargin = Math.max(height - (int) (width/1.4), 0);
        double boardSize = Math.min((width-widthMargin/2.0)/1.4, (height-heightMargin/2.0));
        double boardScale = 1000.0/boardSize;

        double scaledX = (e.getX() - widthMargin/2.0) * boardScale;
        double scaledY = (e.getY() - heightMargin/2.0) * boardScale;

        double maxDistance = boardSize / 15;

        for(int i = 0; i < 24; i++) {
            Coordinate potentialTile = Global.gridSpaceCoordinates[i];
            double clickDist = calculateDistance(scaledX, scaledY, potentialTile.x, potentialTile.y );

            if(clickDist <= maxDistance) {
                return i;
            }
        }
        return -1;
    }

    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }


    // drawing graphical ui

    public void draw() { // draws to the panel on frame
        switch(displayMode) {
            case GAME:
                frame.getPanel().setLastFrame(drawGame(frame.getPanel().getWidth(), frame.getPanel().getHeight()));
                break;
            case WIN:
                frame.getPanel().setLastFrame(drawGameWin(frame.getPanel().getWidth(), frame.getPanel().getHeight()));
                break;
            default:
        }
        frame.getPanel().repaint();
    }

    // draws the board and menu/info
    private BufferedImage drawGameWin(int width, int height) {

        BufferedImage fullDisplay = new BufferedImage(1400, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics g = fullDisplay.getGraphics();
        g.drawImage(drawGameBoard(), 0, 0, null);
        g.drawImage(drawGameSideUI(), 1000, 0, null);
        g.drawImage(Global.getSprite(SPRITE.WIN_OVERLAY), 0, 0, null);

        int widthMargin = Math.max(width - (int) (height*1.4), 0);
        int heightMargin = Math.max(height - (int) (width/1.4), 0);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        output.getGraphics().drawImage(fullDisplay, widthMargin/2, heightMargin/2, width-widthMargin, height-heightMargin, null);
        return output;

    }

    private BufferedImage drawGame(int width, int height) {

        BufferedImage fullDisplay = new BufferedImage(1400, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics g = fullDisplay.getGraphics();
        g.drawImage(drawGameBoard(), 0, 0, null);
        g.drawImage(drawGameSideUI(), 1000, 0, null);

        int widthMargin = Math.max(width - (int) (height*1.4), 0);
        int heightMargin = Math.max(height - (int) (width/1.4), 0);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        output.getGraphics().drawImage(fullDisplay, widthMargin/2, heightMargin/2, width-widthMargin, height-heightMargin, null);
        return output;

    }

    private BufferedImage drawGameSideUI() {
        BufferedImage output = new BufferedImage(400, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) output.getGraphics();


        // draw border
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(10));
        g2.drawRect(0, 0, 400, 1000);

        // text: to move
        g2.setFont(new Font("Ariel", Font.PLAIN, 60)); // mod font size here
        g2.drawImage(Global.getSprite(gameHandler.getSprite(gameHandler.getCurrentPlayer())), 20, 20, 100, 100, null);
        g2.drawString("to move", 140, 90);

        // text: next action
        g2.setFont(new Font("Ariel", Font.PLAIN, 30)); // mod font size here
        String actionLine = "";
             if(nextAction == NEXTACTIONTYPE.PLACE_TOKEN)          actionLine = "Place a token";
        else if(nextAction == NEXTACTIONTYPE.REMOVE_TOKEN)         actionLine = "Remove a token";
        else if(nextAction == NEXTACTIONTYPE.SELECT_TOKEN)         actionLine = "Select token to move";
        else if(nextAction == NEXTACTIONTYPE.SELECT_MOVE_LOCATION) actionLine = "select location to move";
        g2.drawString(actionLine, 20, 200);

        return output;
    }

    // draws the game board and all the pieces
    private BufferedImage drawGameBoard() {

        BufferedImage boardImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) boardImage.getGraphics();

        int tokenSize = 80;

        g2.drawImage(Global.getSprite(SPRITE.BOARD), 0, 0, null);

        for(DataPair<Integer, TOKENTYPE> intTypePair : gameHandler.getTokenLocations()) {
            int xPos = Global.gridSpaceCoordinates[intTypePair.A].x - tokenSize/2;
            int yPos = Global.gridSpaceCoordinates[intTypePair.A].y - tokenSize/2;
            SPRITE sprite = gameHandler.getSprite(intTypePair.B);

            g2.drawImage(Global.getSprite(sprite), xPos, yPos, tokenSize, tokenSize, null);
        }

        if(showMills) {
            g2.setColor(highlightMillColor);
            g2.setStroke(new BasicStroke(highlightMillWidth)); // stroke width

            for(DataPair<Integer, Integer> intintPair : gameHandler.getChainLines()) {
                int x1 = Global.gridSpaceCoordinates[intintPair.A].x;
                int y1 = Global.gridSpaceCoordinates[intintPair.A].y;
                int x2 = Global.gridSpaceCoordinates[intintPair.B].x;
                int y2 = Global.gridSpaceCoordinates[intintPair.B].y;
                g2.drawLine(x1, y1, x2, y2);
            }
        }

        if(showPossibleMoves) {
            g2.setColor(highlightMovesColor);
            g2.setStroke(new BasicStroke(highlightMovesWidth)); // stroke width
            switch(nextAction) {
                case PLACE_TOKEN:          for (int i = 0; i < 24; i++) if (gameHandler.canPlaceToken(i))                         drawCircle(g2, i, tokenSize); break;
                case REMOVE_TOKEN:         for (int i = 0; i < 24; i++) if (gameHandler.canRemoveToken(i))                        drawCircle(g2, i, tokenSize); break;
                case SELECT_TOKEN:         for (int i = 0; i < 24; i++) if (gameHandler.canMoveToken(i))                          drawCircle(g2, i, tokenSize); break;
                case SELECT_MOVE_LOCATION: for (int i = 0; i < 24; i++) if (gameHandler.isMovePossible(selectedTokenLocation, i)) drawCircle(g2, i, tokenSize); break;
                default: System.out.println("unknown move state in GameHandler drawGameBoard()");
            }
        }

        return boardImage;
    }

    private void drawCircle(Graphics2D g2, int boardIndex, int circleSize) {
        int x = Global.gridSpaceCoordinates[boardIndex].x - circleSize/2;
        int y = Global.gridSpaceCoordinates[boardIndex].y - circleSize/2;
        g2.drawArc(x, y, circleSize, circleSize, 0, 360);
    }

}
