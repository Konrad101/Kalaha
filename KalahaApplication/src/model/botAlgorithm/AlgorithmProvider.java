package model.botAlgorithm;

import model.botAlgorithm.miniMaxAlgorithm.MiniMax;
import model.game.kalaha.Kalaha;

public class AlgorithmProvider {

    private AlgorithmProvider() {
    }

    public static IGameAlgorithm<Kalaha> getAlgorithm() {
        return new MiniMax<>();
    }
}
