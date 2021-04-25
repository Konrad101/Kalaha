package model.botAlgorithm.miniMaxAlgorithm;


import model.botAlgorithm.DifficultyLevel;
import model.botAlgorithm.IGameAlgorithm;
import model.game.IEvaluate;
import model.game.IGame;

import java.util.Random;


public class MiniMax<T extends IGame<T> & IEvaluate> implements IGameAlgorithm<T> {
    private int maxDepth = 13;
    private int playerNumber = -1;
    private int nextMoveNumber = -1;

    @Override
    public void setDifficultyLevel(DifficultyLevel difficulty) {
        if (difficulty == DifficultyLevel.EASY) {
            maxDepth = 5;
        } else if (difficulty == DifficultyLevel.MEDIUM) {
            maxDepth = 9;
        } else {
            maxDepth = 12;
        }
    }

    @Override
    public int findBestWay(T initialPosition, int playerNumber) {
        if(initialPosition.isFirstMove()){
            return 1 + new Random().nextInt(initialPosition.getFieldsAmount());
        }

        this.playerNumber = playerNumber;
        this.nextMoveNumber = -1;
        minimax(initialPosition,
                maxDepth,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                true);
        return nextMoveNumber;
    }

    private int minimax(T position, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || position.gameOver()) {
            return position.evaluate(playerNumber);
        }

        int evaluation = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        int currentPlayerNumber = maximizingPlayer ? playerNumber : (playerNumber + 1) % 2;
        for (T child : position.getAllChildren(currentPlayerNumber)) {
            int eval;
            if (child.extraPlayerMove()) {
                eval = minimax(child, depth, alpha, beta, maximizingPlayer);
            } else {
                eval = minimax(child, depth - 1, alpha, beta, !maximizingPlayer);
            }

            if (depth == maxDepth && eval > evaluation) {
                nextMoveNumber = child.getLastMoveNumber();
            }
            evaluation = maximizingPlayer ? Math.max(evaluation, eval) : Math.min(evaluation, eval);

            if (maximizingPlayer) {
                alpha = Math.max(alpha, evaluation);
            } else {
                beta = Math.min(beta, eval);
            }
            if (beta <= alpha) {
                break;
            }
        }

        return evaluation;
    }
}
