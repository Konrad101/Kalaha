package controllers.gameControllers;

import controllers.MenuController;
import model.game.kalaha.Kalaha;
import view.AnchorValues;
import view.Stone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public abstract class GameController implements Initializable, IGameController {
    public GridPane boardGridLayout;
    public Button menuButton;
    public Button resetButton;

    public VBox middleVBox;
    private Text gameNameText;

    public Circle secondPlayerCircle;
    public Circle firstPlayerCircle;

    public AnchorPane firstPlayerBasePane;
    public AnchorPane secondPlayerBasePane;

    private final ArrayList<AnchorPane> anchorPanes = new ArrayList<>(12);
    public AnchorPane firstPaneFirstPlayer;
    public AnchorPane secondPaneFirstPlayer;
    public AnchorPane thirdPaneFirstPlayer;
    public AnchorPane fourthPaneFirstPlayer;
    public AnchorPane fifthPaneFirstPlayer;
    public AnchorPane sixthPaneFirstPlayer;

    public AnchorPane firstPaneSecondPlayer;
    public AnchorPane secondPaneSecondPlayer;
    public AnchorPane thirdPaneSecondPlayer;
    public AnchorPane fourthPaneSecondPlayer;
    public AnchorPane fifthPaneSecondPlayer;
    public AnchorPane sixthPaneSecondPlayer;

    protected ArrayList<Circle> firstPlayerHoles = new ArrayList<>(6);
    public Circle firstHoleFirstPlayer;
    public Circle secondHoleFirstPlayer;
    public Circle thirdHoleFirstPlayer;
    public Circle fourthHoleFirstPlayer;
    public Circle fifthHoleFirstPlayer;
    public Circle sixthHoleFirstPlayer;

    protected ArrayList<Circle> secondPlayerHoles = new ArrayList<>(6);
    public Circle firstHoleSecondPlayer;
    public Circle secondHoleSecondPlayer;
    public Circle thirdHoleSecondPlayer;
    public Circle fourthHoleSecondPlayer;
    public Circle fifthHoleSecondPlayer;
    public Circle sixthHoleSecondPlayer;


    private final ArrayList<Stone> stones = new ArrayList<>();
    private final ArrayList<Text> stonesAmount = new ArrayList<>(14);

    private int menuStonesAmount;
    private boolean gameStarted = false;

    protected Kalaha kalaha;
    protected int playerNumber;

    @Override
    public void setStonesAmount(int stonesAmount) {
        menuStonesAmount = stonesAmount;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeEffectOnPlayerCircle(0);
        setGlowingEffectOnButton(menuButton, 10, 10);
        setGlowingEffectOnButton(resetButton, 10, 10);
        setGlowingEffectsOnButtons();
        setGameNameText();

        playerNumber = 0;
        addHolesToList();
        addAnchorPanesToList();
        addStonesAmountText();
    }

    private void setGlowingEffectsOnButtons() {
        menuButton.setOnMouseEntered(e -> setGlowingEffectOnButton(menuButton, 37, 37));
        menuButton.setOnMouseExited(e -> setGlowingEffectOnButton(menuButton, 10, 10));

        resetButton.setOnMouseEntered(e -> setGlowingEffectOnButton(resetButton, 37, 37));
        resetButton.setOnMouseExited(e -> setGlowingEffectOnButton(resetButton, 10, 10));
    }

    @Override
    public void createStones() {
        kalaha = new Kalaha(menuStonesAmount);
        for (int i = 0; i < kalaha.getBoard().length - 1; i++) {
            // first base index
            if (i == (kalaha.getBoard().length / 2) - 1) {
                i++;
            }

            int indexOfNextHole = i;
            ArrayList<AnchorValues> anchorValues = new ArrayList<>();
            for (int j = 0; j < menuStonesAmount; j++) {
                Stone s = new Stone(i, 11);

                s.setDisable(true);
                s.initializeStoneAnchor(17.5, 46.0, menuStonesAmount, anchorValues);

                s.fillStone(j);
                stones.add(s);

                if (indexOfNextHole == kalaha.getBoard().length - 1) {
                    indexOfNextHole = -1;
                }
            }
        }

        int firstStoneIndex = 0;
        for (AnchorPane pane : anchorPanes) {
            addStonesToHole(pane, firstStoneIndex);
            firstStoneIndex += menuStonesAmount;
        }

        updateStonesAmountOnText();
    }

    private void setGameNameText() {
        gameNameText = new Text("Kalaha");
        gameNameText.setFont(new Font(45));
        gameNameText.setFill(new Color(0.75, 0.55, 0.34, 1));

        VBox.setMargin(gameNameText, new Insets(16.8, 0, 0, 33));
        middleVBox.getChildren().add(gameNameText);
    }

    private void addStonesToHole(AnchorPane anchorPane, int firstStonesIndex) {
        int stoneIndex = firstStonesIndex + new Random().nextInt(menuStonesAmount);

        for (int i = 0; i < menuStonesAmount; i++) {
            anchorPane.getChildren().add(stones.get(stoneIndex));
            stoneIndex++;
            if (stoneIndex == (firstStonesIndex + menuStonesAmount)) {
                stoneIndex = firstStonesIndex;
            }
        }
    }

    private void addStonesAmountText() {
        int row = 3, column = 1;
        Text text;
        for (int i = 0; i < 6; i++) {
            text = createStonesAmountText(row, column, false);
            stonesAmount.add(text);
            column++;
        }
        // first base - index = 6
        text = createBaseText(0, 7);
        stonesAmount.add(text);

        row = 1;
        column = 6;
        for (int i = 0; i < 6; i++) {
            text = createStonesAmountText(row, column, true);
            stonesAmount.add(text);
            column--;
        }
        // second base - index = 13
        text = createBaseText(4, 0);
        stonesAmount.add(text);

        boardGridLayout.getChildren().addAll(stonesAmount);
    }

    private Text createStonesAmountText(int row, int column, boolean upperSide) {
        Text text = setupBasicsText(false);

        GridPane.setConstraints(text, column, row);
        if (upperSide) {
            GridPane.setValignment(text, VPos.TOP);
        } else {
            GridPane.setValignment(text, VPos.BOTTOM);
        }

        return text;
    }

    private Text createBaseText(int row, int column) {
        Text text = setupBasicsText(true);

        GridPane.setConstraints(text, column, row);
        GridPane.setValignment(text, VPos.CENTER);

        return text;
    }

    private Text setupBasicsText(boolean baseText) {
        Text text;
        if (!baseText) {
            text = new Text("" + menuStonesAmount);
        } else {
            text = new Text("" + 0);
        }

        text.setFont(new Font("System", 28));
        text.setFill(Color.web("0xBF8D57"));
        GridPane.setHalignment(text, HPos.CENTER);

        return text;
    }

    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/Menu.fxml"));
        loader.load();

        Parent menuParent = loader.getRoot();

        Scene menuScene = new Scene(menuParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        MenuController controller = loader.getController();
        controller.changeStonesAmount(menuStonesAmount);
        System.gc();

        window.setScene(menuScene);
        window.show();
    }

    protected void resetGame(ActionEvent event, int gameOption) {
        if (gameStarted) {
            String url;
            if (gameOption == 1) {
                url = "/view/ComputerVsComputer.fxml";
            } else {
                if (gameOption == 2) {
                    url = "/view/UserVsComputer.fxml";
                } else {
                    url = "/view/UserVsUser.fxml";
                }
            }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(url));

            try {
                loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Parent menuParent = loader.getRoot();
            Scene menuScene = new Scene(menuParent);

            GameController controller = loader.getController();
            controller.setStonesAmount(menuStonesAmount);
            controller.createStones();

            System.gc();

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(menuScene);
            window.show();
        }
    }

    protected void setGlowingEffectOnButton(Button button, double width, double height) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(new Color(0.75, 0.55, 0.34, 1));
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(height);
        borderGlow.setWidth(width);

        button.setEffect(borderGlow);
    }


    public void moveStones(int indexOfHole) {
        int playerNum;

        if (indexOfHole < 6) {
            playerNum = 0;
        } else {
            playerNum = 1;
        }

        if (playerNum == playerNumber) {
            gameStarted = true;
            int holeNum;

            holeNum = numberOfHoleForIndex(indexOfHole);
            int result = kalaha.move(holeNum, playerNum);
            if (result != -1) {
                int boardLength = kalaha.getBoard().length;

                int indexOfOpponentBase;
                if (indexOfHole < 6) {
                    indexOfOpponentBase = boardLength - 1;
                } else {
                    indexOfOpponentBase = (boardLength / 2) - 1;
                }

                int index = indexOfHole + 1;
                for (Stone stone : stones) {
                    if (stone.getCurrentHole() == indexOfHole) {
                        for (AnchorPane pane : anchorPanes) {
                            pane.getChildren().remove(stone);
                        }

                        if (index == indexOfOpponentBase) {
                            index++;
                        }
                        if (index == boardLength) {
                            index = 0;
                        }

                        if (index < boardLength / 2) {
                            if (index == (boardLength / 2) - 1) {
                                firstPlayerBasePane.getChildren().add(stone);
                            } else {
                                anchorPanes.get(index).getChildren().add(stone);
                            }
                        } else {
                            if (index == boardLength - 1) {
                                secondPlayerBasePane.getChildren().add(stone);
                            } else {
                                anchorPanes.get(index - 1).getChildren().add(stone);
                            }
                        }

                        stone.setCurrentHole(index);
                        index++;
                    }
                }

                if (result == 2) {
                    // steal your and opponent's stones to your base (playerNumber before change)
                    int lastHoleIndex = kalaha.getLastHoleIndex();
                    int opponentHoleIndex;

                    if (indexOfHole < 6) {
                        opponentHoleIndex = 12 - lastHoleIndex;
                    } else {
                        opponentHoleIndex = 6 - numberOfHoleForIndex(lastHoleIndex);
                    }

                    for (Stone stone : stones) {
                        if (stone.getCurrentHole() == opponentHoleIndex || stone.getCurrentHole() == lastHoleIndex) {
                            for (AnchorPane pane : anchorPanes) {
                                pane.getChildren().remove(stone);
                            }

                            stone.changePositionToBase();
                            if (indexOfHole < 6) {
                                stone.setCurrentHole(6);
                                firstPlayerBasePane.getChildren().add(stone);
                            } else {
                                stone.setCurrentHole(13);
                                secondPlayerBasePane.getChildren().add(stone);
                            }
                        }
                    }
                }

                updateStonesAmountOnText();
            }

            int nextMovePlayerNumber = kalaha.extraPlayerMove() ? playerNumber : (playerNumber + 1) % 2;
            if (kalaha.gameOver(nextMovePlayerNumber)) {
                kalaha.collectRestOfStones();
                collectSecondPlayerStones();
                collectFirstPlayerStones();

                updateStonesAmountOnText();
                enableWinnerGlowingEffect(kalaha.whichPlayerWon());
                setWinnerText(kalaha.whichPlayerWon());

                for (Circle circle : firstPlayerHoles) {
                    enableGlowingEffect(circle);
                }
                for (Circle circle : secondPlayerHoles) {
                    enableGlowingEffect(circle);
                }

                playerNumber = 2;
            } else {
                if (result != 1 && result != -1) {
                    playerNumber = (playerNum + 1) % 2;
                    changeEffectOnPlayerCircle(playerNumber);
                }
            }
        }
    }

    private void updateStonesAmountOnText() {
        int index = 0;
        for (Text text : stonesAmount) {
            text.setText("" + kalaha.getBoard()[index++]);
        }
    }

    private void collectFirstPlayerStones() {
        for (Stone stone : stones) {
            if (stone.getCurrentHole() < (kalaha.getBoard().length / 2) - 1) {
                for (AnchorPane pane : anchorPanes) {
                    pane.getChildren().remove(stone);
                }
                stone.changePositionToBase();
                stone.setCurrentHole((kalaha.getBoard().length / 2) - 1);
                firstPlayerBasePane.getChildren().add(stone);
            }
        }
    }

    private void collectSecondPlayerStones() {
        for (Stone stone : stones) {
            if (stone.getCurrentHole() > (kalaha.getBoard().length / 2) - 1
                    && stone.getCurrentHole() < (kalaha.getBoard().length) - 1) {
                for (AnchorPane pane : anchorPanes) {
                    pane.getChildren().remove(stone);
                }
                stone.changePositionToBase();
                stone.setCurrentHole(kalaha.getBoard().length - 1);
                secondPlayerBasePane.getChildren().add(stone);
            }
        }
    }

    private int numberOfHoleForIndex(int index) {
        if (index < 6) {
            return index + 1;
        }

        return (index % 7) + 1;
    }

    protected void enableGlowingEffect(Circle circle) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLDENROD);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(50);
        borderGlow.setWidth(50);

        circle.setEffect(borderGlow);
    }

    /**
     * @param winnerNumber must be the same as player who wins
     *                     otherwise method will enable glow for both of players (draw)
     */
    private void enableWinnerGlowingEffect(int winnerNumber) {
        DropShadow zeroGlow = new DropShadow();
        zeroGlow.setHeight(0);
        zeroGlow.setWidth(0);

        firstPlayerCircle.setEffect(zeroGlow);
        secondPlayerCircle.setEffect(zeroGlow);

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.PALEGOLDENROD);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(70);
        borderGlow.setWidth(70);
        if (winnerNumber == 0) {
            firstPlayerCircle.setEffect(borderGlow);
        } else {
            secondPlayerCircle.setEffect(borderGlow);
            if (winnerNumber == 2) {
                firstPlayerCircle.setEffect(borderGlow);
            }
        }
    }

    private void setWinnerText(int winnerNumber) {
        if (winnerNumber == 0 || winnerNumber == 1) {
            winnerNumber++;
            middleVBox.getChildren().remove(gameNameText);
            Font font = new Font("Yu Gothic Light", 34);
            Color color = new Color(0.75, 0.55, 0.34, 1);

            Text playerText = new Text("Player " + winnerNumber);
            Text wonText = new Text("won");

            playerText.setFont(font);
            wonText.setFont(font);
            playerText.setFill(color);
            wonText.setFill(color);

            VBox.setMargin(playerText, new Insets(13.5, 0, 0, 40));
            VBox.setMargin(wonText, new Insets(0, 0, 0, 68));

            middleVBox.getChildren().add(playerText);
            middleVBox.getChildren().add(wonText);
        } else {
            gameNameText.setText("Draw!");
            gameNameText.setFont(new Font("Yu Gothic Light", 55));
            VBox.setMargin(gameNameText, new Insets(20, 0, 0, 29.7));
        }
    }


    private void changeEffectOnPlayerCircle(int playerNumber) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLDENROD);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(50);
        borderGlow.setWidth(50);

        DropShadow zeroGlow = new DropShadow();
        zeroGlow.setWidth(0);
        zeroGlow.setHeight(0);

        if (playerNumber == 0) {
            secondPlayerCircle.setEffect(zeroGlow);
            firstPlayerCircle.setEffect(borderGlow);
        } else {
            firstPlayerCircle.setEffect(zeroGlow);
            secondPlayerCircle.setEffect(borderGlow);
        }
    }

    private void addHolesToList() {
        firstPlayerHoles.add(firstHoleFirstPlayer);
        firstPlayerHoles.add(secondHoleFirstPlayer);
        firstPlayerHoles.add(thirdHoleFirstPlayer);
        firstPlayerHoles.add(fourthHoleFirstPlayer);
        firstPlayerHoles.add(fifthHoleFirstPlayer);
        firstPlayerHoles.add(sixthHoleFirstPlayer);

        secondPlayerHoles.add(firstHoleSecondPlayer);
        secondPlayerHoles.add(secondHoleSecondPlayer);
        secondPlayerHoles.add(thirdHoleSecondPlayer);
        secondPlayerHoles.add(fourthHoleSecondPlayer);
        secondPlayerHoles.add(fifthHoleSecondPlayer);
        secondPlayerHoles.add(sixthHoleSecondPlayer);
    }

    private void addAnchorPanesToList() {
        anchorPanes.add(firstPaneFirstPlayer);
        anchorPanes.add(secondPaneFirstPlayer);
        anchorPanes.add(thirdPaneFirstPlayer);
        anchorPanes.add(fourthPaneFirstPlayer);
        anchorPanes.add(fifthPaneFirstPlayer);
        anchorPanes.add(sixthPaneFirstPlayer);

        anchorPanes.add(firstPaneSecondPlayer);
        anchorPanes.add(secondPaneSecondPlayer);
        anchorPanes.add(thirdPaneSecondPlayer);
        anchorPanes.add(fourthPaneSecondPlayer);
        anchorPanes.add(fifthPaneSecondPlayer);
        anchorPanes.add(sixthPaneSecondPlayer);
    }
}