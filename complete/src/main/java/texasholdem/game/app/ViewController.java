package texasholdem.game.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import texasholdem.game.model.Table;

@Controller
public class ViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        Table table = new Table();

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(table);
            model.addAttribute("tableJson", json);
            return "index";
        } catch (JsonProcessingException e) {
            model.addAttribute("error", e);
            return "error";
        }
    }

}
