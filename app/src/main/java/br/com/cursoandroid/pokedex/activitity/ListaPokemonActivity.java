package br.com.cursoandroid.pokedex.activitity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.cursoandroid.pokedex.R;

public class ListaPokemonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pokemon);

        setTitle("Listagem");
    }
}
