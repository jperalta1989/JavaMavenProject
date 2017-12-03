package texasholdem.game.app;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
