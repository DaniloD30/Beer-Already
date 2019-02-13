package com.example.leese.beer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterCestas;
import adapters.AdapterListaBebidas;
import dao.BebidaDao;
import dao.CestaDao;
import dao.ConexaoSQLite;
import model.Bebida;
import model.Cesta;

public class Compare extends AppCompatActivity {

    private AdapterCestas adapterCestas;
    private ListView lsvCestas;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private CestaDao daoCesta = new CestaDao(conexaoSQLite);
    private List<Cesta> cestaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Preencher a list view com as cestas
        cestaList = new ArrayList<>();
        this.cestaList = daoCesta.retornarTodos();
        //Log.d("Compare","Tamanho na lista de cestas " + this.cestaList.size());

        lsvCestas = (ListView) findViewById(R.id.lsvCestas);

        this.adapterCestas = new AdapterCestas(Compare.this, this.cestaList);

        this.lsvCestas.setAdapter(this.adapterCestas);

        this.lsvCestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Cesta cestaSelecionada = (Cesta) adapterCestas.getItem(position);
                Bundle bundleDadosCesta = new Bundle();
                // fabricante, String estabelecimento, Double preco, double ml
                Log.d("CestaActivity","ID CESTA COMPARE:  " + cestaSelecionada.getId());
                bundleDadosCesta.putInt("id_cesta", cestaSelecionada.getId());
                bundleDadosCesta.putString("nome", cestaSelecionada.getNome());

                Intent intentEditarBebidas = new Intent(Compare.this, OrdenadaActivity.class);
                intentEditarBebidas.putExtras(bundleDadosCesta);
                startActivity(intentEditarBebidas);

               // Toast.makeText(CestaActivity.this, "Bebida adicionada na cesta com sucesso", Toast.LENGTH_SHORT).show();

                // PEGAR A BEBIDA SELECIONADA, COLOCAR NA CESTA
                // PRRENCHER A RECLYCLE VIEW DA CESTA NA TELA " Compare "

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            Intent intent = new Intent(Compare.this, CestaActivity.class);
                            startActivity(intent);

            }
        });

    }
    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterCestas.atualizar(daoCesta.retornarTodos());
    }

}
