package model;

public class Item {

    private int id_cesta;
    private int id_bebida;

    public Item(int id_cesta, int id_bebida) {
        this.id_cesta = id_cesta;
        this.id_bebida = id_bebida;
    }

    public int getId_cesta() {
        return id_cesta;
    }

    public void setId_cesta(int id_cesta) {
        this.id_cesta = id_cesta;
    }

    public int getId_bebida() {
        return id_bebida;
    }

    public void setId_bebida(int id_bebida) {
        this.id_bebida = id_bebida;
    }
}
