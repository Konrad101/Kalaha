package controllers.gameControllers.gameTypesControllers;

import controllers.gameControllers.ComputerGameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;
import model.botAlgorithm.AlgorithmProvider;
import model.botAlgorithm.IGameAlgorithm;
import model.game.kalaha.Kalaha;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComputerVsComputerController extends ComputerGameController {
    private final static int MIN_THINKING_TIME = 500;
    public Button startGameButton;

    private boolean startGameActivated = false;
    private boolean closedStage = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        resetButton.setOnAction(event -> resetGame(event, 1));
        setGlowingEffectOnButton(startGameButton, 10, 10);
    }

    public void increaseGlowingEffect() {
        setGlowingEffectOnButton(startGameButton, 45, 45);
    }

    public void decreaseGlowingEffect() {
        if (!startGameActivated) {
            setGlowingEffectOnButton(startGameButton, 10, 10);
        }
    }

    public void setClosedStage(boolean closedStage) {
        this.closedStage = closedStage;
    }

    @Override
    public void moveStones(int indexOfHole) {
        super.moveStones(indexOfHole);

        if (!kalaha.checkStopCondition()) {
            Circle circleEffect;
            if (indexOfHole < 6) {
                circleEffect = firstPlayerHoles.get(indexOfHole);
            } else {
                circleEffect = secondPlayerHoles.get(indexOfHole % 7);
            }

            disableGlowingEffect();
            enableGlowingEffect(circleEffect);
        }
    }

    @Override
    public void backToMenu(ActionEvent event) throws IOException {
        closedStage = true;
        super.backToMenu(event);
    }

    @Override
    protected void resetGame(ActionEvent event, int gameOption) {
        super.resetGame(event, gameOption);
        closedStage = true;
    }

    private void disableGlowingEffect() {
        DropShadow zeroGlow = new DropShadow();
        zeroGlow.setWidth(0);
        zeroGlow.setHeight(0);

        for (Circle circle : firstPlayerHoles) {
            circle.setEffect(zeroGlow);
        }
        for (Circle circle : secondPlayerHoles) {
            circle.setEffect(zeroGlow);
        }
    }

    public void startGame() {
        if (!startGameActivated) {
            startGameActivated = true;
            Thread playThread = new Thread(this::play);
            playThread.start();
        }
    }

    private void play() {
        IGameAlgorithm<Kalaha> algorithm;
        int holeIndex;
        int holeNum;

        long startTime;
        long currentTime;

        while (!kalaha.checkStopCondition() && !closedStage) {
            startTime = System.currentTimeMillis();
            algorithm = AlgorithmProvider.getAlgorithm();
            algorithm.setDifficultyLevel(difficultyLevel);

            holeNum = algorithm.findBestWay(kalaha, playerNumber);
            if (playerNumber == 0) {
                holeIndex = holeNum - 1;

            } else {
                holeIndex = holeNum + 6;
            }

            final int index = holeIndex;
            Platform.runLater(() -> moveStones(index));

            do {
                currentTime = System.currentTimeMillis() - startTime;
            } while (currentTime < MIN_THINKING_TIME);
        }
    }
}
