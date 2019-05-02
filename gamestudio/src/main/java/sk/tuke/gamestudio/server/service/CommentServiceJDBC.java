package sk.tuke.gamestudio.server.service;


import sk.tuke.gamestudio.server.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {

    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "qwerty123";


    public static final String INSERT_COMMENT =
            "INSERT INTO comment (player, game, text, commented_on) VALUES (?, ?, ?, ?)";

    public static final String SELECT_COMMENT =
            "SELECT player, game, text, commented_on FROM comment where game = ? ORDER BY commented_on DESC";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT)) {
                ps.setString(2, comment.getGame());
                ps.setString(1, comment.getPlayer());
                ps.setString(3, comment.getComment());
                ps.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CommentException("Error saving comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_COMMENT)) {

            ps.setString(1, game);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getTimestamp(4));
                comments.add(comment);
            }

        } catch (SQLException e) {
            throw new CommentException("Error loading score", e);
        }

        return comments;
    }

}
