package com.example.leese.beer;

import android.app.ProgressDialog;
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
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Bebida;
import model.Cesta;
import model.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CestaActivity extends AppCompatActivity {

    private AdapterListaBebidas adapterListaBebidas;
    private ListView lsvBebidas;
    private List<Bebida> bebidaList;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

    private BebidaDao dao = new BebidaDao(conexaoSQLite);
    private CestaDao daoCesta = new CestaDao(conexaoSQLite);
    private ItemDao daoItem = new ItemDao(conexaoSQLite);

    private List<Bebida> bebidasCesta;
    private List<Cesta> listaCesta;
    private String nomeCesta;
    private Bebida bebidaSelecionada;
    private RetrofitService service;
    ProgressDialog dialog;
    Cesta cesta = new Cesta();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);
        dialog = new ProgressDialog(CestaActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);
        Call<List<Bebida>> call = service.getAllBebidas();
        call.enqueue(new Callback<List<Bebida>>() {
            @Override
            public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final List<Bebida> bebidaList;
                bebidaList = response.body();
                listaCesta = new ArrayList<>();
                bebidasCesta = new ArrayList<>();
                //bebidaList = new ArrayList<>();
                //this.bebidaList = dao.retornarTodos();
                lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);
                lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);
                adapterListaBebidas = new AdapterListaBebidas(CestaActivity.this, bebidaList);
                lsvBebidas.setAdapter(adapterListaBebidas);
                lsvBebidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        bebidaSelecionada = (Bebida) adapterListaBebidas.getItem(position);
                        if (!bebidasCesta.contains(bebidaSelecionada)) {
                            bebidasCesta.add(bebidaSelecionada);
                            Toast.makeText(CestaActivity.this, "Bebida adicionada na cesta com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CestaActivity.this, "Bebida ja adicionada anteriormente na cesta ", Toast.LENGTH_SHORT).show();
                        }
                        // PEGAR A BEBIDA SELECIONADA, COLOCAR NA CESTA
                        // PRRENCHER A RECLYCLE VIEW DA CESTA NA TELA " Compare "

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Bebida>> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Problema de acesso", Toast.LENGTH_LONG).show();
            }//fim do onFailure
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtNome = findViewById(R.id.edtNome);
                nomeCesta = txtNome.getText().toString();
                // SE O NOME DA CESTA FOR REPETIDO, VAI TRAZER AS BEBIDAS SELECIONDAS DA PRIMEIRA CESTA
                cesta.setNome(nomeCesta);
                cesta.addBebida(bebidasCesta);
                dialog = new ProgressDialog(CestaActivity.this);
                dialog.setMessage("Carregando...");
                dialog.setCancelable(false);
                dialog.show();
                RetrofitService service = ServiceGenerator
                        .createService(RetrofitService.class);
                final Call<Void> call = service.addCestas(cesta);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Cesta criada com sucesso", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                    }
                });

                // daoCesta.salvar(nomeCesta);
                //cesta = daoCesta.retornarTodosByName(nomeCesta);

                RetrofitService service1 = ServiceGenerator
                        .createService(RetrofitService.class);

                Call<List<Cesta>> call1 = service1.getAllCestas();

                //Preencher a list view com as cestas
                // cestaList = new ArrayList<>();
                //this.cestaList = daoCesta.retornarTodos();
                //Log.d("Compare","Tamanho na lista de cestas " + this.cestaList.size());

                call1.enqueue(new Callback<List<Cesta>>() {
                    @Override
                    public void onResponse(Call<List<Cesta>> calll, Response<List<Cesta>> response) {

                        final List<Cesta> cestaList;
                        cestaList = response.body();
                        List<Item> listaItem = new ArrayList<>();
                        listaItem = getItensCesta(cestaList);
                        RetrofitService service2 = ServiceGenerator
                                .createService(RetrofitService.class);
                        for (Item i: listaItem
                             ) {
                            final Call<Void> call2 = service2.addItem(i);
                            call2.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    //Log.d("CestaActivity","ID CESTA:  " + cesta.getId());
                                    //oolean sucesso = daoItem.salvar(cesta.getId(), b.getId()); // idcesta , idbebi
                                    Toast.makeText(getBaseContext(), "Item inserido com sucesso", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Cesta>> calll, Throwable t) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                    }

                });
                listaCesta.add(cesta);
                //Toast.makeText(CestaActivity.this, "Cesta criada com sucesso", Toast.LENGTH_SHORT).show();

            }
            public List<Item> getItensCesta(List<Cesta> ces) {
                List<Item> lista = new ArrayList<>();
                for (Cesta c : ces
                ) {
                    //  Log.d("Compare","id antes:  " + cesta.getId());
                    if (c.getNome().equalsIgnoreCase(cesta.getNome())) {
                        cesta.setId(c.getId());
                        //    Log.d("Compare","id:  " + cesta.getId());
                        for (Bebida b : bebidasCesta
                        ) {
                            //  Log.d("Compare","id2:  " + cesta.getId());
                            Item item = new Item(cesta.getId(), b.getId());
                            lista.add(item);
                        }
                    }
                }
                return lista;
            }
        });

        //cadastro bebidas
        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CestaActivity.this, CadastroBebidas.class);
                startActivity(it);
            }
        });

        /*
        public List<Cesta> getListCesta() {
            return listaCesta;
        }
           */
    }
    //executa o evento de click do botão atualizar
    public void eventAtualizar (View view){
        this.adapterListaBebidas.atualizar(dao.retornarTodos());
    }
}


