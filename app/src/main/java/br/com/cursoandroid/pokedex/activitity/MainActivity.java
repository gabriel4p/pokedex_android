package br.com.cursoandroid.pokedex.activitity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.cursoandroid.pokedex.R;
import br.com.cursoandroid.pokedex.contract.PokemonAsyncTaskHandler;
import br.com.cursoandroid.pokedex.models.Constantes;
import br.com.cursoandroid.pokedex.models.Erro;
import br.com.cursoandroid.pokedex.models.Pokemon;
import br.com.cursoandroid.pokedex.repository.PokemonRepository;

public class MainActivity extends BaseActivity implements PokemonAsyncTaskHandler{

    private LinearLayout llBusca;
    private ProgressBar pbEstatistica;
    private EditText txtBusca;
    private ImageView imgPokemon;
    private TextView lblNome;
    private TextView lblAttrId;
    private TextView lblAttrPeso;
    private TextView lblAttrAltura;
    private TextView lblAttrTipo;
    private TextView lblEstatistica;

    private CardView cardPokemon;
    private PokemonRepository pokemonRepository;
    private Pokemon pokemonAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        pokemonRepository = PokemonRepository.getInstance();
        pokemonRepository.setListener(this);
        pokemonRepository.loadFromFile(getSharedPreferences(Constantes.FILE_POKEMON, MODE_PRIVATE));
    }

    private void initializeComponents(){
        llBusca = (LinearLayout)findViewById(R.id.llBusca);
        pbEstatistica = (ProgressBar)findViewById(R.id.pbEstatistica);
        txtBusca = (EditText)findViewById(R.id.txtBusca);
        imgPokemon = (ImageView)findViewById(R.id.imgPokemon);
        cardPokemon = (CardView)findViewById(R.id.cardPokemon);

        lblNome = (TextView) findViewById(R.id.lblNomePokemon);
        lblAttrId = (TextView) findViewById(R.id.lblAttrId);
        lblAttrPeso = (TextView) findViewById(R.id.lblAttrPeso);
        lblAttrAltura = (TextView) findViewById(R.id.lblAttrAltura);
        lblAttrTipo = (TextView) findViewById(R.id.lblAttrTipo);
        lblEstatistica = (TextView) findViewById(R.id.lblEstatistica);

        cardPokemon.setVisibility(View.INVISIBLE);

        llBusca.setVisibility(View.INVISIBLE);

        pbEstatistica.setMax(300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemBusca:
                llBusca.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buscarPokemon(View view){
        int id = 0;

        try{
            id = Integer.parseInt(txtBusca.getText().toString());
        }catch(Exception e) { }

        txtBusca.setText("");

        if(id == 0)
            return;

        llBusca.setVisibility(View.INVISIBLE);
        cardPokemon.setVisibility(View.INVISIBLE);

        Pokemon pokemon = pokemonRepository.getById(id);
        if(pokemon == null){
            showLoader("Buscando online...");
            pokemonRepository.getByIdAsync(id);
        } else {
            preencherDados(pokemon);
            pokemonAtual = pokemon;
        }
    }

    public void preencherDados(Pokemon pokemon){
        hideLoader();
        lblAttrId.setText(String.format("Id: %s", pokemon.getId()));
        lblAttrAltura.setText(String.format("Altura: %scm", pokemon.getAltura()*10));
        lblAttrPeso.setText(String.format("Peso: %s Kg", pokemon.getPesoKilos()));
        lblAttrTipo.setText(String.format("Tipo: %s", pokemon.getTipos()));
        lblNome.setText(pokemon.getNome().toUpperCase());

        int exp = pokemon.getExperience();
        pbEstatistica.setProgress(exp);
        lblEstatistica.setText(String.format("%s/300", exp));

        Picasso.with(this).load(pokemon.getUrlImagem()).into(imgPokemon);
        cardPokemon.setVisibility(View.VISIBLE);
    }

    public final void exibirErro(Erro erro){
        hideLoader();
        switch(erro.getTipo()){
            case Constantes.ERRO_NETWORK:
                showError("Verifique sua conexão com a internet");
                break;
            case Constantes.ERRO_NOT_FOUND:
                showError("Pokémon não encontrado");
                break;
            default:
                showError("Ocorreu um erro durante a operação");
                break;
        }
    }

    @Override
    public void tratarErro(Erro erro) {
        final Erro erroFinal = erro;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                exibirErro(erroFinal);
            }
        });
    }

    @Override
    public void receberPokemon(Pokemon pokemon) {
        this.pokemonAtual = pokemon;
        preencherDados(pokemon);
    }

    public void refreshPokemonForce(View view){
        cardPokemon.setVisibility(View.INVISIBLE);
        showLoader("Atualização online");
        pokemonRepository.getByIdAsync(pokemonAtual.getId());
    }

    public void abrirListagem(View view){
        Intent intent = new Intent(this, ListaPokemonActivity.class);
        startActivity(intent);
    }
}
