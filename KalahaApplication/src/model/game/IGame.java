package model.game;

import java.util.List;

public interface IGame<T> {
    boolean extraPlayerMove();
    int getLastMoveNumber();
    int getFieldsAmount();
    boolean gameOver(int playerNumber);
    boolean isFirstMove();
    List<T> getAllChildren(int playerNumber);
}
