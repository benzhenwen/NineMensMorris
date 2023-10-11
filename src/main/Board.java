package main;

/* the Board object contains all the Space objects 
 * contains Chains
 * 
 * board layout:
 * 
 * 7   0***********1***********2
 *     *           *           *
 * 6   *   3*******4*******5   *
 *     *   *       *       *   *
 * 5   *   *   6***7***8   *   *
 *     *   *   *       *   *   *
 * 4   9***10**11      12**13**14
 *     *   *   *       *   *   *    
 * 3   *   *   15**16**17  *   *
 *     *   *       *       *   *
 * 2   *   18******19******20  *
 *     *           *           *
 * 1   21**********22**********23
 * 
 * -   a   b   c   d   e   f   g
*/

public class Board {
    
    private final Space[] boardSpaces = new Space[24];
    private final Chain[] chains = new Chain[Global.chainsNumbers.length];

    public Board() {

        for(int i = 0; i < boardSpaces.length; i++) boardSpaces[i] = new Space(); // init all spaces

        for(int i = 0; i < chains.length; i++) { // init all chains
            chains[i] = new Chain(
                boardSpaces[Global.chainsNumbers[i][0]], 
                boardSpaces[Global.chainsNumbers[i][1]], 
                boardSpaces[Global.chainsNumbers[i][2]]
            );
        }

    }

    // basic accessors

    public int getChainCount() {
        return chains.length;
    }

    public Space getSpace(int i) {
        return boardSpaces[i];
    }

    public Chain getChain(int i) {
        return chains[i];
    }

}
