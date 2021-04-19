package controllers.gameOptions;

import model.DecisionTree.DecisionTree;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class UserVsComputerController extends ComputerGameController {
    private boolean moveIsDone = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        resetButton.setOnAction(event -> resetGame(event, 2));

        for (Circle circle : firstPlayerHoles) {
            circle.setOnMouseEntered(e -> glowingEffectForPlayer(circle));
            circle.setOnMouseExited(e -> disableGlowingEffect(circle));
        }
        initializeHolesActions();
    }

    private void initializeHolesActions() {
        int indexOfHole = 0;
        for (Circle circle : firstPlayerHoles) {
            final int index = indexOfHole++;
            circle.setOnMouseClicked(e -> moveStones(index));
        }
    }

    @Override
    public void moveStones(int indexOfHole) {
        if (moveIsDone) {
            super.moveStones(indexOfHole);
            if (whichPlayer == 1) {
                moveIsDone = false;
                Thread enemyMoveThread = new Thread(this::enemyMove);
                enemyMoveThread.start();
            }
        }
    }

    private void glowingEffectForPlayer(Circle circle) {
        if (whichPlayer == 0) {
            enableGlowingEffect(circle);
        }
    }

    private void disableGlowingEffect(Circle circle) {
        if (whichPlayer == 0 || whichPlayer == 1) {
            DropShadow zeroGlow = new DropShadow();
            zeroGlow.setOffsetX(0f);
            zeroGlow.setOffsetY(0f);

            zeroGlow.setWidth(0.);
            zeroGlow.setHeight(0.);

            circle.setEffect(zeroGlow);
        }
    }

    private void enemyMove() {
        for (Circle circle : firstPlayerHoles) {
            disableGlowingEffect(circle);
        }
        for (Circle circle : secondPlayerHoles) {
            disableGlowingEffect(circle);
        }

        DecisionTree decisionTree = new DecisionTree(kalaha, whichPlayer, treeDepth);

        long startTime = System.currentTimeMillis();
        long currentTime;
        final int indexOfHole = decisionTree.findBestWay() + 6;
        do {
            currentTime = System.currentTimeMillis() - startTime;
        } while (currentTime < 777);

        enableGlowingEffect(secondPlayerHoles.get(indexOfHole - 7));
        moveIsDone = true;

        Platform.runLater(() -> moveStones(indexOfHole));
    }
}
