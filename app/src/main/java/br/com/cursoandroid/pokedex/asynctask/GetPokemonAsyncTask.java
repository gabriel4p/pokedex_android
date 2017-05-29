package br.com.cursoandroid.pokedex.asynctask;

import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.cursoandroid.pokedex.contract.PokemonAsyncTaskHandler;
import br.com.cursoandroid.pokedex.models.Constantes;
import br.com.cursoandroid.pokedex.models.Erro;
import br.com.cursoandroid.pokedex.models.Pokemon;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetPokemonAsyncTask extends AsyncTask <Integer, Void, JSONObject>{

    private PokemonAsyncTaskHandler pokemonHandler;

    public GetPokemonAsyncTask(PokemonAsyncTaskHandler pokemonHandler){
        this.pokemonHandler = pokemonHandler;
    }

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Integer id = params[0];
        String url = String.format("%s/%s", Constantes.URL_POKEAPI, id);
        JSONObject jsonObject = null;
        Erro erro = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            jsonObject = new JSONObject(json);
            if (jsonObject.has("detail") && jsonObject.getString("detail").equals("Not found.")){
                throw new Resources.NotFoundException("Pokemon Not Found");
            }

            JSONObject jsonSprites = new JSONObject(jsonObject.getString("sprites"));
            jsonObject.put("sprite", jsonSprites.getString("front_default"));
            JSONArray arrayTipos = jsonObject.getJSONArray("types");
            JSONArray tipos = new JSONArray();
            for(int i = 0; i < arrayTipos.length(); i++){
                JSONObject typeJson = arrayTipos.getJSONObject(i).getJSONObject("type");
                tipos.put(typeJson.getString("name"));
            }
            jsonObject.put("tipos", tipos);

        } catch (Resources.NotFoundException e){
            erro = new Erro(e, Constantes.ERRO_NOT_FOUND);
        } catch (IOException e) {
            erro = new Erro(e, Constantes.ERRO_NETWORK);
        } catch (JSONException e) {
            erro = new Erro(e, Constantes.ERRO_UNKNOWN);
        }

        if(erro != null){
            pokemonHandler.tratarErro(erro);
            jsonObject = null;
        }

        return jsonObject;
    }

    protected void onPostExecute(JSONObject jsonObject){

        try {
            if(jsonObject != null){
                Pokemon pokemon = new Pokemon();
                pokemon.fromJson(jsonObject);
                pokemonHandler.receberPokemon(pokemon);
            }
        } catch (Exception e) {
            pokemonHandler.tratarErro(new Erro(e, Constantes.ERRO_UNKNOWN));
        }
    }
}
