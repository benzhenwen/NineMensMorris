package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import main.Global.SPRITE;
import main.Global.TOKENTYPE;

/* handles game backend aspects like turns, moves, etc */

public class GameHandler {
    
    // player sprites
    private SPRITE p1Sprite;
    private SPRITE p2Sprite;
    private TOKENTYPE currentPlayer;
    private Board board;
    private boolean recentChainA, recentChainB;
    private Set<Chain> madeChains = new HashSet<Chain>();

    public GameHandler(SPRITE p1Sprite, SPRITE p2Sprite, TOKENTYPE startingPlayer) {

        this.board = new Board();

        this.p1Sprite = p1Sprite;
        this.p2Sprite = p2Sprite;
        this.currentPlayer = startingPlayer; 

    }

    // access and change sprites of players
    public void setP1Sprite(SPRITE s) {
        p1Sprite = s;
    }

    public void setP2Sprite(SPRITE s) {
        p2Sprite = s;
    }

    public SPRITE getSprite(TOKENTYPE t) {
        if(t == TOKENTYPE.A) return p1Sprite;
        else                 return p2Sprite;
    }

    public boolean getRecentChain() {
        if(getCurrentPlayer() == TOKENTYPE.A) return recentChainA;
        else                                  return recentChainB;
    }

    // manage player turns
    public TOKENTYPE getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(TOKENTYPE player) {
        currentPlayer = player;
    }

    /**
     * if the player is A, set to B, and vice versa
     */
    public void togglePlayer() {
        currentPlayer = reverseTokenType(currentPlayer);
    }

    // handle moves

    /**
     * determines if a move is available
     * @param startIndex the position to move from
     * @param endIndex   the position to move to
     * @return           is move possible
     */
    public boolean isMovePossible(int startIndex, int endIndex) {
        Space startSpace = board.getSpace(startIndex);
        Space endSpace = board.getSpace(endIndex);

        return isMovePossible(startSpace, endSpace);
    }

    /**
     * performs a move on the board
     * @param startIndex move from
     * @param endIndex   move to
     */
    public void makeMove(int startIndex, int endIndex) {
        Space startSpace = board.getSpace(startIndex);
        Space endSpace = board.getSpace(endIndex);

        endSpace.setToken(startSpace.getToken());
        startSpace.removeToken();

        setRecentChains();
    }

    /**
     * if a space can have a token placed on it
     * @param boardIndex where to place the token
     * @return           can place token
     */
    public boolean canPlaceToken(int boardIndex) {
        return board.getSpace(boardIndex).isEmpty();
    }

    /**
     * places a token on the board with tokentype of the current player
     * @param boardIndex where to place the token
     */
    public void placeToken(int boardIndex) {
        Space space = board.getSpace(boardIndex);
        space.addToken(currentPlayer);

        setRecentChains();
    }

    /**
     * checks if a token can be removed from a tile, does not check for mills
     * @param boardIndex where to check
     * @return           removable
     */
    public boolean canRemoveToken(int boardIndex) {
        Space space = board.getSpace(boardIndex);
        if(space.isEmpty() || space.getToken().getTokenType() == currentPlayer) return false;
        if(getRemainingTokens(reverseTokenType(currentPlayer)) > totalTokensChained(reverseTokenType(currentPlayer))) { // check for if there are only mills left
            if(isSpaceChained(space)) return false;
        }
        return true;
    }

    /**
     * removes a token
     * @param boardIndex where to remove from
     */
    public void removeToken(int boardIndex) {
        Space space = board.getSpace(boardIndex);
        space.removeToken();

        setRecentChains();
    }

    /**
     * checks if a token is movable from its current position and is the current player's token
     * @param boardIndex where to check
     * @return           is there a token that can move
     */
    public boolean canMoveToken(int boardIndex) {
        return canMoveToken(boardIndex, currentPlayer);
    }

    /**
     * if the game has been won. checks if the current player has won.
     * @return won
     */
    public boolean hasWon() {

        // less than 3 tokens left
        if(getRemainingTokens(reverseTokenType(currentPlayer)) < 3) return true;
        // no moves left
        boolean moveLeft = false;
        for(int i = 0; i < 24; i++) {
            if(canMoveToken(i, reverseTokenType(currentPlayer))) moveLeft = true;
        }
        return !moveLeft;
    }


