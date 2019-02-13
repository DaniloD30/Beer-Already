package com.example.leese.beer;

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

import adapters.AdapterListaEstabelecimento;
import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;
import model.Estabelecimento;

public class ListarEstabelecimentoActivity extends AppCompatActivity {

    private AdapterListaEstabelecimento adapterListaEstabelecimento;

    private ListView lsvEstabelecimento;
    private List<Estabelecimento> estabelecimentoList;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private EstabelecimentoDao dao = new EstabelecimentoDao(conexaoSQLite);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estabelecimento);

        try {
            estabelecimentoList = new ArrayList<>();
            this.estabelecimentoList = dao.retornarTodos();

            lsvEstabelecimento = (ListView) findViewById(R.id.lsvEstabelecimento);

            this.adapterListaEstabelecimento = new AdapterListaEstabelecimento(ListarEstabelecimentoActivity.this, this.estabelecimentoList);

            this.lsvEstabelecimento.setAdapter(this.adapterListaEstabelecimento);

        }catch (NullPointerException e){
            Toast.makeText(ListarEstabelecimentoActivity.this, "Lista Vazia", Toast.LENGTH_SHORT).show();
        }

        this.lsvEstabelecimento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        boolean excluiu = dao.excluir(estabelecimentoSelecionado.getId());

                        dialog.cancel();

                        if(excluiu) {
                            adapterListaEstabelecimento.removerBebida(position);
                            Toast.makeText(ListarEstabelecimentoActivity.this, "Bebida excluida com sucessso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListarEstabelecimentoActivity.this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show();
                        }

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






    }

    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterListaEstabelecimento.atualizar(dao.retornarTodos());
    }
}
