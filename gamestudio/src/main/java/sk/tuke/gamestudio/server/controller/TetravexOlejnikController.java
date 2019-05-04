package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.webui.WebUI;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TetravexOlejnikController {

    private WebUI webUI = new WebUI();

    private final ScoreService scoreService;

    private final CommentService commentService;

    private final RatingServiceJPA ratingService;

    private Field field;

    @Autowired
    public TetravexOlejnikController(ScoreService scoreService, CommentService commentService, RatingServiceJPA ratingService) {
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }


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

    @RequestMapping("/comment")
    public String comment(String player, String comment, Model model) throws CommentException {
        model.addAttribute("webUI", webUI);
        commentService.addComment(new Comment(player,"tetravex",comment,new Date()));
        return "tetravex-olejnik";
    }

    @RequestMapping("/rating")
    public String rating(String player, int rating, Model model) throws RatingException {
        model.addAttribute("webUI", webUI);
        ratingService.setRating(new Rating(player,"tetravex",rating,new Date()));
        return "tetravex-olejnik";
    }

}
