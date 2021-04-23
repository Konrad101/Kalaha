package model.botAlgorithm;

import model.game.IEvaluate;
import model.game.IGame;

public interface IGameAlgorithm<T extends IGame<T> & IEvaluate> extends IAdjustable {
    int findBestWay(T initialPosition, int playerNumber);
}
