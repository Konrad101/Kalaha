package controllers.gameControllers;

import model.botAlgorithm.DifficultyLevel;

public abstract class ComputerGameController extends GameController {
    protected static DifficultyLevel difficultyLevel;

    public void setDifficulty(DifficultyLevel difficulty){
        difficultyLevel = difficulty;
    }
}
