package br.com.cursoandroid.pokedex.repository;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.cursoandroid.pokedex.asynctask.GetPokemonAsyncTask;
import br.com.cursoandroid.pokedex.contract.PokemonAsyncTaskHandler;
import br.com.cursoandroid.pokedex.models.Constantes;
import br.com.cursoandroid.pokedex.models.Erro;
import br.com.cursoandroid.pokedex.models.Pokemon;

public class PokemonRepository implements PokemonAsyncTaskHandler {

    private List<Pokemon> pokemons;
    private static PokemonRepository repository;
    private PokemonAsyncTaskHandler listener;
    private SharedPreferences preferences;

    private PokemonRepository(){
        pokemons = new ArrayList<>();
    }

    public void add(Pokemon pokemon){
        int indiceItem = -1;
        for(int i = 0; i < pokemons.size() && indiceItem == -1; i++){
            if(pokemons.get(i).getId() == pokemon.getId()){
                indiceItem = i;
            }
        }
        if(indiceItem != -1)
            pokemons.remove(indiceItem);
        pokemons.add(pokemon);
        saveFile();
    }

    public void getByIdAsync(int id){
        new GetPokemonAsyncTask(this).execute(id);
    }

    public Pokemon getById(int id){
        for(int i=0; i<pokemons.size(); i++)
            if(pokemons.get(i).getId() == id)
                return pokemons.get(i);
        return null;
    }

    public static PokemonRepository getInstance(){
        if(repository == null){
            repository = new PokemonRepository();
        }
        return repository;
    }

    @Override
    public void tratarErro(Erro erro) {
        if(listener != null)
            listener.tratarErro(erro);
    }

    @Override
    public void receberPokemon(Pokemon pokemon) {
        add(pokemon);
        if(listener != null)
            listener.receberPokemon(pokemon);
    }

    public void setListener(PokemonAsyncTaskHandler listener){
        this.listener = listener;
    }

    public void saveFile(){
        if(preferences == null)
            throw new NullPointerException("SharedPreferences nao pode ser nulo");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.FILE_POKEMON, getListaPokemonStringFile());
        editor.commit();
    }

    private String getListaPokemonStringFile() {
        StringBuilder sb = new StringBuilder();
        for(Pokemon p : pokemons)
            sb.append(String.format("%s\n", p.toJson()));
        return sb.toString();
    }

    public void loadFromFile(SharedPreferences preferences) {
        try {
            this.preferences = preferences;
            String jsonFile = preferences.getString(Constantes.FILE_POKEMON, Constantes.BANCO_SEM_REGISTROS);

            if (jsonFile.equals(Constantes.BANCO_SEM_REGISTROS))
                return;

            if (pokemons == null)
                pokemons = new ArrayList<>();
            else
                pokemons.removeAll(pokemons);
            for (String s : jsonFile.split("\n")) {
                if(!s.trim().isEmpty()) {
                    JSONObject jsonPokemon = new JSONObject(s);
                    Pokemon p = new Pokemon();
                    p.fromJson(jsonPokemon);
                    pokemons.add(p);
                }
            }
        }catch(Exception e){
            Log.d("ERRO LOAD ARQUIVO", e.getMessage());
        }
    }
}
