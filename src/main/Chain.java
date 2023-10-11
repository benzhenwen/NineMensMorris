package main;

import main.Global.TOKENTYPE;

/* the Chain object contains 3 spaces each to represent 3 connected Spaces on the board 
 * the Space objects contained in Chains are interlinked with the Space objects on the board
 * can return if the chain is complete (all the same color) or if it is complete taking a TOKENTYPE to match
 */;

public class Chain {

    private Space[] spaces = new Space[3];
    
    public Chain(Space s1, Space s2, Space s3) {
        spaces[0] = s1;
        spaces[1] = s2;
        spaces[2] = s3;
    }

    // useful for finding 3 in a rows
    public boolean isComplete() {
        for(Space space : spaces) { if(space.isEmpty()) return false; }
        
        return spaces[0].getToken().getTokenType() == spaces[1].getToken().getTokenType() && 
               spaces[1].getToken().getTokenType() == spaces[2].getToken().getTokenType();
    }

    public boolean isComplete(TOKENTYPE t) {
        for(Space space : spaces) { if(space.isEmpty()) return false; }

        return spaces[0].getToken().getTokenType() == t && 
               spaces[1].getToken().getTokenType() == t && 
               spaces[2].getToken().getTokenType() == t;
    }

    // useful for finding adjacent spaces
    public boolean contains(Space s) {
        for(Space space : spaces) if(space == s) return true;
        return false;
    }

    public Space[] getAdjacentSpaces(Space s) {
        if(contains(s) == false) return new Space[] {};
        if(s == spaces[1]) return new Space[] {spaces[0], spaces[2]};
        return new Space[] {spaces[1]};
    }

}
