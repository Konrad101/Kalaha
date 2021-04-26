package model.botAlgorithm.decisionTree;

import model.game.kalaha.Kalaha;

import java.util.ArrayList;
import java.util.concurrent.*;

class Node {
    private final int startHole;
    private int originalPlayer;
    private final int whichPlayer;
    private final int depth;
    private int collectedStones;
    private final ArrayList<Node> children;
    public Kalaha currentKalaha;
    final int MAX_DEPTH;

    // root constructor
    Node (Kalaha currentKalaha, int whichPlayer, int maxDepth) {
        this.depth = 0;
        this.collectedStones = 0;
        this.startHole = 0;
        this.whichPlayer = whichPlayer;
        this.originalPlayer = whichPlayer;
        this.MAX_DEPTH = maxDepth;

        this.currentKalaha = currentKalaha;
        children = new ArrayList<>(currentKalaha.getBoard().length);
    }

    private Node (Kalaha currentKalaha, int startHole, int depth, int collectedStones, int whichPlayer, int maxDepth) {
        this.depth = depth;
        this.collectedStones = collectedStones;
        this.whichPlayer = whichPlayer;
        this.startHole = startHole;
        this.MAX_DEPTH = maxDepth;

        this.currentKalaha = currentKalaha;
        children = new ArrayList<>((currentKalaha.getBoard().length / 2) - 1);
    }

    int getStartHole(){
        return startHole;
    }

    int getCollectedStones() {
        return collectedStones;
    }

    ArrayList<Node> getChildren(){
        return children;
    }

    ArrayList<Node> getAllLeaves(Node node){
        ArrayList<Node> leafs = new ArrayList<>();
        if(node.children.isEmpty()){
            leafs.add(node);
        }else{
            for(Node child: node.children){
                leafs.addAll(getAllLeaves(child));
            }
        }

        return leafs;
    }

    void createBranchToLeaf(Node node) {
        int holesAmount = (currentKalaha.getBoard().length / 2) - 1;
        for (int i = 0; i < holesAmount; i++) {
            Kalaha childKalaha = new Kalaha(node.currentKalaha);

            int result = childKalaha.move(i + 1, node.whichPlayer);
            int nextMovePlayerNumber = childKalaha.extraPlayerMove() ? node.whichPlayer : (node.whichPlayer + 1) % 2;
            if (childKalaha.gameOver(nextMovePlayerNumber)) {
                childKalaha.collectRestOfStones();
            }

            node.collectedStones = node.currentKalaha.getStonesAmount(originalPlayer) - node.currentKalaha.getOpponentStonesAmount(originalPlayer);

            if (node.depth < MAX_DEPTH && result != -1) {
                Node child;

                // result = 0 - change player
                if (result == 0) {
                    if (node.whichPlayer == 0) {
                        child = new Node(childKalaha, node.startHole, node.depth + 1, node.collectedStones, 1, this.MAX_DEPTH);
                    } else {
                        child = new Node(childKalaha, node.startHole, node.depth + 1, node.collectedStones, 0, this.MAX_DEPTH);
                    }
                } else {
                    // result = 1 - the same player
                    child = new Node(childKalaha, node.startHole, node.depth + 1, node.collectedStones, node.whichPlayer, this.MAX_DEPTH);
                }

                node.children.add(child);
                createBranchToLeaf(child);
            }
        }
    }

    void createBranches(){
        int holesAmount = (currentKalaha.getBoard().length / 2) - 1;

        //only for root's children
        ExecutorService executorService = Executors.newFixedThreadPool(holesAmount);

        for (int i = 0; i < holesAmount; i++) {
            Runnable worker = new BranchThread(this, i);
            executorService.execute(worker);
        }

        executorService.shutdown();
        long startTime = System.currentTimeMillis();
        long waitingTime = System.currentTimeMillis() - startTime;
        while(!executorService.isTerminated() && waitingTime < 30000){
            waitingTime = System.currentTimeMillis() - startTime;
        }
    }

    /**
     * only for root*/
    void createChildren() {
        int holesAmount = (currentKalaha.getBoard().length / 2) - 1;

        for (int i = 0; i < holesAmount; i++) {
            Kalaha childKalaha = new Kalaha(currentKalaha);
            int result = childKalaha.move(i + 1, whichPlayer);

            int stonesInHole = childKalaha.getStonesAmount(whichPlayer);
            Node child;
            if(result == 1) {
                child = new Node(childKalaha, i + 1, depth + 1, stonesInHole, whichPlayer, this.MAX_DEPTH);
            }else {
                child = new Node(childKalaha, i + 1, depth + 1, stonesInHole, (whichPlayer + 1) % 2, this.MAX_DEPTH);
            }
            children.add(child);
        }
    }
}
