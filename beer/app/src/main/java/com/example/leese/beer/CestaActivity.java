package com.example.leese.beer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterListaBebidas;
import dao.BebidaDao;
import dao.CestaDao;
import dao.ConexaoSQLite;
import dao.ItemDao;
import model.Bebida;
import model.Cesta;

public class CestaActivity extends AppCompatActivity {

    private AdapterListaBebidas adapterListaBebidas;
    private ListView lsvBebidas;
    private List<Bebida> bebidaList;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

    private BebidaDao dao = new BebidaDao(conexaoSQLite);
    private CestaDao daoCesta = new CestaDao(conexaoSQLite);
    private ItemDao daoItem = new ItemDao(conexaoSQLite);

    private List<Bebida> bebidasCesta;
    private  List<Cesta> listaCesta;
    private String nomeCesta;
    private Bebida bebidaSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        listaCesta = new ArrayList<>();
        bebidasCesta = new ArrayList<>();
        bebidaList = new ArrayList<>();
        this.bebidaList = dao.retornarTodos();

        lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);

        this.adapterListaBebidas = new AdapterListaBebidas(CestaActivity.this, this.bebidaList);

        this.lsvBebidas.setAdapter(this.adapterListaBebidas);



        this.lsvBebidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                bebidaSelecionada = (Bebida) adapterListaBebidas.getItem(position);
                if(!bebidasCesta.contains(bebidaSelecionada)) {
                    bebidasCesta.add(bebidaSelecionada);
                    Toast.makeText(CestaActivity.this, "Bebida adicionada na cesta com sucesso", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CestaActivity.this, "Bebida ja adicionada anteriormente na cesta ", Toast.LENGTH_SHORT).show();
                }
                    // PEGAR A BEBIDA SELECIONADA, COLOCAR NA CESTA
                    // PRRENCHER A RECLYCLE VIEW DA CESTA NA TELA " Compare "

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtNome = findViewById(R.id.edtNome);
                nomeCesta = txtNome.getText().toString();
                // SE O NOME DA CESTA FOR REPETIDO, VAI TRAZER AS BEBIDAS SELECIONDAS DA PRIMEIRA CESTA
                Cesta cesta = new Cesta();
                cesta.setNome(nomeCesta);
                cesta.addBebida(bebidasCesta);
                daoCesta.salvar(nomeCesta);
                cesta = daoCesta.retornarTodosByName(nomeCesta);

                for (Bebida b: bebidasCesta
                     ) {
                    Log.d("CestaActivity","ID CESTA:  " + cesta.getId());
                    boolean sucesso = daoItem.salvar(cesta.getId(), b.getId()); // idcesta , idbebida
                    if(sucesso)
                        Log.d("ERRO LISTA PRODUTOS", "SUCESSO AO SALVAR");
                    else
                        Log.d("ERRO LISTA PRODUTOS", "ERRO AO SALVAR");
                }


                listaCesta.add(cesta);

                Toast.makeText(CestaActivity.this, "Cesta criada com sucesso", Toast.LENGTH_SHORT).show();

            }
        });
        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CestaActivity.this, CadastroBebidas.class);
                startActivity(it);
            }
        });
    }

    public List<Cesta> getListCesta(){
        return listaCesta;
    }

    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterListaBebidas.atualizar(dao.retornarTodos());
    }
}
