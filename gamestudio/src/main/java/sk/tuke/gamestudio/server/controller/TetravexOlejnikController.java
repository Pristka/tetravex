package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.tetravex.olejnik.webui.WebUI;
import sk.tuke.gamestudio.service.*;

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TetravexOlejnikController {

    private WebUI webUI = new WebUI();

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingServiceJPA ratingService;


    @RequestMapping("/tetravex-olejnik")
    public String tetravex(@RequestParam(value = "command", required = false) String command,
                        @RequestParam(value = "row", required = false) String row,
                        @RequestParam(value = "column", required = false) String column,
                        Model model) throws CommentException {

        // if required, add additional code, e.g. to check provided parameters for null

        webUI.processCommand(command, row, column);
        model.addAttribute("webUI", webUI);
        List<Score> bestScores = scoreService.getBestScores("tetravex");
        List<Comment> getComments = commentService.getComments("tetravex");
        model.addAttribute("scores", bestScores);
        model.addAttribute("comments",getComments);

        return "tetravex-olejnik"; //same name as the template

    }
}
