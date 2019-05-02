package sk.tuke.gamestudio.service;


import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "qwerty123";

    public static final String INSERT_RATING =
            "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_RATING =
            "SELECT AVG(rating) FROM rating;";

    public static final String SELECT_USER_RATING =
            "SELECT player, game, rating, ratedon FROM rating WHERE game = ? AND player = ?";


    @Override
    public void setRating(Rating rating) throws RatingException {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_RATING)) {
                ps.setString(2, rating.getGame());
                ps.setString(1, rating.getPlayer());
                ps.setInt(3, rating.getRating());
                ps.setTimestamp(4, new Timestamp(rating.getRatedon().getTime()));

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING)) {

                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading average rating", e);
        }
    }


    @Override
    public int getRating(String game, String player) throws RatingException {
        int playerRating = -1;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_USER_RATING)) {
                ps.setString(1, game);
                ps.setString(2, player);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Rating rating = new Rating(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        playerRating = rating.getRating();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading player rating", e);
        }
        return playerRating;
    }

    public static void main(String[] args) throws Exception {
        //Rating rating = new Rating("prist", "tetravex", 5, new java.util.Date());
        RatingService ratingService = new RatingServiceJDBC();
        //ratingService.setRating(rating);
        //ratingService.getAverageRating("tetravex");
        System.out.println(ratingService.getRating("tetravex", "prist"));
        System.out.println(ratingService.getAverageRating("tetravex"));
    }
}