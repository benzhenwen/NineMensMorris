package main;

import main.Global.TOKENTYPE;

/* 27 Spaces on the Board, each can contain a Token but can also be empty. 
 * can add Tokens to the Space or remove Tokens from the Space. can also set the Token object directly.
 * can return the Token object and also tell if the Space is occupied or not. 
 */

public class Space {
    
    private Token token = null;

    public Space() {

    }

    public void addToken(TOKENTYPE t) {
        token = new Token(t);
    }

    public void setToken(Token t) {
        token = t;
    }

    public void removeToken() {
        token = null;
    }

    public boolean isEmpty() {
        return token == null;
    }

    public Token getToken() {
        return token;
    }

}
