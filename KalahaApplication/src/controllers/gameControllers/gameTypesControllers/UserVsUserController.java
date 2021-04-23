package controllers.gameControllers.gameTypesControllers;

import controllers.gameControllers.GameController;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class UserVsUserController extends GameController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        resetButton.setOnAction(event -> resetGame(event, 3));

        for(Circle circle: firstPlayerHoles){
            circle.setOnMouseEntered(e -> glowingEffectForPlayer(circle, 0));
            circle.setOnMouseExited(e -> disableGlowingEffect(circle));
        }

        for(Circle circle: secondPlayerHoles){
            circle.setOnMouseEntered(e -> glowingEffectForPlayer(circle, 1));
            circle.setOnMouseExited(e -> disableGlowingEffect(circle));
        }

        initializeHolesActions();
    }

    private void initializeHolesActions(){
        int indexOfHole = 0;
        for(Circle circle: firstPlayerHoles){
            final int index = indexOfHole++;
            circle.setOnMouseClicked(e -> moveStones(index));
        }

        indexOfHole++;

        for(Circle circle: secondPlayerHoles){
            final int index = indexOfHole++;
            circle.setOnMouseClicked(e -> moveStones(index));
        }
    }

    private void glowingEffectForPlayer(Circle circle, int playerHole){
        if(playerHole == playerNumber) {
            enableGlowingEffect(circle);
        }
    }

    private void disableGlowingEffect(Circle circle){
        if(playerNumber == 0 || playerNumber == 1) {
            DropShadow zeroGlow = new DropShadow();
            zeroGlow.setOffsetX(0f);
            zeroGlow.setOffsetY(0f);

            zeroGlow.setWidth(0.);
            zeroGlow.setHeight(0.);

            circle.setEffect(zeroGlow);
        }
    }
}
