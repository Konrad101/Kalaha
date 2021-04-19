package controllers;

import controllers.gameOptions.ComputerGameController;
import controllers.gameOptions.ComputerVsComputerController;
import controllers.gameOptions.Game;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public ImageView leftImage;
    public ImageView rightImage;

    public MenuButton stonesAmountButton;

    public Button compVsCompButton;
    public Button userVsCompButton;
    public Button userVsUserButton;
    public AnchorPane topAnchorPane;

    private int stones;

    private final Button[] difficultyButtons = new Button[3];
    private static int difficultyLevel = 2;

    private final static String SELECTED_LEVEL_COLOR = "#d39756";
    private final static String UNSELECTED_LEVEL_COLOR = "#ecba88";

    private final static int MAX_STONES_AMOUNT = 11;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardAnimation();
        stones = 4;
        setMenuItemsOnButton();
        createDifficultyButtons();

        stonesAmountButton.setOnMouseEntered(e -> glowingEffectOnEnter(stonesAmountButton));
        compVsCompButton.setOnMouseEntered(e -> glowingEffectOnEnter(compVsCompButton));
        userVsCompButton.setOnMouseEntered(e -> glowingEffectOnEnter(userVsCompButton));
        userVsUserButton.setOnMouseEntered(e -> glowingEffectOnEnter(userVsUserButton));

        stonesAmountButton.setOnMouseExited(e -> glowingEffectOnExit(stonesAmountButton));
        compVsCompButton.setOnMouseExited(e -> glowingEffectOnExit(compVsCompButton));
        userVsCompButton.setOnMouseExited(e -> glowingEffectOnExit(userVsCompButton));
        userVsUserButton.setOnMouseExited(e -> glowingEffectOnExit(userVsUserButton));
    }

    private void setMenuItemsOnButton() {
        MenuItem menuItem;
        String itemText = " Stone";

        for (int i = 0; i < MAX_STONES_AMOUNT; i++) {
            menuItem = new MenuItem();
            final int amount = i + 1;

            menuItem.setOnAction(e -> changeStonesAmount(amount));
            if (i == 0) {
                menuItem.setText(amount + itemText);
            } else {
                menuItem.setText(amount + itemText + "s");
            }
            stonesAmountButton.getItems().add(menuItem);
        }
    }

    private void boardAnimation() {
        TranslateTransition leftTransition = new TranslateTransition();

        leftTransition.setDuration(Duration.seconds(3));
        leftTransition.setToY(-40);
        leftTransition.setAutoReverse(true);
        leftTransition.setCycleCount(Animation.INDEFINITE);

        leftTransition.setNode(leftImage);
        leftTransition.play();

        TranslateTransition rightTransition = new TranslateTransition();

        rightTransition.setDuration(Duration.seconds(3));
        rightTransition.setToY(40);
        rightTransition.setAutoReverse(true);
        rightTransition.setCycleCount(Animation.INDEFINITE);

        rightTransition.setNode(rightImage);
        rightTransition.play();
    }

    private void glowingEffectOnEnter(Node button) {
        setGlowingEffect(button, 18, 18);
    }

    private void glowingEffectOnExit(Node button) {
        setGlowingEffect(button, 0, 0);
    }

    private void setGlowingEffect(Node node, double width, double height) {
        DropShadow glowingEffect = new DropShadow();
        glowingEffect.setOffsetX(0f);
        glowingEffect.setOffsetY(0f);

        glowingEffect.setWidth(width);
        glowingEffect.setHeight(height);
        glowingEffect.setColor(Color.LIGHTYELLOW);

        node.setEffect(glowingEffect);
    }

    public void computerVsComputer(ActionEvent event) {
        changeSceneToGame(event, 1);
    }

    public void userVsComputer(ActionEvent event) {
        changeSceneToGame(event, 2);
    }

    public void userVsUser(ActionEvent event) {
        changeSceneToGame(event, 3);
    }

    private void changeSceneToGame(ActionEvent event, int optionNumber) {
        String url;
        if (optionNumber == 1) {
            url = "/view/ComputerVsComputer.fxml";
        } else {
            if (optionNumber == 2) {
                url = "/view/UserVsComputer.fxml";
            } else {
                url = "/view/UserVsUser.fxml";
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(url));

            loader.load();

            Parent menuParent = loader.getRoot();
            Scene menuScene = new Scene(menuParent);


            if (optionNumber == 1 || optionNumber == 2) {
                int depth = 6;
                if (difficultyLevel == 1) {
                    depth = 4;
                } else if (difficultyLevel == 3) {
                    depth = 8;
                }
                ComputerGameController controller = loader.getController();
                controller.setTreeDepth(depth);
                controller.setStonesAmount(stones);
                controller.createStones();
            } else {
                Game controller = loader.getController();
                controller.setStonesAmount(stones);
                controller.createStones();
            }

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (optionNumber == 1) {
                ComputerVsComputerController compVsCompController = loader.getController();
                window.setOnCloseRequest(e -> compVsCompController.setClosedStage(true));
            }

            window.setScene(menuScene);
            window.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void changeStonesAmount(int stonesAmount) {
        stones = stonesAmount;
        stonesAmountButton.setText("Stones amount: " + stonesAmount);
    }

    protected void createDifficultyButtons() {
        int xPos = 12;
        int yPos = 12;
        int level = 1;

        for (int i = 0; i < difficultyButtons.length; i++) {
            difficultyButtons[i] = new Button();
            if (i == 0) {
                difficultyButtons[i].setText("Easy");
            } else if (i == 1) {
                difficultyButtons[i].setText("Medium");
            } else {
                difficultyButtons[i].setText("Hard");
            }

            final int currentLvl = level;
            difficultyButtons[i].setOnAction(e -> changeSelectedColor(currentLvl, false));

            level++;
        }

        setButtonsStyle(xPos, yPos);
        topAnchorPane.getChildren().addAll(difficultyButtons);
    }

    private void setButtonsStyle(int xPos, int yPos) {
        changeSelectedColor(difficultyLevel, true);
        int width = 65;
        for (Button button : difficultyButtons) {
            button.setPrefWidth(width);
            button.setPrefHeight(27);
            button.setLayoutX(xPos);
            button.setLayoutY(yPos);

            xPos += width + 6;
        }
    }

    public void changeSelectedColor(int level, boolean firstChange) {
        if (level != difficultyLevel || firstChange) {
            String colorCode;
            for (int i = 0; i < difficultyButtons.length; i++) {
                if (i + 1 == level) {
                    colorCode = SELECTED_LEVEL_COLOR;
                } else {
                    colorCode = UNSELECTED_LEVEL_COLOR;
                }
                difficultyButtons[i].setStyle("-fx-background-color: " + colorCode + "; ");
            }
            difficultyLevel = level;
        }
    }
}
