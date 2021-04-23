package model.game;

import java.util.List;

public interface IGame<T> {
    int getLastMoveResult();
    int getLastMoveNumber();
    int getFieldsAmount();
    boolean gameOver();
    boolean isFirstMove();
    List<T> getAllChildren(int playerNumber);
}
