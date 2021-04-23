package controllers.gameControllers;

public interface IGameController {
    void setStonesAmount(int stonesAmount);
    void createStones();
    void moveStones(int indexOfHole);
}
