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

import adapters.AdapterListaBebidas;
import dao.BebidaDao;
import dao.ConexaoSQLite;
import model.Bebida;

public class ListarBebidasActivity extends AppCompatActivity {

    private AdapterListaBebidas adapterListaBebidas;
    private ListView lsvBebidas;
    private List<Bebida> bebidaList;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private BebidaDao dao = new BebidaDao(conexaoSQLite);

   // private List<Bebida> aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_bebidas);
        //fazer a busca dos dados no banco de dados

        try {
            bebidaList = new ArrayList<>();
            this.bebidaList = dao.retornarTodos();

            lsvBebidas = (ListView) findViewById(R.id.lsvBebidas);

            this.adapterListaBebidas = new AdapterListaBebidas(ListarBebidasActivity.this, this.bebidaList);

            this.lsvBebidas.setAdapter(this.adapterListaBebidas);

        }catch (NullPointerException e){
            Toast.makeText(ListarBebidasActivity.this, "Lista Vazia", Toast.LENGTH_SHORT).show();
        }


        // algoritmo para pegar item selecionado pelo usuario
        // isso vai servir pra selecionar da lista e colocar no carrinho
        // provavelmente para calcular a " melhor bebida ", trabalharemos com uma query que pega os mls, coloca em uma lista, dessa lista
        // irei fazer a regra de negocio para calcular.

        this.lsvBebidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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






    }

    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterListaBebidas.atualizar(dao.retornarTodos());
    }
}
