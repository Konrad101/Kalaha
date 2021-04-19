package model.DecisionTree;

import model.Kalaha;

import java.util.ArrayList;
import java.util.Random;


public class DecisionTree {
    private Kalaha kalaha;
    private int whichPlayer;
    private Node root;

    public DecisionTree(Kalaha kalaha, int whichPlayer, int maxDepth){
        this.root = new Node(kalaha, whichPlayer, maxDepth);
        this.kalaha = new Kalaha(kalaha);
        this.whichPlayer = whichPlayer;
    }

    /**
     * @return hole number for greatest advantage over opponent*/
    public int findBestWay(){
        root.createChildren();
        root.createBranches();

        return getBestPath();
    }

    private int getBestPath(){
        int max = -100;
        int pathNum = 0;
        ArrayList<Node> leafs = root.getAllLeafs(root);
        ArrayList<Node> bestLeafs = new ArrayList<>();

        System.gc();

        for(Node leaf: leafs){
            if(leaf.getCollectedStones() >= max && !kalaha.holeIsEmpty(leaf.getStartHole(), whichPlayer)){
                if(leaf.getCollectedStones() > max){
                    bestLeafs.clear();
                }

                max = leaf.getCollectedStones();
                bestLeafs.add(leaf);
            }
        }

        Random random = new Random();
        if(bestLeafs.size() > 0) {
            int rand = random.nextInt(bestLeafs.size());
            pathNum = bestLeafs.get(rand).getStartHole();
        }

        leafs = null;
        root = null;    // protect your RAM <3
        System.gc();

        return pathNum;
    }
}