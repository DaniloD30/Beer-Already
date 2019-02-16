package model;

public class Estabelecimento {

    private int id;
    private String nome;
    private String endereço;

    public Estabelecimento(int id, String nome, String endereço){
        this.id = id;
        this.nome = nome;
        this.endereço = endereço;
    }

    public Estabelecimento(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.nome + " - " + this.endereço;
    }
}