    // needed for graphical interface

    /**
     * gets the amount of tokens remaining for a player
     * @param tokenType type of token to search for
     * @return          amount
     */
    public int getRemainingTokens(TOKENTYPE tokenType) {
        int total = 0;
        for(int i = 0; i < 24; i++) {
            Space space = board.getSpace(i);
            if(!space.isEmpty() && space.getToken().getTokenType() == tokenType) total++;
        }
        return total;
    }

    /**
     * returns a DataPair<Integer, TOKENTYPE>[] that holds the location and type of every token
     * @return an array of every token and its location
     */
    public DataPair<Integer, TOKENTYPE>[] getTokenLocations() {
        ArrayList<DataPair<Integer, TOKENTYPE>> ar = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            Space s = board.getSpace(i);
            if(!s.isEmpty()) {
                ar.add(new DataPair<Integer, TOKENTYPE>(i, s.getToken().getTokenType()));
            }
        }
        return ar.toArray(new DataPair[0]);
    }

    /**
     * gets the start and end indexes of every chain
     * @return DataPair[] of Integers for the start and end of each chain
     */
    public DataPair<Integer, Integer>[] getChainLines() {
        ArrayList<DataPair<Integer, Integer>> ar = new ArrayList<>();
        for(int i = 0; i < board.getChainCount(); i++) {
            if(board.getChain(i).isComplete()) {
                ar.add(new DataPair<Integer, Integer>(Global.chainsNumbers[i][0], Global.chainsNumbers[i][2]));
            }
        }
        return ar.toArray(new DataPair[0]);
    }


    // extra useful stuff

    private boolean isMovePossible(Space startSpace, Space endSpace) {
        if(startSpace.isEmpty() || startSpace.getToken().getTokenType() != currentPlayer) return false;
        if(!isSpaceAdjacent(startSpace, endSpace)) return false;
        return endSpace.isEmpty();
    }

    private boolean isSpaceAdjacent(Space firstSpace, Space secondSpace) {
        for(int i = 0; i < board.getChainCount(); i++) {
            for(Space space : board.getChain(i).getAdjacentSpaces(firstSpace)) {
                if(secondSpace == space) return true;
            }
        }
        return false;
    }

    private boolean isSpaceChained(Space inputSpace) {
        for(int i = 0; i < board.getChainCount(); i++) {
            Chain c = board.getChain(i);
            if(c.isComplete() && c.contains(inputSpace)) return true;
        }
        return false;
    }

    private int totalTokensChained(TOKENTYPE tokenType) {
        int total = 0;
        for(int i = 0; i < 24; i++) {
            Space space = board.getSpace(i);
            if(isSpaceChained(space) && space.getToken().getTokenType() == tokenType) total++;
        }
        return total;
    }

    private void setRecentChains() {
        recentChainA = false;
        recentChainB = false;
        for(int i = 0; i < board.getChainCount(); i++) {
            Chain c = board.getChain(i);
            if(c.isComplete() && !madeChains.contains(c)) {
                if(currentPlayer == TOKENTYPE.A) recentChainA = true;
                else                             recentChainB = true;
                madeChains.add(c);
            }
        }
        madeChains.removeIf(c -> !c.isComplete()); // removes a chain from the set if it is no longer complete
    }

    public boolean canMoveToken(int boardIndex, TOKENTYPE tokenType) {
        Space space = board.getSpace(boardIndex);
        if(space.isEmpty() || space.getToken().getTokenType() != tokenType) return false;
        for(int i = 0; i < 24; i++) {
            Space adjSpace = board.getSpace(i);
            if(isSpaceAdjacent(space, adjSpace) && adjSpace.isEmpty()) return true;
        }
        return false;
    }

    private static TOKENTYPE reverseTokenType(TOKENTYPE tokenType) {
        if(tokenType == TOKENTYPE.A) return TOKENTYPE.B;
        else                         return TOKENTYPE.A;
    }
}
