package main;

import main.Global.TOKENTYPE;

/* token class to represent a physical token on the board. can read and write the type of token through TOKENTYPE */

public class Token {

    private TOKENTYPE tokenType;
    
    public Token(TOKENTYPE tokenType) {
        setTokenType(tokenType);
    }

    public TOKENTYPE getTokenType() {
        return tokenType;
    }

    public void setTokenType(TOKENTYPE t) {
        tokenType = t;
    }

}
