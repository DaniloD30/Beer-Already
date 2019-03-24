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
import java.util.List;

import adapters.AdapterListaEstabelecimento;
import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;
import model.Estabelecimento;

import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Estabelecimento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarEstabelecimentoActivity extends AppCompatActivity {

    private AdapterListaEstabelecimento adapterListaEstabelecimento;

    private ListView lsvEstabelecimento;
    private List<Estabelecimento> estabelecimentoList;

    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private EstabelecimentoDao dao = new EstabelecimentoDao(conexaoSQLite);

    private RetrofitService service;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estabelecimento);
        dialog = new ProgressDialog(ListarEstabelecimentoActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);

        Call<List<Estabelecimento>> call = service.getAll();

        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final List<Estabelecimento> listaEstabelecimento;
                listaEstabelecimento = response.body();
                //  estabelecimentoList = listaEstabelecimento;
                lsvEstabelecimento = (ListView) findViewById(R.id.lsvEstabelecimento);
                //Log.d("listafinal", " " + estabelecimentoList.isEmpty());
                adapterListaEstabelecimento = new AdapterListaEstabelecimento(ListarEstabelecimentoActivity.this, listaEstabelecimento);
                lsvEstabelecimento.setAdapter(adapterListaEstabelecimento);
                lsvEstabelecimento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        final Estabelecimento estabelecimentoSelecionado = (Estabelecimento) adapterListaEstabelecimento.getItem(position);
                        //Toast.makeText(ListarBebidasActivity.this, "Bebida: " + bebidaSelecionada.getFabricante(), Toast.LENGTH_SHORT);
                        final AlertDialog.Builder janelaEscolha = new AlertDialog.Builder(ListarEstabelecimentoActivity.this);

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
                                //boolean excluiu = dao.excluir(estabelecimentoSelecionado.getId());

                                final ProgressDialog dialog1 = new ProgressDialog(ListarEstabelecimentoActivity.this);
                                dialog1.setMessage("Carregando...");
                                dialog1.setCancelable(false);
                                dialog1.show();
                                RetrofitService service1 = ServiceGenerator
                                        .createService(RetrofitService.class);
                                Call<Void> call2 = service1.excluirEstabelecimento(estabelecimentoSelecionado.getId());
                                Log.d("listafinal", "id selecionado " + estabelecimentoSelecionado.getId());
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (dialog1.isShowing())
                                            dialog1.dismiss();
                                        Log.d("listafinal", "to " + response.body().toString());
                                        Toast.makeText(getBaseContext(), "Estabelecimento removido com sucesso", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        if (dialog1.isShowing())
                                            dialog1.dismiss();
                                        Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                        janelaEscolha.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Bundle bundleDadosEstabelecimento = new Bundle();
                                // fabricante, String estabelecimento, Double preco, double ml

                                bundleDadosEstabelecimento.putInt("id_bebida", estabelecimentoSelecionado.getId());
                                bundleDadosEstabelecimento.putString("nome_estabelecimento", estabelecimentoSelecionado.getNome());
                                bundleDadosEstabelecimento.putString("endereco", estabelecimentoSelecionado.getEndereço());


                                Intent intentEditarBebidas = new Intent(ListarEstabelecimentoActivity.this, EditarEstabelecimentoActivity.class);
                                intentEditarBebidas.putExtras(bundleDadosEstabelecimento);
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
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Problema de acesso", Toast.LENGTH_LONG).show();
            }//fim do onFailure
        });
    }






    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        //executa o evento de click do botão atualizar

            dialog = new ProgressDialog(ListarEstabelecimentoActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setCancelable(false);
            dialog.show();
            RetrofitService service = ServiceGenerator
                    .createService(RetrofitService.class);

            Call<List<Estabelecimento>> call = service.getAll();
            //fazer a busca dos dados no banco de dados


            // bebidaList = new ArrayList<>();
            //this.bebidaList = dao.retornarTodos();



            call.enqueue(new Callback<List<Estabelecimento>>() {
                @Override
                public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    final List<Estabelecimento> estabelecimentoList;
                    estabelecimentoList = response.body();
                    adapterListaEstabelecimento.atualizar(estabelecimentoList);
                }

                @Override
                public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                }
            });
        }


/*

    public List<Estabelecimento> carregarEstabelecimento(){
        dialog = new ProgressDialog(ListarEstabelecimentoActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();


        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);

        Call<List<Estabelecimento>>  call = service.getAll();

        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final List<Estabelecimento> listaEstabelecimento ;
                listaEstabelecimento = response.body();


              //  estabelecimentoList = listaEstabelecimento;


            }//fim do onResponse

            @Override
            public void onFailure( Call<List<Estabelecimento>>  call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Problema de acesso", Toast.LENGTH_LONG).show();
            }//fim do onFailure
        });
        return estabelecimentoList;
    }//fim do ca
    */
}

