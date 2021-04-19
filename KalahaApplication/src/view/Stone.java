package view;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Stone extends Circle {
    private int currentHole;    // index

    public Stone(double v){
        super(v);
    }

    public Stone(int currentHole, double v){
        super(v);
        this.currentHole = currentHole;
    }

    public void setCurrentHole(int currentHole) {
        this.currentHole = currentHole;
    }

    public int getCurrentHole() {
        return currentHole;
    }

    public void fillStone(int stoneNumber) {
        switch (stoneNumber) {
            case 0:
                this.setFill(Color.INDIANRED);
                break;
            case 1:
                this.setFill(Color.CORNFLOWERBLUE);
                break;
            case 2:
                this.setFill(Color.LIMEGREEN);
                break;
            case 3:
                this.setFill(Color.YELLOW);
                break;
            case 4:
                this.setFill(Color.ORANGE);
                break;
            case 5:
                this.setFill(Color.GRAY);
                break;
            case 6:
                this.setFill(Color.FIREBRICK);
                break;
            case 7:
                this.setFill(Color.NAVAJOWHITE);
                break;
            case 8:
                this.setFill(Color.DARKBLUE);
                break;
            case 9:
                this.setFill(Color.DARKKHAKI);
                break;
            default:
                this.setFill(Color.DARKVIOLET);
        }
        this.setStroke(Color.DARKSLATEGRAY);
        this.setStrokeWidth(1);
    }

    public void initializeStoneAnchor(double minValue, double maxValue, int stonesAmount, ArrayList<AnchorValues> previousAnchors){
        Random random = new Random();
        int spacing;

        if(stonesAmount == 4){
            spacing = 5;
        } else {
            spacing = 3;
        }

        double leftAnchor;
        double topAnchor;
        boolean correctSpacing;

        int iterations = 0;
        do {
            // if too much iterations then decrease spacing
            if(iterations > 1000){
                if(stonesAmount == 4) {
                    spacing = 4;
                } else{
                    spacing = 2;
                }

                if(iterations > 5000){
                    if(stonesAmount == 4) {
                        spacing = 3;
                    } else {
                        spacing = 1;
                    }
                }
            }


            correctSpacing = true;
            leftAnchor = minValue + ((maxValue - minValue) * random.nextDouble());
            topAnchor  = minValue + ((maxValue - minValue) * random.nextDouble());

            for (int i = 0; i < previousAnchors.size() && correctSpacing; i++) {
                if(Math.abs(previousAnchors.get(i).getLeftAnchor() - leftAnchor) < spacing){
                    correctSpacing = false;
                } else {
                    if(Math.abs(previousAnchors.get(i).getTopAnchor() - topAnchor) < spacing){
                        correctSpacing = false;
                    }
                }
            }

            iterations++;
        }while (!correctSpacing);

        previousAnchors.add(new AnchorValues(leftAnchor, topAnchor));
        AnchorPane.setLeftAnchor(this, leftAnchor);
        AnchorPane.setTopAnchor(this, topAnchor);
    }

    public void changePositionToBase(){
        // LEFT: MIN = 11.5   MAX = 49.
        // TOP:  MIN = 0.     MAX = 90.
        changePosition(11.5, 49., 0., 90.);
    }

    private void changePosition(double leftMin, double leftMax, double topMin, double topMax){
        Random random = new Random();
        double leftAnchor;
        double topAnchor;

        leftAnchor = leftMin + ((leftMax - leftMin) * random.nextDouble());
        topAnchor  = topMin + ((topMax - topMin) * random.nextDouble());

        AnchorPane.setLeftAnchor(this, leftAnchor);
        AnchorPane.setTopAnchor(this, topAnchor);
    }
}
