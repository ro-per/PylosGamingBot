package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGame;
import be.kuleuven.pylos.game.PylosGameState;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;
import java.util.Random;


/**
 * Created by Jan on 19/02/2015.
 */
public class Battle {
    public static List<String> order_core;

    private static final Random random = new Random(0);

    /**
     * @param playerLight
     * @param playerDark
     * @param runs
     * @return
     * @throws Exception
     */
    public static BattleResults play(PylosPlayer playerLight, PylosPlayer playerDark, int runs) throws Exception {
        return play(playerLight, playerDark, runs, true);
    }

    public static BattleResults play(PylosPlayer playerLight, PylosPlayer playerDark, int runs, boolean print) throws Exception {
        BattleResults battleResults = null;
        if (runs % 2 != 0) {
            throw new IllegalArgumentException("Please specify an even number of runs");
        }

        double totalPlayTime = 0;
        int lightStartLightWin = 0;
        int lightStartDarkWin = 0;
        int lightStartDraw = 0;
        String playerLightClass = playerLight.getClass().getSimpleName();
        String playerDarkClass = playerDark.getClass().getSimpleName();

        for (int i = 0; i < runs / 2; i++) {
            PylosBoard board = new PylosBoard();
            PylosGame game = new PylosGame(board, playerLight, playerDark, random);
            double startTime = System.currentTimeMillis();
            game.play();
            double playTime = System.currentTimeMillis() - startTime;
            totalPlayTime += playTime;
            String message = (i + 1) + "/" + runs + "\tLight: " + playerLightClass + "\tDark: " + playerDarkClass + "\t";
            if (game.getState() == PylosGameState.DRAW) {
                lightStartDraw++;
                message += "Draw";
            } else {
                if (game.getWinner() == playerLight) {
                    lightStartLightWin++;
                } else {
                    lightStartDarkWin++;
                }
                message += "Winner: " + (game.getWinner() == playerLight ? "Light" : "Dark");
            }
//			System.out.println(message);
        }

        int darkStartLightWin = 0;
        int darkStartDarkWin = 0;
        int darkStartDraw = 0;

        for (int i = 0; i < runs / 2; i++) {
            PylosBoard board = new PylosBoard();
            PylosGame game = new PylosGame(board, playerDark, playerLight, random);
            double startTime = System.currentTimeMillis();
            game.play();
            double playTime = System.currentTimeMillis() - startTime;
            totalPlayTime += playTime;
            String message = (i + 1 + runs / 2) + "/" + runs + "\tLight: " + playerDarkClass + "\tDark: " + playerLightClass + "\t";
            if (game.getState() == PylosGameState.DRAW) {
                darkStartDraw++;
                message += "Draw";
            } else {
                if (game.getWinner() == playerLight) {
                    darkStartLightWin++;
                } else {
                    darkStartDarkWin++;
                }
                message += "Winner: " + (game.getWinner() == playerLight ? "Dark" : "Light");
            }
//			System.out.println(message);
        }

        totalPlayTime /= 1000;
        int totalLightWin = lightStartLightWin + darkStartLightWin;
        int totalDarkWin = lightStartDarkWin + darkStartDarkWin;
        int totalDraw = lightStartDraw + darkStartDraw;

        if (print) {
            double tlw = (double) totalLightWin / runs * 100;
            double tdw = (double) totalDarkWin / runs * 100;
            double td = (double) totalDraw / runs * 100;
            StringBuilder sb = new StringBuilder();
            for(String s: order_core) sb.append(s);

            battleResults = new BattleResults(sb.toString(),runs, tlw, tdw, td, totalPlayTime);

            /*System.out.println("");
            System.out.println("----------------------------");
            System.out.println(runs / 2 + " games where " + playerLightClass + " starts:");
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) lightStartLightWin / (runs / 2) * 100)) + "% " + playerLightClass);
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) lightStartDarkWin / (runs / 2) * 100)) + "% " + playerDarkClass);
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) lightStartDraw / (runs / 2) * 100)) + "% Draw");
            System.out.println();
            System.out.println(runs / 2 + " games where " + playerDarkClass + " starts:");
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) darkStartLightWin / (runs / 2) * 100)) + "% " + playerLightClass);
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) darkStartDarkWin / (runs / 2) * 100)) + "% " + playerDarkClass);
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) darkStartDraw / (runs / 2) * 100)) + "% Draw");
            System.out.println();
            System.out.println(runs + " games in total:");
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) totalLightWin / runs * 100)) + "% " + playerLightClass);
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) totalDarkWin / runs * 100)) + "% " + playerDarkClass);
            System.out.println(String.format(" * %6s", String.format("%.2f", (double) totalDraw / runs * 100)) + "% Draw");
            System.out.println();
            System.out.println("Time: " + String.format("%.2f", totalPlayTime) + " sec (" + String.format("%.2f", totalPlayTime / runs) + " sec / game)");
            System.out.println("----------------------------");*/
        }

        double[] wins = new double[]{(double) (totalLightWin) / runs, (double) (totalDarkWin) / runs, (double) (totalDraw) / runs};
        return battleResults;
    }
}
