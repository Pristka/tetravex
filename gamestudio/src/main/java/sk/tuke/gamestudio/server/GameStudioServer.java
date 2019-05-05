package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.tetravex.olejnik.webui.WebUI;
import sk.tuke.gamestudio.service.*;

@Configuration
@SpringBootApplication
@EntityScan({"sk.tuke.gamestudio.entity"})
public class GameStudioServer {
	static { /* too late ! */
		System.setProperty("java.awt.headless", "false");
		System.out.println(java.awt.GraphicsEnvironment.isHeadless());
		/* ---> prints false */
	}
	public static void main(String[] args) {
		SpringApplication.run(GameStudioServer.class, args);
	}

	@Bean(name="scoreServiceServer")
	public ScoreService scoreService() {return new ScoreServiceJPA();}

	@Bean(name="commentServiceServer")
	public CommentService commentService() {return new CommentServiceJPA();}

	@Bean(name="ratingServiceServer")
	public RatingService ratingService() {return new RatingServiceJPA();}

	@Bean(name="webUI")
	public WebUI webUI() {
		return new WebUI();
	}
}
