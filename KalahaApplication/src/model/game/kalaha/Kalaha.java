package model.game.kalaha;

import model.game.IEvaluate;
import model.game.IGame;

import java.util.ArrayList;
import java.util.List;

public class Kalaha implements IEvaluate, IGame<Kalaha> {
    private static final int DEFAULT_STONES_AMOUNT = 4;

    private final Board board;
    private int lastMoveResult = 404;
    private int lastMoveHoleNumber = -1;
    private int lastHoleIndex;

    public Kalaha(int stonesAmountInHole){
        board = new Board();
        board.initBoard(stonesAmountInHole);
        lastHoleIndex = -1;
    }

    public Kalaha() {
        board = new Board();
        board.initBoard(DEFAULT_STONES_AMOUNT);
    }

    public Kalaha(Kalaha kalaha) {
        this.board = new Board(kalaha.board);
        this.lastMoveResult = kalaha.lastMoveResult;
        this.lastMoveHoleNumber = kalaha.lastMoveHoleNumber;
        this.lastHoleIndex = kalaha.lastHoleIndex;
    }

    public int[] getBoard() {
        return board.getBoard();
    }

    public int getLastHoleIndex() {
        return lastHoleIndex;
    }

    public void printBoard() {
        board.printBoard();
    }

    public boolean checkStopCondition() {
        return board.checkStopCondition();
    }

    public void collectRestOfStones() {
        board.collectRestOfStones();
    }

    /**
     * @param holeNum     values [1,6]          if not returns true
     * @param whichPlayer values 0 or 1         if not returns true
     * @return false if hole has stones
     */
    public boolean holeIsEmpty(int holeNum, int whichPlayer) {
        return board.holeIsEmpty(holeNum, whichPlayer);
    }

    /**
     * @param whichPlayer needs to be 0 or 1
     * @return -1 when there is wrong param value
     * @return stones amount in opponent base
     */
    public int getStonesAmount(int whichPlayer) {
        if (whichPlayer != 0 && whichPlayer != 1) {
            return -1;
        }

        if (whichPlayer == 0) {
            return board.getBoard()[(board.getBoard().length / 2) - 1]; // 6
        }
        return board.getBoard()[board.getBoard().length - 1]; // 13
    }

    /**
     * @param whichPlayer needs to be 0 or 1
     * @return -1 when there is wrong param value
     * @return stones amount in opponent base
     */
    public int getOpponentStonesAmount(int whichPlayer) {
        if (whichPlayer != 0 && whichPlayer != 1) {
            return -1;
        }

        return getStonesAmount((whichPlayer + 1) % 2);
    }

    public int getLastMoveResult() {
        return lastMoveResult;
    }

    @Override
    public int getLastMoveNumber() {
        return lastMoveHoleNumber;
    }

    /**
     * @param holeNum     needs to be [1, 6]
     * @param whichPlayer needs to be 0 or 1
     * @return -1 when there is wrong param value
     * @return 1  when player got additional move
     * @return 2  when player steals opponent's stones
     * @return 0  otherwise
     */
    public int move(int holeNum, int whichPlayer) {
        lastMoveResult = moveStonesAndGetResult(holeNum, whichPlayer);
        lastMoveHoleNumber = holeNum;
        return lastMoveResult;
    }

    private int moveStonesAndGetResult(int holeNum, int whichPlayer) {
        int condition = board.checkLastHoleConditions(holeNum, whichPlayer);

        if (condition < 0) {
//            printLastHoleConditionResult(condition);
            return -1;
        }

        int result = board.moveStones(holeNum, whichPlayer);
        lastHoleIndex = result;
        if (condition == 2) {
            board.emptyHole(result);
            return 2;
        } else {
            if (condition == 1) {
                return 1;
            }
        }

        return 0;
    }

    private void printLastHoleConditionResult(int condition) {
        System.out.print("Wrong argument -");
        if (condition == -1) {
            System.out.println(" number of hole.");
        } else {
            if (condition == -2) {
                System.out.println(" player number.");
            } else {
                System.out.println(" hole is empty.");
            }
        }
    }

    /**
     * @return -1 when nobody won yet
     * @return 0  when first player won
     * @return 1  when second player won
     * @return 2  when draw
     */
    public int whichPlayerWon() {
        return board.whichPlayerWon();
    }

    @Override
    public int evaluate(int playerNumber) {
        return getStonesAmount(playerNumber) - getStonesAmount((playerNumber + 1) % 2);
    }

    @Override
    public boolean gameOver() {
        return this.checkStopCondition();
    }

    @Override
    public boolean isFirstMove() {
        return lastMoveResult == 404;
    }

    @Override
    public List<Kalaha> getAllChildren(int playerNumber) {
        List<Kalaha> children = new ArrayList<>();
        for (int i = 0; i < board.getOnePlayerHolesAmount(); i++) {
            Kalaha child = new Kalaha(this);
            int holeNumber = i + 1;
            if (!holeIsEmpty(holeNumber, playerNumber)) {
                child.move(holeNumber, playerNumber);
                children.add(child);
            }
        }
        return children;
    }
}
