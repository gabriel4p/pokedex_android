package br.com.cursoandroid.pokedex;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import br.com.cursoandroid.pokedex.asynctask.GetPokemonAsyncTask;
import br.com.cursoandroid.pokedex.models.Pokemon;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llBusca;
    private ProgressBar pbEstatistica;
    private EditText txtBusca;
    private ImageView imgPokemon;
    private TextView lblNome;
    private TextView lblAttrId;
    private TextView lblAttrPeso;
    private TextView lblAttrAltura;
    private TextView lblAttrTipo;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    private void initializeComponents(){
        llBusca = (LinearLayout)findViewById(R.id.llBusca);
        pbEstatistica = (ProgressBar)findViewById(R.id.pbEstatistica);
        txtBusca = (EditText)findViewById(R.id.txtBusca);
        imgPokemon = (ImageView)findViewById(R.id.imgPokemon);

        lblNome = (TextView) findViewById(R.id.lblNomePokemon);
        lblAttrId = (TextView) findViewById(R.id.lblAttrId);
        lblAttrPeso = (TextView) findViewById(R.id.lblAttrPeso);
        lblAttrAltura = (TextView) findViewById(R.id.lblAttrAltura);
        lblAttrTipo = (TextView) findViewById(R.id.lblAttrTipo);

        pbEstatistica.setMax(100);
        pbEstatistica.setProgress(30);

        llBusca.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando pokemon...");
        progressDialog.setCancelable(false);
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
        String id = txtBusca.getText().toString();
        txtBusca.setText("");
        if(id.isEmpty()){
            return;
        }
        progressDialog.show();
        new GetPokemonAsyncTask(this).execute(Integer.parseInt(id));
        llBusca.setVisibility(View.INVISIBLE);
    }

    public void preencherDados(Pokemon pokemon){
        lblAttrId.setText(String.format("Id: %s", pokemon.getId()));
        lblAttrAltura.setText(String.format("Altura: %s", pokemon.getAltura()));
        lblAttrPeso.setText(String.format("Peso: %s", pokemon.getPeso()));
        lblAttrTipo.setText(String.format("Tipo: %s", pokemon.getTipos()));
        lblNome.setText(pokemon.getNome().toUpperCase());
        Picasso.with(this).load(pokemon.getUrlImagem()).into(imgPokemon);
        progressDialog.dismiss();
    }
}
