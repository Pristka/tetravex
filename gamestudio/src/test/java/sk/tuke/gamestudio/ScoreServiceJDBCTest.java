package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreService;
import sk.tuke.gamestudio.server.service.ScoreServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ScoreServiceJDBCTest {

    @Test
    public void addScore() {

        ScoreService service = new ScoreServiceJDBC();
        Score score1 = new Score("tetravex",  "Zuzka", 200, new Date());
        Score score2 = new Score("tetravex",  "Peter", 250, new Date());
        service.addScore(score1);
        service.addScore(score2);
        List<Score> scores = service.getBestScores("tetravex"); // setScore test

        assertEquals(2, scores.size());
        assertEquals(score2.getPlayer(), scores.get(0).getPlayer());
        assertEquals(score2.getPoints(), scores.get(0).getPoints()); //getBest score test
        assertEquals(score1.getPlayer(), scores.get(1).getPlayer());
        assertEquals(score1.getPoints(), scores.get(1).getPoints());
    }
}
