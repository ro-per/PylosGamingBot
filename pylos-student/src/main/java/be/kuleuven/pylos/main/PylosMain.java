package be.kuleuven.pylos.main;

import be.kuleuven.pylos.battle.Battle;
import be.kuleuven.pylos.battle.BattleResults;
import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGame;
import be.kuleuven.pylos.game.PylosGameObserver;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.PylosPlayerObserver;
import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.student.StudentPlayerBestFit;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.BufferedWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static be.kuleuven.pylos.battle.Battle.*;

public class PylosMain {
    private final Random R = new Random(); //TODO SEED PYLOS
    public static List<String> order;

    public PylosMain() {
    }

    public void startSingleGame(PylosPlayer player1, PylosPlayer player2) throws Exception {
        PylosBoard pylosBoard = new PylosBoard();
        PylosGame pylosGame = new PylosGame(pylosBoard, player1, player2, R, PylosGameObserver.CONSOLE_GAME_OBSERVER, PylosPlayerObserver.NONE);
        pylosGame.play();
    }

    public void startBattle(PylosPlayer player1, PylosPlayer player2, int battleCount) throws Exception {

        List<Object> list = new ArrayList<>();
        list.add("A1");
        list.add("A21");
        list.add("A22");
        list.add("B");
        list.add("C1");
        list.add("C21");
        list.add("C22");
//        list.add("D");
        list.add("E");
//        list.add("F");
        list.add("G");

        List<BattleResults> battleResultsList = new ArrayList<>();
        for (List permutation : Permutation.getPermutations(list)) {
            order_core = permutation;
            BattleResults brs = Battle.play(player1, player2, battleCount);
            battleResultsList.add(brs);
        }
        try {
            // CURRENT DATE
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
            LocalDateTime now = LocalDateTime.now();
            String nows=dtf.format(now);
            //HOSTNAME
            String hostname = "Unknown";
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get("json/"+hostname+"_"+nows+"_battleResultsList.json"));
            Jsoner.serialize(battleResultsList, writer);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        /* !!! vm argument !!! -ea */
        PylosPlayer player1 = new PylosPlayerBestFit();
        PylosPlayer player2 = new StudentPlayerBestFit();

        int battleCount = Integer.parseInt(args[0]);
        if (battleCount == 1) new PylosMain().startSingleGame(player1, player2);
        else new PylosMain().startBattle(player1, player2, battleCount);
    }

}
