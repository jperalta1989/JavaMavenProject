package texasholdem.game.app;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import texasholdem.game.model.Player;
import texasholdem.game.model.Table;

@RestController
public class AjaxController {

    @RequestMapping(value = "/ajax/player/join")
    public String playerJoin(
            Model model,
            @RequestParam(value="username", defaultValue="player") String username,
            @RequestParam(value="balance", defaultValue="100") String balance,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            Table table = mapper.readValue(tableJson, Table.class);

            int intBalance = Integer.valueOf(balance);
            Player player = new Player(username, intBalance);

            table.playerJoinsGame(player);

            String json = mapper.writeValueAsString(table);

            return json;
        } catch (JsonProcessingException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (IOException e) {
            model.addAttribute("error", e);
            return "error";
        }
    }

    @RequestMapping(value = "/ajax/game/start")
    public String gameStart(
            Model model,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {
        return runTableStage(model, tableJson, "pre-flop");
    }

    @RequestMapping(value = "/ajax/game/flop")
    public String gameFlop(
            Model model,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {

        return runTableStage(model, tableJson, "flop");
    }

    @RequestMapping(value = "/ajax/game/turn")
    public String gameTurn(
            Model model,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {

        return runTableStage(model, tableJson, "turn");
    }

    @RequestMapping(value = "/ajax/game/river")
    public String gameRiver(
            Model model,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {

        return runTableStage(model, tableJson, "river");
    }

    @RequestMapping(value = "/ajax/game/showdown")
    public String gameShowDown(
            Model model,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {

        return runTableStage(model, tableJson, "showdown");
    }

    @RequestMapping(value = "/ajax/game/reset")
    public String gameReset(
            Model model,
            @RequestParam(value="tableJson", defaultValue="") String tableJson) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            Table table = mapper.readValue(tableJson, Table.class);
            table.newGame();
            String json = mapper.writeValueAsString(table);

            return json;
        } catch (JsonProcessingException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (IOException e) {
            model.addAttribute("error", e);
            return "error";
        }
    }




    private String runTableStage(Model model, String tableJson, String stage){
        ObjectMapper mapper = new ObjectMapper();

        try {
            Table table = mapper.readValue(tableJson, Table.class);
            table.getStageMethodData(stage);
            String json = mapper.writeValueAsString(table);

            return json;
        } catch (JsonProcessingException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (IOException e) {
            model.addAttribute("error", e);
            return "error";
        }
    }
}
