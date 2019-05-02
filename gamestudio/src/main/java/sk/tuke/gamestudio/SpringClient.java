package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.tetravex.olejnik.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.server.service.*;

@Configuration
@SpringBootApplication
public class SpringClient {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) { return args -> ui.run(); }

    @Bean
    public ConsoleUI consoleUI(Field field) { return new ConsoleUI(field); }

    @Bean
    public Field field() { return new Field(3,3); }

    @Bean
    public ScoreService scoreService() { //return new ScoreServiceJPA();
        return new ScoreServiceRestClient(); }

    @Bean
    public CommentService commentService() { //return new CommentServiceJPA();
        return new CommentServiceRestClient(); }

    @Bean
    public RatingService ratingService() { //return new RatingServiceJPA();
        return new RatingServiceRestClient(); }
}
