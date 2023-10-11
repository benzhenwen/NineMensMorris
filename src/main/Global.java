package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/* contains things like enum types to share across classes */

public class Global {
    
    public enum TOKENTYPE {A, B}; // token types for each player. one white one black.
    public enum DISPLAYMODE {START, GAME, WIN};
    public enum NEXTACTIONTYPE {SELECT_TOKEN, SELECT_MOVE_LOCATION, PLACE_TOKEN, REMOVE_TOKEN}
    public enum SPRITE {BOARD, TOKEN_BLUE, TOKEN_BLACK, TOKEN_GREEN, TOKEN_ORANGE, TOKEN_PURPLE, TOKEN_RED, TOKEN_YELLOW, WIN_OVERLAY}

    public static final Coordinate[] gridSpaceCoordinates = {
        new Coordinate(75 , 75 ),
        new Coordinate(500, 75 ),
        new Coordinate(925, 75 ),
        new Coordinate(221, 221),
        new Coordinate(500, 221),
        new Coordinate(779, 221),
        new Coordinate(368, 368),
        new Coordinate(500, 368),
        new Coordinate(632, 368),
        new Coordinate(75 , 500),
        new Coordinate(221, 500),
        new Coordinate(368, 500),
        new Coordinate(632, 500),
        new Coordinate(779, 500),
        new Coordinate(925, 500),
        new Coordinate(368, 632),
        new Coordinate(500, 632),
        new Coordinate(632, 632),
        new Coordinate(221, 779),
        new Coordinate(500, 779),
        new Coordinate(779, 779),
        new Coordinate(75 , 925),
        new Coordinate(500, 925),
        new Coordinate(925, 925)
    };

    private static Map<SPRITE, BufferedImage> iconMap = new HashMap<SPRITE, BufferedImage>();

    static {
        try{
            String spritePath = new File(".").getCanonicalPath() + "\\sprites\\";
            System.out.println(spritePath);

            // icon loading
            iconMap.put(SPRITE.BOARD,        ImageIO.read(new File(spritePath + "board.jpg"       )));
            iconMap.put(SPRITE.TOKEN_BLUE,   ImageIO.read(new File(spritePath + "token_blue.png"  )));
            iconMap.put(SPRITE.TOKEN_BLACK,  ImageIO.read(new File(spritePath + "token_black.png" )));
            iconMap.put(SPRITE.TOKEN_GREEN,  ImageIO.read(new File(spritePath + "token_green.png" )));
            iconMap.put(SPRITE.TOKEN_ORANGE, ImageIO.read(new File(spritePath + "token_orange.png")));
            iconMap.put(SPRITE.TOKEN_PURPLE, ImageIO.read(new File(spritePath + "token_purple.png")));
            iconMap.put(SPRITE.TOKEN_RED,    ImageIO.read(new File(spritePath + "token_red.png"   )));
            iconMap.put(SPRITE.TOKEN_YELLOW, ImageIO.read(new File(spritePath + "token_yellow.png")));
            iconMap.put(SPRITE.WIN_OVERLAY , ImageIO.read(new File(spritePath + "win.png"         )));
        }
        catch(Exception e) {
            System.out.println("file loading failed");
            System.out.println(iconMap);
        };
    }

    public static BufferedImage getSprite(SPRITE s) {
        return iconMap.get(s);
    }

    public static final int[][] chainsNumbers = {
        // horizontal sets
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        {9, 10, 11},
        {12, 13, 14},
        {15, 16, 17},
        {18, 19, 20},
        {21, 22, 23},
        // vertical sets
        {0, 9, 21},
        {3, 10, 18},
        {6, 11, 15},
        {1, 4, 7},
        {16, 19, 22},
        {8, 12, 17},
        {5, 13, 20},
        {2, 14, 23}
    };

}
