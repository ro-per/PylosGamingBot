package be.kuleuven.pylos.battle;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.Writer;

public class BattleResults implements Jsonable {

    private String order;

    private int runs;

    private double lightStartLightWin;
    private double lightStartDarkWin;
    private double lightStartDraw;

    private double darkStartLightWin;
    private double darkStartDarkWin;
    private double darkStartDraw;

    private double totalLightWin;
    private double totalDarkWin;
    private double totalDraw;

    private double totalPlayTime;

    public BattleResults(int runs, double tlw, double tdw, double td, double time) {
        this.runs = runs;
        this.totalLightWin = tlw;
        this.totalDarkWin = tdw;
        this.totalDraw = td;
        this.totalPlayTime = time;
    }


    @Override
    public String toJson() {
        JsonObject json = new JsonObject();
        json.put("runs", this.runs);
        json.put("totalLightWin", this.totalLightWin);
        json.put("totalDarkWin", this.totalDarkWin);
        json.put("totalDraw", this.totalDraw);
        json.put("totalPlayTime", this.totalDraw);
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
