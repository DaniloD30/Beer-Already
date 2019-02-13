package com.example.leese.beer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.AdapterListaBebidas;
import adapters.AdapterListaOrdenada;
import dao.BebidaDao;
import dao.ConexaoSQLite;
import dao.ItemDao;
import model.Bebida;
import model.Cesta;

public class OrdenadaActivity extends AppCompatActivity {

    private AdapterListaOrdenada adapterListaOrdenada;
    private ListView lsvBebidas;
    private List<Bebida> bebidaList;
    private List<Bebida> bebidaListOrdenada;
    private ItemDao dao;
    private BebidaDao daoBebida;
    private Cesta cesta;

    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenada);

        // Essa logica de preencher a listview das bebidas na cesta, ira mudar, por conta da criação do Item, que é a relação entre cesta
        //  e bebida, assim conseguindo saber quais bebidas pertencem a cesta.

       // Log.d("Ordenada","Tamanho na lista de cestas " + this.listaCestas.size());
        this.bebidaList = new ArrayList<>();

        dao = new ItemDao(conexaoSQLite);
        daoBebida = new BebidaDao(conexaoSQLite);

        Bundle bundleDadosBebida = getIntent().getExtras();

        cesta = new Cesta();
        cesta.setId(bundleDadosBebida.getInt("id_cesta"));
        cesta.setNome(bundleDadosBebida.getString("nome"));

        Log.d("Ordenada","ID CESTA do bundle: " + cesta.getId());

        this.bebidaList = dao.retornarBebidaByIdCesta(cesta.getId());
        Collections.sort(this.bebidaList);

        // FAZER A LÓGICA DA MELHOR BEBIDA POR AQUI

        //Itaipava corona skol barata


        // preciso setar a lista de bebidas
       // cesta.setLista(listaCestas.get(cesta.getId()).getListaCesta());

        // A logica da regra de negocio acontece aqui agora.

        lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);

        this.adapterListaOrdenada = new AdapterListaOrdenada(OrdenadaActivity.this,  this.bebidaList);

        this.lsvBebidas.setAdapter(this.adapterListaOrdenada);

        this.lsvBebidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Bebida bebidaSelecionada = (Bebida) adapterListaOrdenada.getItem(position);
                //Toast.makeText(ListarBebidasActivity.this, "Bebida: " + bebidaSelecionada.getFabricante(), Toast.LENGTH_SHORT);
                final AlertDialog.Builder janelaEscolha = new AlertDialog.Builder(OrdenadaActivity.this);

                janelaEscolha.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                janelaEscolha.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //excluir o produto
                        boolean excluiu = dao.excluir(bebidaSelecionada.getId());

                        dialog.cancel();

                        if(excluiu) {
                            adapterListaOrdenada.removerBebida(position);
                            Toast.makeText(OrdenadaActivity.this, "Bebida excluida com sucessso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrdenadaActivity.this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                janelaEscolha.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundleDadosBebidas = new Bundle();
                        // fabricante, String estabelecimento, Double preco, double ml

                        bundleDadosBebidas.putInt("id_bebida", bebidaSelecionada.getId());
                        bundleDadosBebidas.putString("nome_fabricante", bebidaSelecionada.getFabricante());
                        bundleDadosBebidas.putString("nome_estabelecimento", bebidaSelecionada.getEstabelecimento());
                        bundleDadosBebidas.putDouble("preco_bebida", bebidaSelecionada.getPreco());
                        bundleDadosBebidas.putDouble("volume_bebida", bebidaSelecionada.getMililitros());


                        Intent intentEditarBebidas = new Intent(OrdenadaActivity.this, EditarActivity.class);
                        intentEditarBebidas.putExtras(bundleDadosBebidas);
                        startActivity(intentEditarBebidas);


                    }
                });

                janelaEscolha.create().show();
            }
        });
    }

    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterListaOrdenada.atualizar(dao.retornarBebidaByIdCesta(cesta.getId()));
    }
}
