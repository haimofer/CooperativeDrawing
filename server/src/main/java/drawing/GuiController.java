package drawing;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

/**
 * Created by Ofer on 6/22/2017.
 */
@Controller
public class GuiController {

    private static final Logger logger = LogManager.getLogger(GuiController.class);

    @CrossOrigin
    @RequestMapping(path = "/getDrawings", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList<CanvasData> getDrawings() throws Exception {
       return ManageDrawings.getAllResults();
    }

    @CrossOrigin
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    @ResponseBody
    public void saveData(@RequestBody CanvasData data) throws Exception {
        logger.info("entered details: {}",data);
        ManageDrawings.addNewDrawing(data);
    }

}