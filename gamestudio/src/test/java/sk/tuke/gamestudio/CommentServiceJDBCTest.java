package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CommentServiceJDBCTest {

    @Test
    public void addComment() throws CommentException {

        CommentService service = new CommentServiceJDBC();
        Comment comment = new Comment("Zuzka", "tetravex", "bruh", new Date());
        service.addComment(comment);
        List<Comment> comments = service.getComments("tetravex");

        assertEquals(1, comments.size()); // add comment test
        assertEquals(comment.getComment(), comments.get(0).getComment()); //get comment test
    }
}

