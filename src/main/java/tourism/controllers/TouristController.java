package tourism.controllers;

import org.springframework.core.metrics.StartupStep;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tourism.model.TouristAttraction;
import tourism.service.TouristService;
import tourism.util.Tag;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/attractions")
public class TouristController {

    private final TouristService touristService;
    private static final Logger logger = Logger.getLogger("ControllerLogger");

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping("/")
    public String showIndex(){
        return "index";
    }

    @GetMapping("")
    public String getAllAttractions(Model model,  @RequestParam(name = "currency", required = false,
            defaultValue = "dkk") String currency) throws IOException, SQLException {
        logger.info("currency: " + currency);
        model.addAttribute("attractions", touristService.getAllAttractions(currency));
        return "attractionList";
    }

    @GetMapping("/{name}")
    public ResponseEntity<TouristAttraction> findAttractionByName(@PathVariable String name){
        return new ResponseEntity<>(touristService.findAttractionByName(name), HttpStatus.OK);
    }

    @GetMapping("/{id}/tags")
    public String getAttractionTags(@PathVariable int id, Model model) throws SQLException {
        model.addAttribute("tags", touristService.findTag(id));
        return "tags";
    }

    @GetMapping("/add")
    public String addAttraction(Model model){
        TouristAttraction touristAttraction = new TouristAttraction();
        model.addAttribute("touristAttraction",touristAttraction);
        return "addAttraction";
    }

    @PostMapping("/save")
    public String saveAttraction(@ModelAttribute TouristAttraction touristAttraction) throws SQLException {
        //model.addAttribute("save",touristService.saveAttraction(touristAttraction));
        touristService.saveAttraction(touristAttraction);
        return "redirect:/attractions";
    }

    @GetMapping("/{name}/edit")
    public String showEditForm(@PathVariable String name, Model model) throws SQLException {
        TouristAttraction touristAttraction = touristService.displayEditAttraction(name);
        List<Tag> selectedTags = touristService.findPrevSelectedTags(touristAttraction.getId());
        System.out.println("Attraction ID: " + touristAttraction.getId());

        model.addAttribute("touristAttraction", touristAttraction);
        model.addAttribute("allPrevTags", selectedTags);
        return "editAttraction";
    }


    //TODO add PUT mapping instead. Could not get it to work without adding WebConfig file to change the post to put via the form on thymeleaf, which seems out of scope
    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute TouristAttraction touristAttraction, @RequestParam("originalName") String originalName){
        touristService.updateAttraction(touristAttraction, originalName);
        return "redirect:/attractions";
    }

    @PostMapping("/delete/{name}")
    public String deleteAttraction(@PathVariable String name) throws SQLException {
        touristService.deleteAttraction(name);
        return "redirect:/attractions";
    }

}