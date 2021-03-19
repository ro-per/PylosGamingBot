package be.kuleuven.pylos.battle;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class BattleResults implements Jsonable {

    private String order;

    private int runs;

    private double lightStartLightWin;
    private double lightStartDarkWin;
    private double lightStartDraw;

    private double darkStartLightWin;
    private double darkStartDarkWin;
    private double darkStartDraw;

    private int totalLightWin;
    private int totalDarkWin;
    private int totalDraw;

    private double totalPlayTime;

    public BattleResults(String order, int runs, double tlw, double tdw, double td, double time) {
        this.order = order;
        this.runs = runs;
        this.totalLightWin = (int) tlw;
        this.totalDarkWin = (int) tdw;
        this.totalDraw = (int) td;
        this.totalPlayTime = time;
    }

    public int getRuns() {
        return runs;
    }

    @Override
    public String toJson() {
        JsonObject json = new JsonObject();
        json.put("order", this.order);
        json.put("runs", this.runs);
        json.put("totalLightWin", this.totalLightWin);
        json.put("totalDarkWin", this.totalDarkWin);
        json.put("totalDraw", this.totalDraw);
        json.put("totalPlayTime", this.totalPlayTime);
        return json.toJson();
    }

    @Override
    public void toJson(Writer writable) throws IOException {
        try {
            writable.write(this.toJson());
        } catch (Exception ignored) {
        }
    }
}
