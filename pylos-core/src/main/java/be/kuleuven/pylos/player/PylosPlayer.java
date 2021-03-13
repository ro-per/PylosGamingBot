package be.kuleuven.pylos.player;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosPlayerColor;

import java.util.Random;

/**
 * Created by Jan on 16/02/2015.
 */
public abstract class PylosPlayer {

    private PylosPlayerObserver OBSERVER;
    public static Random PYLOS_PLAYER_RANDOM;
    public PylosPlayerColor PLAYER_COLOR;
    public PylosPlayer OTHER;

    public void init(PylosPlayerColor playerColor, PylosPlayer other, PylosPlayerObserver observer, Random random) {
        this.PLAYER_COLOR = playerColor;
        this.OTHER = other;
        this.OBSERVER = observer;
        PYLOS_PLAYER_RANDOM = new Random();
    }

    public abstract void doMove(PylosGameIF game, PylosBoard board) throws Exception;

    public abstract void doRemove(PylosGameIF game, PylosBoard board) throws Exception;

    public abstract void doRemoveOrPass(PylosGameIF game, PylosBoard board) throws Exception;

    protected Random getRandom() {
        return PYLOS_PLAYER_RANDOM;
    }

    protected PylosPlayerObserver getObserver() {
        return OBSERVER;
    }

    public String toString() {
        return PLAYER_COLOR.toString();
    }

}
