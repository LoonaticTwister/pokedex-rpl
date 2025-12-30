package com.pokedex.ujian.model;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PokemonDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Pokemon pokemon) {
        sessionFactory.getCurrentSession().persist(pokemon);
    }

    public void update(Pokemon pokemon) {
        sessionFactory.getCurrentSession().merge(pokemon);
    }

    public void delete(Pokemon pokemon) {
        Pokemon p = sessionFactory.getCurrentSession().get(Pokemon.class, pokemon.getId());
        if (p != null) {
            sessionFactory.getCurrentSession().remove(p);
        }
    }

    public Pokemon findById(Long id) {
        return sessionFactory.getCurrentSession().get(Pokemon.class, id);
    }

    public List<Pokemon> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Pokemon", Pokemon.class).list();
    }
    
    public List<String> findNamesLike(String keyword) {
        return sessionFactory.getCurrentSession()
                .createQuery("select p.nama from Pokemon p where p.nama LIKE :keyword", String.class)
                .setParameter("keyword", "%" + keyword + "%")
                .setMaxResults(10)
                .list();
    }
}
