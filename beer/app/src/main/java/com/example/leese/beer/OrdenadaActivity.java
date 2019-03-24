package com.example.leese.beer;

import android.app.ProgressDialog;
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
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Bebida;
import model.Cesta;
import model.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdenadaActivity extends AppCompatActivity {

    private AdapterListaOrdenada adapterListaOrdenada;
    private ListView lsvBebidas;
    private List<Bebida> bebidaList;
    private List<Bebida> bebidaListOrdenada;
    private ItemDao dao;
    private BebidaDao daoBebida;
    private Cesta cesta;

    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private RetrofitService service;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenada);

        // Essa logica de preencher a listview das bebidas na cesta, ira mudar, por conta da criação do Item, que é a relação entre cesta
        //  e bebida, assim conseguindo saber quais bebidas pertencem a cesta.

        // Log.d("Ordenada","Tamanho na lista de cestas " + this.listaCestas.size());
        dialog = new ProgressDialog(OrdenadaActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        Bebida bebida = new Bebida();
        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);
        final Call<List<Item>> call = service.getAllItens();
        call.enqueue(new Callback<List<Item>>() {

            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final List<Item> listaItem;
                final List<Integer> listaItensCesta = new ArrayList<>();
                listaItem = response.body();
                bebidaList = new ArrayList<>();


                // dao = new ItemDao(conexaoSQLite);
                //daoBebida = new BebidaDao(conexaoSQLite);

                Bundle bundleDadosBebida = getIntent().getExtras();

                cesta = new Cesta();
                cesta.setId(bundleDadosBebida.getInt("id_cesta"));
                cesta.setNome(bundleDadosBebida.getString("nome"));

                Log.d("Ordenada", "ID CESTA do bundle: " + cesta.getId());
                for (Item i : listaItem
                ) {
                    if (i.getId_cesta() == cesta.getId()) {
                        listaItensCesta.add(i.getId_bebida());
                    }
                }

                RetrofitService service1 = ServiceGenerator
                        .createService(RetrofitService.class);

                Call<List<Bebida>> call1 = service1.getAllBebidas();
                //fazer a busca dos dados no banco de dados


                // bebidaList = new ArrayList<>();
                //this.bebidaList = dao.retornarTodos();


                call1.enqueue(new Callback<List<Bebida>>() {
                    @Override
                    public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        final List<Bebida> bebidaListT;
                        bebidaListT = response.body();
                        for (Bebida b : bebidaListT
                        ) {
                            for (Integer i : listaItensCesta
                            ) {
                                if (b.getId() == i) {
                                    bebidaList.add(b);
                                }
                            }

                        }
                        // this.bebidaList = dao.retornarBebidaByIdCesta(cesta.getId());
                        Collections.sort(bebidaList);
                        Toast.makeText(getBaseContext(), "Bebida inserido com sucesso", Toast.LENGTH_SHORT).show();
                        lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);

                        adapterListaOrdenada = new AdapterListaOrdenada(OrdenadaActivity.this, bebidaList);

                        lsvBebidas.setAdapter(adapterListaOrdenada);

                        lsvBebidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                                        if (excluiu) {
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


                    @Override
                    public void onFailure(Call<List<Bebida>> call, Throwable t) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                    }
                });


                // FAZER A LÓGICA DA MELHOR BEBIDA POR AQUI

                //Itaipava corona skol barata


                // preciso setar a lista de bebidas
                // cesta.setLista(listaCestas.get(cesta.getId()).getListaCesta());

                // A logica da regra de negocio acontece aqui agora.


            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
            }
        });


    }
    //executa o evento de click do botão atualizar
    public void eventAtualizar (View view){
        this.adapterListaOrdenada.atualizar(dao.retornarBebidaByIdCesta(cesta.getId()));
    }
}
