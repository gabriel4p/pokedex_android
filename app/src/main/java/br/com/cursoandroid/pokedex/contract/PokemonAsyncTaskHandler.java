package br.com.cursoandroid.pokedex.contract;

import br.com.cursoandroid.pokedex.models.Erro;
import br.com.cursoandroid.pokedex.models.Pokemon;

public interface PokemonAsyncTaskHandler {
    void tratarErro(Erro erro);
    void receberPokemon(Pokemon pokemon);
}
