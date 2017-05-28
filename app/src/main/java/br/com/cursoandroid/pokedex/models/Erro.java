package br.com.cursoandroid.pokedex.models;

public class Erro {
    private Exception exception;
    private String tipo;
    public Erro(Exception e, String tipo){
        this.exception = e;
        this.tipo = tipo;
    }

    public String getTipo(){ return tipo; }

    public Exception getException(){ return exception; }
}
