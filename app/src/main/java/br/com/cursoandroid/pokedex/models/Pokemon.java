package br.com.cursoandroid.pokedex.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private int id;
    private String urlImagem;
    private String nome;
    private int altura;
    private int peso;
    private List<String> tipos;

    public Pokemon(JSONObject jsonPokemon){
        try {
            JSONObject jsonSprites = new JSONObject(jsonPokemon.getString("sprites"));
            this.id = jsonPokemon.getInt("id");
            this.urlImagem = jsonSprites.getString("front_default");
            this.nome = jsonPokemon.getString("name");
            this.altura = jsonPokemon.getInt("height");
            this.peso = jsonPokemon.getInt("weight");
            JSONArray arrayTipos = jsonPokemon.getJSONArray("types");
            tipos = new ArrayList<>();
            for(int i = 0; i < arrayTipos.length(); i++){
                JSONObject typeJson = arrayTipos.getJSONObject(i).getJSONObject("type");
                tipos.add(typeJson.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getTipos() {
        StringBuilder sb = new StringBuilder();
        for(String tipo : tipos)
            sb.append(String.format("%s, ", tipo));
        String tipos = sb.toString();
        if(tipos.endsWith(", "))
            return tipos.substring(0, tipos.length() - 2);
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }
}
