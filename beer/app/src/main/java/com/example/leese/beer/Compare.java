package com.example.leese.beer;

import android.app.ProgressDialog;
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
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Bebida;
import model.Cesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Compare extends AppCompatActivity {

    private AdapterCestas adapterCestas;
    private ListView lsvCestas;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private CestaDao daoCesta = new CestaDao(conexaoSQLite);
    private List<Cesta> cestaList;
    private RetrofitService service;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(Compare.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);

        Call<List<Cesta>> call = service.getAllCestas();
        Log.d("Compare"," entrou" );
        //Preencher a list view com as cestas
         // cestaList = new ArrayList<>();
        //this.cestaList = daoCesta.retornarTodos();
        //Log.d("Compare","Tamanho na lista de cestas " + this.cestaList.size());

        call.enqueue(new Callback<List<Cesta>>() {
            @Override
            public void onResponse(Call<List<Cesta>> call, Response<List<Cesta>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final List<Cesta> cestaList;
                cestaList = response.body();
                Log.d("Compare"," " + cestaList.get(0));
                lsvCestas = (ListView) findViewById(R.id.lsvCestas);

                adapterCestas = new AdapterCestas(Compare.this, cestaList);
                lsvCestas.setAdapter(adapterCestas);

                lsvCestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        final Cesta cestaSelecionada = (Cesta) adapterCestas.getItem(position);
                        Bundle bundleDadosCesta = new Bundle();
                        // fabricante, String estabelecimento, Double preco, double ml
                        Log.d("CestaActivity", "ID CESTA COMPARE:  " + cestaSelecionada.getId());
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
            }
                @Override
                public void onFailure(Call<List<Cesta>> call, Throwable t) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Problema de acesso", Toast.LENGTH_LONG).show();
                }//fim do onFailure
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
