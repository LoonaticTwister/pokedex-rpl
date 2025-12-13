package com.pokedex.ujian.service;

import com.pokedex.ujian.dao.PokemonDao;
import com.pokedex.ujian.model.Pokemon;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PokemonService {

    @Autowired
    private PokemonDao pokemonDao;

    public void savePokemon(Pokemon pokemon) {
        pokemonDao.save(pokemon);
    }

    public void updatePokemon(Pokemon pokemon) {
        pokemonDao.update(pokemon);
    }

    public void deletePokemon(Pokemon pokemon) {
        pokemonDao.delete(pokemon);
    }

    @Transactional(readOnly = true)
    public List<Pokemon> getAllPokemon() {
        return pokemonDao.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<String> searchPokemonNames(String keyword) {
        return pokemonDao.findNamesLike(keyword);
    }
}
