package controllers.gameOptions;

public abstract class ComputerGameController extends GameController {
    protected static int treeDepth;

    public void setTreeDepth(int depth){
        if(treeDepth >= 1 && treeDepth <= 9) {
            treeDepth = depth;
        }
    }
}
