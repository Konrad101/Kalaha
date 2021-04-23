package model.game;

import java.util.List;

public interface IGame<T> {
    int getLastMoveResult();
    int getLastMoveNumber();
    boolean gameOver();
    List<T> getAllChildren(int playerNumber);
}
