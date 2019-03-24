package com.example.leese.beer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterListaBebidas;
import dao.BebidaDao;
import dao.ConexaoSQLite;
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Bebida;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarBebidasActivity extends AppCompatActivity {

    private AdapterListaBebidas adapterListaBebidas;
    private ListView lsvBebidas;
    private List<Bebida> bebidaList;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private BebidaDao dao = new BebidaDao(conexaoSQLite);
    private RetrofitService service;
    ProgressDialog dialog;

   // private List<Bebida> aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_bebidas);
        dialog = new ProgressDialog(ListarBebidasActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);

        Call<List<Bebida>> call = service.getAllBebidas();
        //fazer a busca dos dados no banco de dados


           // bebidaList = new ArrayList<>();
            //this.bebidaList = dao.retornarTodos();



        call.enqueue(new Callback<List<Bebida>>() {
            @Override
            public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final List<Bebida> bebidaList;
                bebidaList = response.body();
                //  estabelecimentoList = listaEstabelecimento;
                lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);

                adapterListaBebidas = new AdapterListaBebidas(ListarBebidasActivity.this, bebidaList);

                lsvBebidas.setAdapter(adapterListaBebidas);
                lsvBebidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final Bebida bebidaSelecionada = (Bebida) adapterListaBebidas.getItem(position);
                        //Toast.makeText(ListarBebidasActivity.this, "Bebida: " + bebidaSelecionada.getFabricante(), Toast.LENGTH_SHORT);
                        final AlertDialog.Builder janelaEscolha = new AlertDialog.Builder(ListarBebidasActivity.this);

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
                                    adapterListaBebidas.removerBebida(position);
                                    Toast.makeText(ListarBebidasActivity.this, "Bebida excluida com sucessso", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ListarBebidasActivity.this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show();
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


                                Intent intentEditarBebidas = new Intent(ListarBebidasActivity.this, EditarActivity.class);
                                intentEditarBebidas.putExtras(bundleDadosBebidas);
                                startActivity(intentEditarBebidas);


                            }
                        });

                        janelaEscolha.create().show();
                    }
                });
            }//fim do on
            //estabelecimentoList = new ArrayList<>();
            //this.estabelecimentoList = carregarEstabelecimento();
            @Override
            public void onFailure(Call<List<Bebida>> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Problema de acesso", Toast.LENGTH_LONG).show();
            }//fim do onFailure
        });
    }

        // algoritmo para pegar item selecionado pelo usuario
        // isso vai servir pra selecionar da lista e colocar no carrinho
        // provavelmente para calcular a " melhor bebida ", trabalharemos com uma query que pega os mls, coloca em uma lista, dessa lista
        // irei fazer a regra de negocio para calcular.










    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterListaBebidas.atualizar(dao.retornarTodos());
    }
}
