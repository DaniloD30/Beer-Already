package model;

import java.util.ArrayList;
import java.util.List;

public class Cesta {

    //private static int idIncrement = 0;

    private int id;
    private String nome;
    private List<Bebida> lista;

    public Cesta(){
      //  idIncrement++;
       // this.setId(idIncrement);
        lista = new ArrayList<>();
    }

    /*
    public Cesta(int id){
        this.setId(id);
    }]*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void addBebida(List<Bebida> listaBebida){
        lista = listaBebida;
    }

    public List<Bebida> getListaCesta(){
        return lista;
    }

    public void setLista(List<Bebida> lista) {
        this.lista = lista;
    }


}
