package be.kuleuven.pylos.main;

import be.kuleuven.pylos.battle.Battle;
import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGame;
import be.kuleuven.pylos.game.PylosGameObserver;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.PylosPlayerObserver;
import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.codes.PylosPlayerRandomFit;
import be.kuleuven.pylos.player.student.StudentPlayerBestFit2;
import be.kuleuven.pylos.player.student.StudentPlayerRandomFit;

import java.util.Random;

public class PylosMain {
    private final Random R = new Random(-1); //TODO SEED PYLOS

    public PylosMain() {
    }

    public void startSingleGame(PylosPlayer player1, PylosPlayer player2) throws Exception {
        PylosBoard pylosBoard = new PylosBoard();
        PylosGame pylosGame = new PylosGame(pylosBoard, player1, player2, R, PylosGameObserver.CONSOLE_GAME_OBSERVER, PylosPlayerObserver.NONE);

        pylosGame.play();
    }

    public void startBattle(PylosPlayer player1, PylosPlayer player2, int battleCount) throws Exception {
        Battle.play(player1, player2, battleCount);
    }

    public static void main(String[] args) throws Exception {
        /* !!! vm argument !!! -ea */
        PylosPlayer player1 = new PylosPlayerBestFit();
        PylosPlayer player2 = new StudentPlayerBestFit2();

        int battleCount = Integer.parseInt(args[0]);
        if (battleCount == 1) new PylosMain().startSingleGame(player1, player2);
        else new PylosMain().startBattle(player1, player2, battleCount);
    }

}
