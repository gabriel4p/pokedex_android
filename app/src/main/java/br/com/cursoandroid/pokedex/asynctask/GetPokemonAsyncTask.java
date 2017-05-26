package br.com.cursoandroid.pokedex.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.cursoandroid.pokedex.MainActivity;
import br.com.cursoandroid.pokedex.models.Pokemon;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetPokemonAsyncTask extends AsyncTask <Integer, Void, JSONObject>{

    private final String TAG_CATEGORY = "GetPokemonAsyncTask";

    MainActivity activity;

    public GetPokemonAsyncTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Integer id = params[0];
        String url = String.format("http://pokeapi.co/api/v2/pokemon/%s", id);
        JSONObject jsonObject = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            Log.e("ERROR HTTP", e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON EXCEPTION", e.getMessage());
        }

        return jsonObject;
    }


    protected void onPostExecute(JSONObject jsonObject){

        try {
            Pokemon pokemon = null;
            if(jsonObject != null)
                pokemon = new Pokemon(jsonObject);
            activity.preencherDados(pokemon);
        } catch (Exception e) {
            Log.e(TAG_CATEGORY, e.getMessage(), e);
        }
    }
}
