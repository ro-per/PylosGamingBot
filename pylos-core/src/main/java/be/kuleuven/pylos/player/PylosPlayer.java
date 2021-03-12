package be.kuleuven.pylos.player;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosPlayerColor;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jan on 16/02/2015.
 */
public abstract class PylosPlayer {

    private PylosPlayerObserver OBSERVER;
    private Random RANDOM;
    public PylosPlayerColor PLAYER_COLOR;
    public PylosPlayer OTHER;
    public static final Logger logger = Logger.getLogger(PylosPlayer.class.getName());


    public void init(PylosPlayerColor playerColor, PylosPlayer other, PylosPlayerObserver observer, Random random) {
        this.PLAYER_COLOR = playerColor;
        this.OTHER = other;
        this.OBSERVER = observer;
        this.RANDOM = random;
        logger.setLevel(Level.FINEST);
    }

    public abstract void doMove(PylosGameIF game, PylosBoard board) throws Exception;

    public abstract void doRemove(PylosGameIF game, PylosBoard board) throws Exception;

    public abstract void doRemoveOrPass(PylosGameIF game, PylosBoard board) throws Exception;

    protected Random getRandom() {
        return RANDOM;
    }

    protected PylosPlayerObserver getObserver() {
        return OBSERVER;
    }

    public String toString() {
        return PLAYER_COLOR.toString();
    }

}
