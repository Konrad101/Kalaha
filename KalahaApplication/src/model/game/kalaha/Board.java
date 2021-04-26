package model.game.kalaha;

public class Board {
    private final int[] board;

    public Board() {
        board = new int[14];
    }

    Board(Board boardObject){
        board = new int[boardObject.board.length];
        System.arraycopy(boardObject.board, 0, board, 0, boardObject.board.length);
    }

    int[] getBoard(){
        return board;
    }

    int getOnePlayerHolesAmount(){
        return (board.length - 2) / 2;
    }

    void initBoard(int stonesInHole) {
        for (int i = 0; i < board.length; i++) {
            if (i != 6 && i != 13) {
                board[i] = stonesInHole;
            } else {
                board[i] = 0;
            }
        }
    }

    void printBoard() {
        System.out.println();

        // first  player base on  6 index
        // second player base on 13 index
        int index = board.length - 2;
        for (int i = 0; i < 6; i++) {
            System.out.print("\t" + board[index]);
            index--;
        }

        System.out.println("\n" + board[board.length - 1] + "\t\t\t\t\t\t\t" + board[index]);
        index = 0;

        for (int i = 0; i < 6; i++) {
            System.out.print("\t" + board[index]);
            index++;
        }
        System.out.println();
    }

    boolean checkStopCondition(int playerNumber) {
        boolean emptyPlayerHoles = true;

        int index = playerNumber == 0 ? 0 : this.getOnePlayerHolesAmount() + 1;
        for (int j = 0; j < getOnePlayerHolesAmount(); j++) {
            if (board[index] != 0) {
                emptyPlayerHoles = false;
            }

            index++;
        }

        return emptyPlayerHoles;
    }

    /**
     * @param holeNum     values [1,6]          if not returns true
     * @param whichPlayer values 0 or 1         if not returns true
     * @return false if hole has stones*/
    boolean holeIsEmpty(int holeNum, int whichPlayer){
        if(holeNum < 1 || holeNum > 6 || whichPlayer < 0 || whichPlayer > 1){
            return true;
        }

        int index;
        if(whichPlayer == 0){
            index = holeNum - 1;
        }else{
            index = holeNum + 6;
        }

        return board[index] == 0;
    }

    /**
     * @param holeNum     values [1,6]          if not returns -1
     * @param whichPlayer values 0 or 1         if not returns -2
     * @return -3 when hole is empty, otherwise number of checked condition */
    int checkLastHoleConditions(int holeNum, int whichPlayer) {
        if (holeNum < 1 || holeNum > 6)
            return -1;
        if (whichPlayer != 0 && whichPlayer != 1)
            return -2;

        int startIndex;
        int indexOfOpponentBase;

        if (whichPlayer == 0) {
            startIndex = holeNum - 1;
            indexOfOpponentBase = board.length - 1;
        } else {
            startIndex = holeNum + 6;
            indexOfOpponentBase = 6;
        }

        if (board[startIndex] == 0)
            return -3;

        int lastHoleIndex = startIndex;
        for (int i = 0; i < board[startIndex]; i++) {
            lastHoleIndex++;
            if (lastHoleIndex == indexOfOpponentBase) {
                lastHoleIndex++;
            }

            if (lastHoleIndex == board.length) {
                lastHoleIndex = 0;
            }
        }

        if (whichPlayer == 0) {
            if (lastHoleIndex == (board.length / 2) - 1 ) {
                return 1;
            }
            if (lastHoleIndex < 6 && board[lastHoleIndex] == 0 && board[opponentHoleIndex(lastHoleIndex)] != 0) {
                return 2;
            }
        } else {
            if (lastHoleIndex == board.length - 1) {
                return 1;
            }
            if (lastHoleIndex > 6 && board[lastHoleIndex] == 0 && board[opponentHoleIndex(lastHoleIndex)] != 0) {
                return 2;
            }
        }

        return 0;
    }

    private int opponentHoleIndex(int holeIndex){
        int opponentIndex;
        if(holeIndex < 6){
            opponentIndex = 12 - holeIndex;
        } else {
            opponentIndex = holeIndex;
            for(int i = holeIndex ; i > 6 ; --i){
                opponentIndex -= 2;
            }
        }
        return opponentIndex;
    }

    /**
     * method is collecting stones from player and opponent's hole
     * when board at index of last hole was empty */
    void emptyHole(int indexOfLastHole) {
        if (indexOfLastHole != 6 && indexOfLastHole != (board.length - 1)) {
            int opponentIndex;
            opponentIndex = 12 - indexOfLastHole;

            int stonesAmount = board[opponentIndex];
            if (stonesAmount > 0) {
                board[opponentIndex] = 0;
                int baseIndex;

                if (indexOfLastHole < 6) {
                    baseIndex = 6;
                } else {
                    baseIndex = board.length - 1;
                }

                board[baseIndex] += board[indexOfLastHole];
                board[indexOfLastHole] = 0;
                board[baseIndex] += stonesAmount;
            }
        }
    }

    /**
     * @param holeNum     values [1,6]     if not returns -1
     * @param whichPlayer values 0 or 1    if not returns -2
     * @return -3 when hole is empty, otherwise index of last hole */
    int moveStones(int holeNum, int whichPlayer) {
        if (holeNum < 1 || holeNum > 6)
            return -1;

        if (whichPlayer != 0 && whichPlayer != 1)
            return -2;


        int startIndex;
        int indexOfOpponentBase;

        if (whichPlayer == 0) {
            startIndex = holeNum - 1;
            indexOfOpponentBase = board.length - 1;
        } else {
            startIndex = holeNum + 6;
            indexOfOpponentBase = 6;
        }
        if (board[startIndex] == 0)
            return -3;

        int lastHoleNum;
        int index = startIndex + 1;

        int stonesInHole = board[startIndex];
        board[startIndex] = 0;

        for(int i = 0 ; i < stonesInHole; i++){
            if(index == indexOfOpponentBase){
                index++;
            }
            if(index == board.length){
                index = 0;
            }
            board[index]++;
            index++;
        }

        lastHoleNum = index - 1;

        return lastHoleNum;
    }

    /**
     * collects rest of stones when one of players
     * has all holes empty*/
    void collectRestOfStones() {
        boolean holesAreEmpty = true;

        // check for only first player
        int index = 0;
        while (holesAreEmpty && index < 6) {
            if (board[index++] != 0) {
                holesAreEmpty = false;
            }
        }

        int indexOfBase;
        if (holesAreEmpty) {
            // second player collects stones
            indexOfBase = 13;
            index = 7;
        } else {
            // first player collects stones
            indexOfBase = 6;
            index = 0;
        }

        for (int i = 0; i < 6; i++) {
            board[indexOfBase] += board[index];
            board[index++] = 0;
        }
    }

    /**
     * @return current game status code
     * -1  when game isn't over
     *  0  when first player won
     *  1  when second player won
     *  2  when draw */
    int whichPlayerWon() {
        // both of players must have 0 stones in holes
        if (!boardHolesAreEmpty())
            return -1;

        if (board[6] > board[board.length - 1]) {
            return 0;
        } else {
            if (board[6] < board[board.length - 1])
                return 1;
        }

        return 2;
    }

    boolean boardHolesAreEmpty(){
        int holeIndex = 0;
        while (holeIndex != board.length - 1){
            if(holeIndex == getOnePlayerHolesAmount()){
                holeIndex++;
            }
            if(board[holeIndex++] != 0){
                return false;
            }
        }

        return true;
    }
}
