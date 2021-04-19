package controllers.gameOptions;

import model.DecisionTree.DecisionTree;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComputerVsComputerController extends ComputerGameController {
    public Button startGameButton;

    private boolean startGameActivated = false;
    private boolean closedStage        = false;

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
        if(!startGameActivated) {
            setGlowingEffectOnButton(startGameButton, 10, 10);
        }
    }

    public void setClosedStage(boolean closedStage){
        this.closedStage = closedStage;
    }

    @Override
    public void moveStones(int indexOfHole) {
        super.moveStones(indexOfHole);

        if(!kalaha.checkStopCondition()) {
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
    void resetGame(ActionEvent event, int gameOption) {
        super.resetGame(event, gameOption);
        closedStage = true;
    }

    private void disableGlowingEffect(){
        DropShadow zeroGlow = new DropShadow();
        zeroGlow.setWidth(0);
        zeroGlow.setHeight(0);

        for(Circle circle: firstPlayerHoles){
            circle.setEffect(zeroGlow);
        }
        for(Circle circle: secondPlayerHoles){
            circle.setEffect(zeroGlow);
        }
    }

    // Threads
    public void startGame() {
        if (!startGameActivated) {
            startGameActivated = true;
            Thread playThread = new Thread(this::play);
            playThread.start();
        }
    }

    private void play() {
        DecisionTree decisionTree;
        int holeIndex;
        int holeNum;

        long startTime;
        long currentTime;

        while (!kalaha.checkStopCondition() && !closedStage) {
            startTime = System.currentTimeMillis();
            decisionTree = new DecisionTree(kalaha, whichPlayer, treeDepth);

            holeNum = decisionTree.findBestWay();
            if (whichPlayer == 0) {
                holeIndex = holeNum - 1;

            } else {
                holeIndex = holeNum + 6;
            }

            final int index = holeIndex;
            Platform.runLater(() -> moveStones(index));

            do {
                currentTime = System.currentTimeMillis() - startTime;
            } while (currentTime < 500);
        }
    }
}
