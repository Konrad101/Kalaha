package controllers.gameOptions;

public interface Game {
    void setStonesAmount(int stonesAmount);
    void createStones();
    void moveStones(int indexOfHole);
}
