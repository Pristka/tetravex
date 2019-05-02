package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RatingServiceJDBCTest {

    @Test
    public void testSetRating() throws Exception {

        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("bruh", "tetravex",  3, new Date());
        Rating rating2 = new Rating("jozef","tetravex",5,new Date());
        ratingService.setRating(rating);
        ratingService.setRating(rating2);


        assertEquals(3, ratingService.getRating("tetravex","bruh")); //setRating, getRating test
        assertEquals("bruh",rating.getPlayer());
        assertEquals(4,ratingService.getAverageRating("tetravex")); //averageRating test

    }

}
