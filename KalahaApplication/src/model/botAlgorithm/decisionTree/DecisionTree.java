package model.botAlgorithm.decisionTree;

import model.botAlgorithm.DifficultyLevel;
import model.botAlgorithm.IGameAlgorithm;
import model.game.IEvaluate;
import model.game.IGame;
import model.game.kalaha.Kalaha;

import java.util.ArrayList;
import java.util.Random;


public class DecisionTree<T extends IGame<T> & IEvaluate> implements IGameAlgorithm<T> {
    private Kalaha kalaha;
    private int playerNumber;
    private Node root;
    private DifficultyLevel difficulty = DifficultyLevel.MEDIUM;

    /**
     * @return hole number for greatest advantage over opponent*/
    @Override
    public int findBestWay(T initialPosition, int playerNumber) {
        this.kalaha = new Kalaha((Kalaha) initialPosition);
        this.playerNumber = playerNumber;
        this.root = new Node(kalaha, playerNumber, getDepthForDifficultyLevel());

        root.createChildren();
        root.createBranches();

        return getBestPath();
    }

    private int getDepthForDifficultyLevel(){
        if(difficulty == DifficultyLevel.EASY){
            return 4;
        } else if (difficulty == DifficultyLevel.MEDIUM){
            return 6;
        }

        return 8;
    }

    private int getBestPath(){
        int max = -100;
        int pathNum = 0;
        ArrayList<Node> leaves = root.getAllLeaves(root);
        ArrayList<Node> bestLeaves = new ArrayList<>();

        System.gc();

        for(Node leaf: leaves){
            if(leaf.getCollectedStones() >= max && !kalaha.holeIsEmpty(leaf.getStartHole(), playerNumber)){
                if(leaf.getCollectedStones() > max){
                    bestLeaves.clear();
                }

                max = leaf.getCollectedStones();
                bestLeaves.add(leaf);
            }
        }

        Random random = new Random();
        if(bestLeaves.size() > 0) {
            int rand = random.nextInt(bestLeaves.size());
            pathNum = bestLeaves.get(rand).getStartHole();
        }

        leaves = null;
        root = null;    // to protect your RAM <3
        System.gc();

        return pathNum;
    }

    @Override
    public void setDifficultyLevel(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
}
