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
    private int experiencia;
    private List<String> tipos;

    public Pokemon() { }

    public void fromJson(JSONObject jsonPokemon) throws JSONException {
        this.id = jsonPokemon.getInt("id");
        urlImagem = jsonPokemon.getString("sprite");
        this.nome = jsonPokemon.getString("name");
        this.altura = jsonPokemon.getInt("height");
        this.peso = jsonPokemon.getInt("weight");
        this.experiencia = jsonPokemon.getInt("base_experience");
        JSONArray arrayTipos = jsonPokemon.getJSONArray("tipos");
        tipos = new ArrayList<>();
        for(int i = 0; i < arrayTipos.length(); i++){
            tipos.add(arrayTipos.getString(i));
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

    public double getPesoKilos() { return (double)peso/1000; }

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

    public int getExperience() { return this.experiencia; }

    public String toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put("name", this.nome);
            jsonObject.put("height", this.altura);
            jsonObject.put("weight", this.peso);
            jsonObject.put("base_experience", this.experiencia);
            jsonObject.put("sprite", this.urlImagem);

            JSONArray jsonArray = new JSONArray();
            for(String tipo : tipos)
                jsonArray.put(tipo);

            jsonObject.put("tipos", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}