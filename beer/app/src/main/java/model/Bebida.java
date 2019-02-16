package model;

import java.util.ArrayList;
import java.util.List;

public class Bebida implements Comparable<Bebida>{

   // private static int idIncrement = 0;
    private int id;
    private String fabricante;
    private String estabelecimento;
    private Double preco;
    private Double mililitros;
    private List<Integer> listaIdBebidaCesta = new ArrayList<>();
    private int idCesta;

    public Bebida(String fabricante, String estabelecimento, Double preco, double ml) {
       // Bebida.idIncrement++;
        //this.id = Bebida.idIncrement;
        this.estabelecimento = estabelecimento;
        this.fabricante = fabricante;
        this.mililitros = ml;
        this.preco =  preco;
    }
    public Bebida(int id, String fabricante, String estabelecimento, Double preco, double ml) {
        // Bebida.idIncrement++;
        this.id = id;
        this.estabelecimento = estabelecimento;
        this.fabricante = fabricante;
        this.mililitros = ml;
        this.preco =  preco;
    }

    public Bebida(){}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getMililitros() {
        return mililitros;
    }

    public void setMililitros(Double mililitros) {
        this.mililitros = mililitros;
    }

    public void addId(int idBebida){
        listaIdBebidaCesta.add(idBebida);
    }

    public int getIdCesta() {
        return idCesta;
    }

    public void setIdCesta(int idCesta) {
        this.idCesta = idCesta;
    }

    @Override
    public int compareTo(Bebida outraBebida) {

        if ((this.preco/this.mililitros) < (outraBebida.getPreco()/outraBebida.getMililitros())) {
            return -1;
        }
        if ((this.preco/this.mililitros) > (outraBebida.getPreco()/outraBebida.getMililitros())) {
            return 1;
        }

        return 0;
    }
}
