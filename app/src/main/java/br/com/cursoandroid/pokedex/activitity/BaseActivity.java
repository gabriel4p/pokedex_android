package br.com.cursoandroid.pokedex.activitity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import br.com.cursoandroid.pokedex.R;

/**
 * Created by nemesis on 5/27/17.
 */

public class BaseActivity extends AppCompatActivity {

    private AlertDialog.Builder alert;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        alert = new AlertDialog.Builder(this);
    }

    protected void showLoader(String text){
        progress.setMessage(text);
        progress.show();
    }

    protected void hideLoader(){
        progress.dismiss();
    }

    protected void showError(String text){
        alert.setMessage(text);
        alert.show();
    }
}
