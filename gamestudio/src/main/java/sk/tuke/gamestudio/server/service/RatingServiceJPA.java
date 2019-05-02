package sk.tuke.gamestudio.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.server.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        Object r = null;
        try {
            r = entityManager.createQuery("select r from Rating r where r.game = :game and r.player = :player")
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .getSingleResult();
        } catch (NoResultException e) {
            entityManager.persist(rating);
            return;
        }
        ((Rating) r).setRating(rating.getRating());

    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return (int) Math.round((Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult());
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return (int) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", player).getSingleResult();
    }
}
